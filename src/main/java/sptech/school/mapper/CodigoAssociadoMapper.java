package sptech.school.mapper;

import sptech.school.dto.codigoAssociado.CodigoAssociadoRequestDto;
import sptech.school.dto.codigoAssociado.CodigoAssociadoResponseDto;
import sptech.school.entity.Cliente;
import sptech.school.entity.CodigoAssociado;
import sptech.school.entity.Fornecedor;

import java.util.List;

public class CodigoAssociadoMapper {

    public static CodigoAssociado toEntity(CodigoAssociadoRequestDto dto) {
        CodigoAssociado codigoAssociado = new CodigoAssociado();
        codigoAssociado.setCodigo(dto.codigo());
        if (dto.fornecedorId() != null) {
            Fornecedor fornecedor = new Fornecedor();
            fornecedor.setId(dto.fornecedorId());
            codigoAssociado.setFornecedor(fornecedor);
        }
        if (dto.clienteId() != null) {
            Cliente cliente = new Cliente();
            cliente.setId(dto.clienteId());
            codigoAssociado.setCliente(cliente);
        }
        return codigoAssociado;
    }

    public static CodigoAssociadoResponseDto toResponseDto(CodigoAssociado codigoAssociado) {
        return new CodigoAssociadoResponseDto(
                codigoAssociado.getId(),
                codigoAssociado.getCodigo(),
                codigoAssociado.getFornecedor() != null ? FornecedorMapper.toResponseDto(codigoAssociado.getFornecedor()) : null,
                codigoAssociado.getCliente() != null ? ClienteMapper.toResponseDto(codigoAssociado.getCliente()) : null
        );
    }

    public static List<CodigoAssociadoResponseDto> toResponseDtoList(List<CodigoAssociado> codigos) {
        return codigos.stream().map(CodigoAssociadoMapper::toResponseDto).toList();
    }
}

