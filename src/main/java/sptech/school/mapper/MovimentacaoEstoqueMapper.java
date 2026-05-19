package sptech.school.mapper;

import sptech.school.dto.movimentacaoEstoque.MovimentacaoEstoqueRequestDto;
import sptech.school.dto.movimentacaoEstoque.MovimentacaoEstoqueResponseDto;
import sptech.school.entity.*;

import java.time.temporal.ChronoUnit;
import java.util.List;

public class MovimentacaoEstoqueMapper {

    public static MovimentacaoEstoque toEntity(MovimentacaoEstoqueRequestDto dto) {
        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setTotalGastoImpostos(dto.totalGastoImpostos());
        movimentacao.setPrecoFrete(dto.precoFrete());
        movimentacao.setDataEntregaPrevista(dto.dataEntregaPrevista());
        movimentacao.setDataEntrega(dto.dataEntrega());
        movimentacao.setObservacoes(dto.observacoes());
        movimentacao.setNumeroNotaFiscal(dto.numeroNotaFiscal());
        if (dto.usuarioId() != null) {
            Usuario usuario = new Usuario();
            //Estava sendo passado como integer, mas o id do usuário é do tipo Long, então foi necessário converter
            usuario.setId(Long.valueOf(dto.usuarioId()));
            movimentacao.setUsuario(usuario);
        }
        if (dto.tipoId() != null) {
            Tipo tipo = new Tipo();
            tipo.setId(dto.tipoId());
            movimentacao.setTipo(tipo);
        }
        if (dto.statusId() != null) {
            Status status = new Status();
            status.setId(dto.statusId());
            movimentacao.setStatus(status);
        }
        if (dto.clienteId() != null) {
            Cliente cliente = new Cliente();
            cliente.setId(dto.clienteId());
            movimentacao.setCliente(cliente);
        }
        if (dto.fornecedorId() != null) {
            Fornecedor fornecedor = new Fornecedor();
            fornecedor.setId(dto.fornecedorId());
            movimentacao.setFornecedor(fornecedor);
        }
        if (dto.movimentacaoOriginalId() != null) {
            MovimentacaoEstoque original = new MovimentacaoEstoque();
            original.setId(dto.movimentacaoOriginalId());
            movimentacao.setMovimentacaoOriginal(original);
        }
        return movimentacao;
    }

    public static Double valorTotal(Double valorProdutos, MovimentacaoEstoque e){

        Double frete = e.getPrecoFrete();

        if(e.getPrecoFrete() == null){
            frete = 0.0;
        }

        Double impostos = e.getTotalGastoImpostos();

        if(e.getTotalGastoImpostos() == null){
            impostos = 0.0;
        }

        return frete + impostos + valorProdutos;

    }

    public static Double valorProdutos(List<ItensNaMovimentacao> itens){
        if(itens == null || itens.isEmpty()){
            return 0.0;
        }

        Double calculo = 0.0;
        for(ItensNaMovimentacao t : itens){
            calculo += t.getPrecoUnitario() * t.getQtd();
        }
        return calculo;
    }

    public static Integer qtdItens(List<ItensNaMovimentacao> itens){
        if(itens == null || itens.isEmpty()){
            return 0;
        }

        Integer calculo = 0;
        for(ItensNaMovimentacao t : itens){
            calculo += t.getQtd();
        }
        return calculo;
    }

    public static Long qtdDiasPrevistos(MovimentacaoEstoque e){
        if(e.getDataMovimentacao() == null || e.getDataEntregaPrevista() == null || e.getDataEntregaPrevista().isBefore(e.getDataMovimentacao().toLocalDate())){
            return 0L;
        }

        return ChronoUnit.DAYS.between(e.getDataMovimentacao().toLocalDate(), e.getDataEntregaPrevista());
    }

    public static Long qtdDiasReais(MovimentacaoEstoque e){
        if(e.getDataMovimentacao() == null || e.getDataEntrega() == null || e.getDataEntrega().isBefore(e.getDataMovimentacao().toLocalDate())){
            return 0L;
        }

        return ChronoUnit.DAYS.between(e.getDataMovimentacao().toLocalDate(), e.getDataEntrega());
    }

    public static MovimentacaoEstoqueResponseDto toResponse(MovimentacaoEstoque entity){


        Double valorP = valorProdutos(entity.getItens());

        MovimentacaoEstoqueResponseDto response = new MovimentacaoEstoqueResponseDto(
                entity.getId(),
                entity.getUsuario() != null ? UsuarioMapper.toResponseDto(entity.getUsuario()) : null,
                entity.getTotalGastoImpostos(),
                entity.getPrecoFrete(),
                entity.getDataMovimentacao(),
                entity.getDataEntregaPrevista(),
                entity.getDataEntrega(),
                entity.getObservacoes(),
                entity.getTipo() != null ? TipoMapper.toResponseDto(entity.getTipo()) : null,
                entity.getStatus() != null ? StatusMapper.toResponseDto(entity.getStatus()) : null,
                entity.getCliente() != null ? ClienteMapper.toResponseDto(entity.getCliente()) : null,
                entity.getPeriodo() != null ? PeriodoMapper.toResponseDto(entity.getPeriodo()) : null,
                entity.getFornecedor() != null ? FornecedorMapper.toResponseDto(entity.getFornecedor()) : null,
                entity.getMovimentacaoOriginal() != null ? entity.getMovimentacaoOriginal().getId() : null,
                entity.getNumeroNotaFiscal(),
                valorTotal(valorP, entity),
                valorP,
                qtdItens(entity.getItens()),
                qtdDiasPrevistos(entity),
                qtdDiasReais(entity)

        );

        return response;
    }

    public static List<MovimentacaoEstoqueResponseDto> toResponseDtoList(List<MovimentacaoEstoque> lista) {
        return lista.stream().map(MovimentacaoEstoqueMapper::toResponse).toList();
    }
}

