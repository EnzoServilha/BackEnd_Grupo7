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

        return codigoAssociado;
    }

    public static CodigoAssociadoResponseDto toResponseDto(CodigoAssociado codigoAssociado) {
        CodigoAssociadoResponseDto.FornecedorResumo fornecedorResumo = null;
        if (codigoAssociado.getFornecedor() != null) {
            Fornecedor fornecedor = codigoAssociado.getFornecedor();
            fornecedorResumo = new CodigoAssociadoResponseDto.FornecedorResumo(fornecedor.getId(), fornecedor.getRazaoSocial(), fornecedor.getEmail());
        }
        CodigoAssociadoResponseDto.ClienteResumo clienteResumo = null;
        if (codigoAssociado.getCliente() != null) {
            Cliente cliente = codigoAssociado.getCliente();
            clienteResumo = new CodigoAssociadoResponseDto.ClienteResumo(cliente.getId(), cliente.getNomeContato(), cliente.getEmail());
        }
        return new CodigoAssociadoResponseDto(
                codigoAssociado.getId(),
                codigoAssociado.getCodigo(),
                fornecedorResumo,
                clienteResumo,
                codigoAssociado.getAtivo(),
                codigoAssociado.getDesativadoPor() != null ? codigoAssociado.getDesativadoPor().getId() : null
        );
    }

    public static List<CodigoAssociadoResponseDto> toResponseDtoList(List<CodigoAssociado> codigos) {
        return codigos.stream().map(CodigoAssociadoMapper::toResponseDto).toList();
    }
}
