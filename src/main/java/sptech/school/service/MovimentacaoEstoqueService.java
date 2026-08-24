package sptech.school.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sptech.school.dto.movimentacaoEstoque.FechamentoCotacaoRequestDto;
import sptech.school.dto.movimentacaoEstoque.ItemFechamentoCotacaoRequestDto;
import sptech.school.dto.movimentacaoEstoque.MovimentacaoEstoqueRequestDto;
import sptech.school.dto.movimentacaoEstoque.MovimentacaoEstoqueResponseDto;
import sptech.school.entity.ItensNaMovimentacao;
import sptech.school.entity.MovimentacaoEstoque;
import sptech.school.entity.Status;
import sptech.school.entity.Tipo;
import sptech.school.entity.Usuario;
import sptech.school.exception.EntidadeNaoEncontradaException;
import sptech.school.exception.EntidadeConflitanteException;
import sptech.school.exception.MovimentacaoNaoEncontrada;
import sptech.school.mapper.MovimentacaoEstoqueMapper;
import sptech.school.repository.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<MovimentacaoEstoqueResponseDto> listar(){
        return MovimentacaoEstoqueMapper.toResponseDtoList(movimentacaoRepository.findAll());
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

        movimentacao.setDataMovimentacao(LocalDateTime.now());
        preencher(movimentacao, request, emailUsuario);
        validarCriacao(movimentacao);

        return  MovimentacaoEstoqueMapper.toResponse(movimentacaoRepository.save(movimentacao));
    }

        @Transactional
        public MovimentacaoEstoqueResponseDto fecharCotacao(Integer cotacaoId,
                                 FechamentoCotacaoRequestDto request,
                                 String emailUsuario) {
        MovimentacaoEstoque cotacao = movimentacaoRepository.buscarPorIdComBloqueio(cotacaoId)
            .orElseThrow(() -> new MovimentacaoNaoEncontrada("Cotação não encontrada"));
        validarCotacaoAberta(cotacao);

        Map<Integer, ItensNaMovimentacao> itensCotados = indexarItensCotados(cotacao);
        validarItensVendidos(request.itens(), itensCotados);

        Usuario usuario = usuarioRepository.findByEmailAndAtivoTrue(emailUsuario)
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário ativo não encontrado", emailUsuario));
        Tipo tipoSaida = tipoRepository.findByNome("SAIDA")
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Tipo SAIDA não encontrado", "SAIDA"));
        Status statusConcluido = statusRepository.findByNome("CONCLUIDO")
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Status CONCLUIDO não encontrado", "CONCLUIDO"));

        MovimentacaoEstoque venda = new MovimentacaoEstoque();
        venda.setUsuario(usuario);
        venda.setTipo(tipoSaida);
        venda.setStatus(statusConcluido);
        venda.setCliente(cotacao.getCliente());
        venda.setMovimentacaoOriginal(cotacao);
        venda.setDataMovimentacao(LocalDateTime.now());
        venda.setTotalGastoImpostos(request.totalGastoImpostos());
        venda.setPrecoFrete(request.precoFrete());
        venda.setDataEntregaPrevista(request.dataEntregaPrevista());
        venda.setDataEntrega(request.dataEntrega());
        venda.setObservacoes(request.observacoes());
        venda.setNumeroNotaFiscal(request.numeroNotaFiscal());
        venda.setPeriodo(periodoRepository.findById(request.periodoId())
            .filter(periodo -> Boolean.FALSE.equals(periodo.getFechado()))
            .orElseThrow(() -> new EntidadeConflitanteException("Vendas só podem usar períodos abertos")));
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

        boolean vendaCompleta = itensCotados.size() == request.itens().size()
            && request.itens().stream().allMatch(itemRequest ->
            itensCotados.get(itemRequest.itemId()).getQtd().equals(itemRequest.qtd()));
        String statusFinal = vendaCompleta ? "CONCLUIDO" : "CONCLUIDO PARCIAL";
        cotacao.setStatus(statusRepository.findByNome(statusFinal)
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Status não encontrado", statusFinal)));
        movimentacaoRepository.save(cotacao);

        return MovimentacaoEstoqueMapper.toResponse(venda);
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
        if (movimentacao.getStatus() != null && "CANCELADO".equals(movimentacao.getStatus().getNome())) {
            return;
        }
        validarPendente(movimentacao);
        movimentacao.setStatus(statusRepository.findByNome("CANCELADO")
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Status CANCELADO não encontrado", "CANCELADO")));
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


    public void preencher(MovimentacaoEstoque entidade, MovimentacaoEstoqueRequestDto requestDto, String emailUsuario){
        Usuario usuario = usuarioRepository.findByEmailAndAtivoTrue(emailUsuario)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário ativo não encontrado", emailUsuario));
        entidade.setUsuario(usuario);

        if(requestDto.tipoId() != null){
            entidade.setTipo(tipoRepository.findById(requestDto.tipoId())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Tipo não encontrado", requestDto.tipoId())));
        }

        if(requestDto.statusId() != null){
            entidade.setStatus(statusRepository.findById(requestDto.statusId())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Status não encontrado", requestDto.statusId())));
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
            movimentacao.setStatus(statusRepository.findByNome("PENDENTE")
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Status PENDENTE não encontrado", "PENDENTE")));
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
}
