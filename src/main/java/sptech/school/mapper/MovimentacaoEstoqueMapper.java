package sptech.school.mapper;

import sptech.school.dto.movimentacaoEstoque.MovimentacaoEstoqueRequestDto;
import sptech.school.dto.movimentacaoEstoque.MovimentacaoEstoqueResponseDto;
import sptech.school.entity.*;

import java.util.List;

public class MovimentacaoEstoqueMapper {

    public static MovimentacaoEstoque toEntity(MovimentacaoEstoqueRequestDto dto) {
        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setTotalGastoImpostos(dto.totalGastoImpostos());
        movimentacao.setPrecoFrete(dto.precoFrete());
        movimentacao.setDataMovimentacao(dto.dataMovimentacao());
        movimentacao.setDataEntregaPrevista(dto.dataEntregaPrevista());
        movimentacao.setDataEntrega(dto.dataEntrega());
        movimentacao.setObservacoes(dto.observacoes());
        movimentacao.setNumeroNotaFiscal(dto.numeroNotaFiscal());
        if (dto.usuarioId() != null) {
            Usuario usuario = new Usuario();
            usuario.setId(dto.usuarioId());
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

    public static MovimentacaoEstoqueResponseDto toResponseDto(MovimentacaoEstoque movimentacao) {
        return new MovimentacaoEstoqueResponseDto(
                movimentacao.getId(),
                movimentacao.getUsuario() != null ? UsuarioMapper.toResponseDto(movimentacao.getUsuario()) : null,
                movimentacao.getTotalGastoImpostos(),
                movimentacao.getPrecoFrete(),
                movimentacao.getDataMovimentacao(),
                movimentacao.getDataEntregaPrevista(),
                movimentacao.getDataEntrega(),
                movimentacao.getObservacoes(),
                movimentacao.getTipo() != null ? TipoMapper.toResponseDto(movimentacao.getTipo()) : null,
                movimentacao.getStatus() != null ? StatusMapper.toResponseDto(movimentacao.getStatus()) : null,
                movimentacao.getCliente() != null ? ClienteMapper.toResponseDto(movimentacao.getCliente()) : null,
                movimentacao.getFornecedor() != null ? FornecedorMapper.toResponseDto(movimentacao.getFornecedor()) : null,
                movimentacao.getMovimentacaoOriginal() != null ? movimentacao.getMovimentacaoOriginal().getId() : null,
                movimentacao.getNumeroNotaFiscal()
        );
    }

    public static List<MovimentacaoEstoqueResponseDto> toResponseDtoList(List<MovimentacaoEstoque> movimentacoes) {
        return movimentacoes.stream().map(MovimentacaoEstoqueMapper::toResponseDto).toList();
    }
}

