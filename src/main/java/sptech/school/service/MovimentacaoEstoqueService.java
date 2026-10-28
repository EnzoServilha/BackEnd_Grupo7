package sptech.school.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sptech.school.dto.dashboard.CotacoesPorcentagemDto;
import sptech.school.dto.dashboard.DashboardMensalDto;
import sptech.school.dto.dashboard.FornecedorEstatisticasDto;
import sptech.school.dto.dashboard.PrecosMediosDto;
import sptech.school.dto.movimentacaoEstoque.*;
import sptech.school.entity.*;
import sptech.school.exception.EntidadeNaoEncontradaException;
import sptech.school.exception.EntidadeConflitanteException;
import sptech.school.exception.MovimentacaoNaoEncontrada;
import sptech.school.mapper.MovimentacaoEstoqueMapper;
import sptech.school.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class MovimentacaoEstoqueService {
    private MovimentacaoRepository movimentacaoRepository;
    private UsuarioRepository usuarioRepository;
    private TipoRepository tipoRepository;
    private StatusRepository statusRepository;
    private ClienteRepository clienteRepository;
    private FornecedorRepository fornecedorRepository;
    private PeriodoRepository periodoRepository;
    private ItensNaMovimentacaoRepository itensNaMovimentacaoRepository;

    public MovimentacaoEstoqueService(MovimentacaoRepository movimentacaoRepository, UsuarioRepository usuarioRepository, TipoRepository tipoRepository, StatusRepository statusRepository, ClienteRepository clienteRepository, FornecedorRepository fornecedorRepository, PeriodoRepository periodoRepository, ItensNaMovimentacaoRepository itensNaMovimentacaoRepository) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoRepository = tipoRepository;
        this.statusRepository = statusRepository;
        this.clienteRepository = clienteRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.periodoRepository = periodoRepository;
        this.itensNaMovimentacaoRepository = itensNaMovimentacaoRepository;
    }

    @Transactional
    public MovimentacaoEstoqueResponseDto buscarPorId(Integer id){
        MovimentacaoEstoque movimentacao = movimentacaoRepository.findById(id).orElseThrow(() -> new MovimentacaoNaoEncontrada("Movimentação não encontrada"));


        return MovimentacaoEstoqueMapper.toResponse(movimentacao);
    }


    @Transactional
    public List<MovimentacaoEstoqueResponsePagebleDto> listarPorPeriodoAtual(Integer idPeriodo, Pageble pagebleRequest) {
        System.out.println("PAGINA: " + pagebleRequest.getPage());
        System.out.println("TAMANHO: " + pagebleRequest.getSize());

        int pagina = pagebleRequest.getPage() != null ? pagebleRequest.getPage() : 0;
        int tamanho = pagebleRequest.getSize() != null ? pagebleRequest.getSize() : 10;

        int offset = pagina * tamanho;

        System.out.println("OFFSET CALCULADO: " + offset);

        long totalRegistros = movimentacaoRepository.contarPorPeriodo(idPeriodo);

        List<MovimentacaoEstoque> listaPaginada = movimentacaoRepository.listarComPaginacaoNativa(idPeriodo, tamanho, offset);

        return listaPaginada.stream()
                .map(entity -> MovimentacaoEstoqueMapper.toResponsePageble(entity, pagebleRequest))
                .toList();
    }



    @Transactional
    public List<MovimentacaoEstoqueResponseDto> buscarPorTipo(String tipo){
        List<MovimentacaoEstoque> movimentacao = movimentacaoRepository.buscarPorTipo(tipo);


        return MovimentacaoEstoqueMapper.toResponseDtoList(movimentacao);
    }

    @Transactional
    public List<MovimentacaoEstoqueResponseDto> buscarPorStatus(String status){
        List<MovimentacaoEstoque> movimentacao = movimentacaoRepository.buscarPorStatus(status);


        return MovimentacaoEstoqueMapper.toResponseDtoList(movimentacao);
    }

    @Transactional
    public MovimentacaoEstoqueResponseDto criar(MovimentacaoEstoqueRequestDto request, String emailUsuario){

        MovimentacaoEstoque movimentacao = MovimentacaoEstoqueMapper.toEntity(request);

        // Todo pedido novo nasce PENDENTE; o status só muda ao concluir ou cancelar
        movimentacao.setStatus(buscarStatus("PENDENTE"));
        movimentacao.setDataMovimentacao(LocalDateTime.now());
        preencher(movimentacao, request, emailUsuario);
        validarCriacao(movimentacao);

        return  MovimentacaoEstoqueMapper.toResponse(movimentacaoRepository.save(movimentacao));
    }
    @Transactional
    public MovimentacaoEstoqueResponseDto fecharCotacao(Integer cotacaoId,
                                                        FechamentoCotacaoRequestDto request,
                                                        String emailUsuario) {
        Periodo periodoVenda = periodoRepository.buscarPeriodoAbertoComBloqueio(request.periodoId())
                .orElseThrow(() -> new EntidadeConflitanteException("Vendas só podem usar períodos abertos"));

        MovimentacaoEstoque cotacao = movimentacaoRepository.buscarPorIdComBloqueio(cotacaoId)
                .orElseThrow(() -> new MovimentacaoNaoEncontrada("Cotação não encontrada"));
        validarCotacaoAberta(cotacao);

        Map<Integer, ItensNaMovimentacao> itensCotados = indexarItensCotados(cotacao);
        validarItensVendidos(request.itens(), itensCotados);
        validarEstoqueDisponivel(request.itens(), request.periodoId());

        Usuario usuario = usuarioRepository.findByEmailAndAtivoTrue(emailUsuario)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário ativo não encontrado", emailUsuario));
        Tipo tipoSaida = tipoRepository.findByNome("SAIDA")
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Tipo SAIDA não encontrado", "SAIDA"));

        // A saída gerada nasce PENDENTE e sem data de entrega:
        // a entrega (e o atraso) são definidos quando ela for concluída.
        MovimentacaoEstoque venda = new MovimentacaoEstoque();
        venda.setUsuario(usuario);
        venda.setTipo(tipoSaida);
        venda.setStatus(buscarStatus("PENDENTE"));
        venda.setCliente(cotacao.getCliente());
        venda.setMovimentacaoOriginal(cotacao);
        venda.setDataMovimentacao(LocalDateTime.now());
        venda.setTotalGastoImpostos(request.totalGastoImpostos());
        venda.setPrecoFrete(request.precoFrete());
        venda.setDataEntregaPrevista(request.dataEntregaPrevista());
        venda.setDataEntrega(null);
        venda.setObservacoes(request.observacoes());
        venda.setNumeroNotaFiscal(request.numeroNotaFiscal());
        venda.setPeriodo(periodoVenda);
        venda = movimentacaoRepository.save(venda);

        MovimentacaoEstoque vendaPersistida = venda;
        List<ItensNaMovimentacao> itensVenda = request.itens().stream().map(itemRequest -> {
            ItensNaMovimentacao itemVenda = new ItensNaMovimentacao();
            itemVenda.setMovimentacaoEstoque(vendaPersistida);
            itemVenda.setItem(itensCotados.get(itemRequest.itemId()).getItem());
            itemVenda.setQtd(itemRequest.qtd());
            itemVenda.setPrecoUnitario(itemRequest.precoUnitario());
            return itemVenda;
        }).toList();
        itensNaMovimentacaoRepository.saveAll(itensVenda);
        venda.setItens(itensVenda);

        // Completa: mesmos itens e mesmas quantidades da cotação. Qualquer diferença = parcial.
        boolean vendaCompleta = itensCotados.size() == request.itens().size()
                && request.itens().stream().allMatch(itemRequest ->
                itensCotados.get(itemRequest.itemId()).getQtd().equals(itemRequest.qtd()));
        String statusFinal = vendaCompleta ? "CONCLUIDO COMPLETAMENTE" : "CONCLUIDO PARCIALMENTE";
        cotacao.setStatus(buscarStatus(statusFinal));
        movimentacaoRepository.save(cotacao);

        return MovimentacaoEstoqueMapper.toResponse(venda);
    }
    // Conclui ou cancela várias movimentações de uma vez (tudo ou nada).
// Cotações não são concluídas aqui: o fechamento gera a venda (fecharCotacao).
    @Transactional
    public List<MovimentacaoEstoqueResponseDto> alterarStatus(AlteracaoStatusRequestDto request) {
        List<MovimentacaoEstoque> movimentacoes = request.ids().stream()
                .distinct()
                .sorted()
                .map(id -> movimentacaoRepository.buscarPorIdComBloqueio(id)
                        .orElseThrow(() -> new MovimentacaoNaoEncontrada("Movimentação " + id + " não encontrada")))
                .toList();

        // Entradas primeiro: assim, no mesmo lote, o estoque que chega já conta para as saídas
        List<MovimentacaoEstoque> ordemDeProcessamento = new ArrayList<>(movimentacoes);
        ordemDeProcessamento.sort(Comparator.comparingInt(m -> ehTipo(m, "ENTRADA") ? 0 : 1));

        for (MovimentacaoEstoque movimentacao : ordemDeProcessamento) {
            switch (request.acao()) {
                case CONCLUIR -> concluir(movimentacao);
                case CANCELAR -> cancelarEntidade(movimentacao);
            }
            // grava agora para a próxima conferência de saldo enxergar esta conclusão
            movimentacaoRepository.flush();
        }

        return MovimentacaoEstoqueMapper.toResponseDtoList(movimentacaoRepository.saveAll(movimentacoes));
    }

    private void concluir(MovimentacaoEstoque movimentacao) {
        validarPeriodoAberto(movimentacao);
        validarPendente(movimentacao);

        if (ehTipo(movimentacao, "COTACAO")) {
            throw new EntidadeConflitanteException(
                    "Cotações só podem ser concluídas pelo fechamento, que gera a venda");
        }
        if (!ehTipo(movimentacao, "ENTRADA") && !ehTipo(movimentacao, "SAIDA")) {
            throw new EntidadeConflitanteException("Somente entradas e saídas podem ser concluídas");
        }

        // A saída só desconta do estoque ao ser concluída, então é aqui que o saldo é conferido
        if (ehTipo(movimentacao, "SAIDA")) {
            validarSaldoParaConclusao(movimentacao);
        }

        LocalDate hoje = LocalDate.now();
        movimentacao.setDataEntrega(hoje);
        movimentacao.setStatus(buscarStatus(
                calcularStatusConclusao(movimentacao.getDataEntregaPrevista(), hoje)));
    }

    private void validarSaldoParaConclusao(MovimentacaoEstoque saida) {
        List<ItensNaMovimentacao> itens =
                itensNaMovimentacaoRepository.findAllByMovimentacaoEstoqueId(saida.getId());

        for (ItensNaMovimentacao item : itens) {
            Long saldoConsultado = periodoRepository.pegarSaldoDisponivelDoItem(
                    saida.getPeriodo().getId(), item.getItem().getId());
            long saldoDisponivel = saldoConsultado == null ? 0L : saldoConsultado;

            if (saldoDisponivel < item.getQtd()) {
                throw new EntidadeConflitanteException(
                        "Estoque insuficiente para o item %s. Quantidade da saída: %d. Quantidade disponível: %d"
                                .formatted(item.getItem().getCodigoInterno(), item.getQtd(), saldoDisponivel));
            }
        }
    }

    private boolean ehTipo(MovimentacaoEstoque movimentacao, String nomeTipo) {
        return movimentacao.getTipo() != null && nomeTipo.equals(movimentacao.getTipo().getNome());
    }

    private void cancelarEntidade(MovimentacaoEstoque movimentacao) {
        if (movimentacao.getStatus() != null && "CANCELADO".equals(movimentacao.getStatus().getNome())) {
            return;
        }
        validarPendente(movimentacao);
        movimentacao.setStatus(buscarStatus("CANCELADO"));
    }

    private String calcularStatusConclusao(LocalDate prevista, LocalDate entrega) {
        return (prevista != null && prevista.isBefore(entrega)) ? "CONCLUIDO COM ATRASO" : "CONCLUIDO";
    }

    private Status buscarStatus(String nome) {
        return statusRepository.findByNome(nome)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Status " + nome + " não encontrado", nome));
    }

    private void validarPeriodoAberto(MovimentacaoEstoque movimentacao) {
        if (movimentacao.getPeriodo() != null && Boolean.TRUE.equals(movimentacao.getPeriodo().getFechado())) {
            throw new EntidadeConflitanteException("Movimentações de períodos fechados não podem ser alteradas");
        }
    }

    public MovimentacaoEstoqueResponseDto editar(MovimentacaoEstoqueRequestDto request, Integer id, String emailUsuario){
        MovimentacaoEstoque movimentacao = movimentacaoRepository.findById(id)
                .orElseThrow(() -> new MovimentacaoNaoEncontrada("Movimentação não encontrada para edição"));
        validarEdicao(movimentacao);

        MovimentacaoEstoqueMapper.atualizar(movimentacao, request);
        preencher(movimentacao, request, emailUsuario);

        return MovimentacaoEstoqueMapper.toResponse(movimentacaoRepository.save(movimentacao));
    }

    @Transactional
    public void cancelar(Integer id){
        MovimentacaoEstoque movimentacao = movimentacaoRepository.findById(id)
                .orElseThrow(() -> new MovimentacaoNaoEncontrada("Movimentação não encontrada"));
        cancelarEntidade(movimentacao);
        movimentacaoRepository.save(movimentacao);
    }

    public void deletar(Integer id){
        cancelar(id);
    }

    public void existe(Integer id){
        boolean existe = movimentacaoRepository.existsById(id);
        if(!existe){
            throw new MovimentacaoNaoEncontrada("Movimentação não encontrada para edição");
        }
    }


    // O status não é definido aqui: ele só muda por concluir, cancelar ou fecharCotacao.
    public void preencher(MovimentacaoEstoque entidade, MovimentacaoEstoqueRequestDto requestDto, String emailUsuario){
        Usuario usuario = usuarioRepository.findByEmailAndAtivoTrue(emailUsuario)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário ativo não encontrado", emailUsuario));
        entidade.setUsuario(usuario);

        if(requestDto.tipoId() != null){
            entidade.setTipo(tipoRepository.findById(requestDto.tipoId())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Tipo não encontrado", requestDto.tipoId())));
        }

        if(requestDto.clienteId() != null){
            entidade.setCliente(clienteRepository.findById(requestDto.clienteId())
                    .filter(encontrado -> Boolean.TRUE.equals(encontrado.getAtivo()))
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente ativo não encontrado", requestDto.clienteId())));
        }

        if(requestDto.fornecedorId() != null){
            entidade.setFornecedor(fornecedorRepository.findById(requestDto.fornecedorId())
                    .filter(encontrado -> Boolean.TRUE.equals(encontrado.getAtivo()))
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Fornecedor ativo não encontrado", requestDto.fornecedorId())));
        }

        if (requestDto.movimentacaoOriginalId() != null) {
            entidade.setMovimentacaoOriginal(movimentacaoRepository.findById(requestDto.movimentacaoOriginalId())
                    .orElseThrow(() -> new MovimentacaoNaoEncontrada("Movimentação original não encontrada")));
        }

        entidade.setPeriodo(periodoRepository.findById(requestDto.periodoId())
                .filter(periodo -> Boolean.FALSE.equals(periodo.getFechado()))
                .orElseThrow(() -> new EntidadeConflitanteException("Movimentações só podem usar períodos abertos")));

    }

    private void validarPendente(MovimentacaoEstoque movimentacao) {
        if (movimentacao.getStatus() == null || !"PENDENTE".equals(movimentacao.getStatus().getNome())) {
            throw new EntidadeConflitanteException("Somente movimentações pendentes podem ser alteradas ou canceladas");
        }
    }

    private void validarEdicao(MovimentacaoEstoque movimentacao) {
        validarPendente(movimentacao);
        if (movimentacao.getTipo() != null && "COTACAO".equals(movimentacao.getTipo().getNome())) {
            throw new EntidadeConflitanteException("Cotações são imutáveis e não podem ser editadas");
        }
    }

    private void validarCriacao(MovimentacaoEstoque movimentacao) {
        if (movimentacao.getTipo() == null) {
            throw new EntidadeConflitanteException("O tipo da movimentação é obrigatório");
        }
        if ("SAIDA".equals(movimentacao.getTipo().getNome())) {
            throw new EntidadeConflitanteException("Uma saída deve ser criada pelo fechamento de uma cotação");
        }
        if ("COTACAO".equals(movimentacao.getTipo().getNome())) {
            if (movimentacao.getCliente() == null) {
                throw new EntidadeConflitanteException("Uma cotação deve possuir um cliente");
            }
            if (movimentacao.getMovimentacaoOriginal() != null) {
                throw new EntidadeConflitanteException("Uma cotação não pode possuir movimentação original");
            }
        }
    }

    private void validarCotacaoAberta(MovimentacaoEstoque cotacao) {
        if (cotacao.getTipo() == null || !"COTACAO".equals(cotacao.getTipo().getNome())) {
            throw new EntidadeConflitanteException("A movimentação informada não é uma cotação");
        }
        if (cotacao.getStatus() == null || !"PENDENTE".equals(cotacao.getStatus().getNome())) {
            throw new EntidadeConflitanteException("Somente cotações pendentes podem ser fechadas");
        }
    }

    private Map<Integer, ItensNaMovimentacao> indexarItensCotados(MovimentacaoEstoque cotacao) {
        if (cotacao.getItens() == null || cotacao.getItens().isEmpty()) {
            throw new EntidadeConflitanteException("A cotação não possui itens");
        }
        Map<Integer, ItensNaMovimentacao> itensCotados = new HashMap<>();
        for (ItensNaMovimentacao item : cotacao.getItens()) {
            itensCotados.put(item.getItem().getId(), item);
        }
        return itensCotados;
    }

    private void validarItensVendidos(List<ItemFechamentoCotacaoRequestDto> itensVendidos,
                                      Map<Integer, ItensNaMovimentacao> itensCotados) {
        Map<Integer, ItemFechamentoCotacaoRequestDto> itensUnicos = new HashMap<>();
        for (ItemFechamentoCotacaoRequestDto itemVendido : itensVendidos) {
            if (itensUnicos.put(itemVendido.itemId(), itemVendido) != null) {
                throw new EntidadeConflitanteException("Um item não pode aparecer mais de uma vez na venda");
            }
            ItensNaMovimentacao itemCotado = itensCotados.get(itemVendido.itemId());
            if (itemCotado == null) {
                throw new EntidadeConflitanteException("A venda contém item não presente na cotação");
            }
            if (itemVendido.qtd() > itemCotado.getQtd()) {
                throw new EntidadeConflitanteException("A quantidade vendida não pode superar a quantidade cotada");
            }
        }
    }

    private void validarEstoqueDisponivel(List<ItemFechamentoCotacaoRequestDto> itensVendidos,
                                          Integer periodoId) {
        for (ItemFechamentoCotacaoRequestDto itemVendido : itensVendidos) {
            Long saldoConsultado = periodoRepository.pegarSaldoDisponivelDoItem(
                    periodoId, itemVendido.itemId());
            long saldoDisponivel = saldoConsultado == null ? 0L : saldoConsultado;

            if (saldoDisponivel < itemVendido.qtd()) {
                throw new EntidadeConflitanteException(
                        "Estoque insuficiente para o item %d. Quantidade solicitada: %d. Quantidade disponível: %d"
                                .formatted(itemVendido.itemId(), itemVendido.qtd(), saldoDisponivel));
            }
        }
    }

    public List<MovimentacaoEstoqueResponseDto> pesquisarFiltro(
            String tipo, String status, LocalDate dataEntrega,
            LocalDate dataPrevista, LocalDate dataMovimentacao, Boolean pagadorFrete) {

        tipo = (tipo == null || tipo.isBlank()) ? null : tipo;
        status = (status == null || status.isBlank()) ? null : status;

        LocalDateTime inicioMov = null;
        LocalDateTime fimMov = null;
        if (dataMovimentacao != null) {
            inicioMov = dataMovimentacao.atStartOfDay();
            fimMov = dataMovimentacao.atTime(LocalTime.MAX);
        }

        List<MovimentacaoEstoque> movimentacoes = movimentacaoRepository.pesquisarEFiltrar(
                tipo, status, dataEntrega, dataPrevista, inicioMov, fimMov, pagadorFrete
        );

        return MovimentacaoEstoqueMapper.toResponseDtoList(movimentacoes);
    }

    public List<FornecedorEstatisticasDto> obterEstatisticasFornecedores(Integer itemId) {
        Map<Integer, List<Long>> temposPorFornecedor = new HashMap<>();
        for (Object[] row : movimentacaoRepository.buscarEntregasPorFornecedor(itemId)) {
            Integer fornecedorId = (Integer) row[0];
            LocalDate pedido = ((LocalDateTime) row[1]).toLocalDate();
            LocalDate entrega = (LocalDate) row[2];
            temposPorFornecedor
                    .computeIfAbsent(fornecedorId, k -> new ArrayList<>())
                    .add(ChronoUnit.DAYS.between(pedido, entrega));
        }

        List<FornecedorEstatisticasDto> dtos = new ArrayList<>();
        for (Object[] row : movimentacaoRepository.buscarEstatisticasPorFornecedor(itemId)) {
            Integer fornecedorId = (Integer) row[0];

            String uf = (String) row[2];
            String cidade = (String) row[3];
            String ufCidade = java.util.stream.Stream.of(uf, cidade)
                    .filter(s -> s != null && !s.isBlank())
                    .collect(java.util.stream.Collectors.joining(" - "));

            FornecedorEstatisticasDto dto = new FornecedorEstatisticasDto();
            dto.setFornecedorId(fornecedorId);
            dto.setNomeEmpresa((String) row[1]);
            dto.setUfCidade(ufCidade.isEmpty() ? null : ufCidade);
            dto.setPercentualEntregasNoPrazo(row[4] != null ? ((Number) row[4]).doubleValue() : 0.0);
            dto.setPercentualFretePago(row[5] != null ? ((Number) row[5]).doubleValue() : 0.0);
            dto.setMedianaTempoEntregaDias(mediana(temposPorFornecedor.get(fornecedorId)));
            dtos.add(dto);
        }
        return dtos;
    }

    private Double mediana(List<Long> valores) {
        if (valores == null || valores.isEmpty()) return null;
        List<Long> ordenados = new ArrayList<>(valores);
        Collections.sort(ordenados);
        int meio = ordenados.size() / 2;
        if (ordenados.size() % 2 == 1) return ordenados.get(meio).doubleValue();
        return (ordenados.get(meio - 1) + ordenados.get(meio)) / 2.0;
    }


    public CotacoesPorcentagemDto obterEstatisticasCotacoes (Integer PeriodoId){
        return (movimentacaoRepository.buscarCotacoesPorPeriodo(PeriodoId));

    }

    public List<DashboardMensalDto> obterEstatisticasMensais(Integer ano) {
        int anoConsulta = ano != null ? ano : LocalDate.now().getYear();
        return movimentacaoRepository.obterDadosMensaisPorAno(anoConsulta);
    }

}