package sptech.school.dto.codigoAssociado;

import sptech.school.dto.cliente.ClienteResponseDto;
import sptech.school.dto.fornecedor.FornecedorResponseDto;

public record CodigoAssociadoResponseDto(
        Integer id,
        String codigo,
        FornecedorResponseDto fornecedor,
        ClienteResponseDto cliente
) {
}

