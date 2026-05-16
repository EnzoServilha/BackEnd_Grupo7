package sptech.school.src.dto.codigoAssociado;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CodigoAssociadoRequestDto(
        @NotBlank
        @Size(max = 100)
        String codigo,

        Integer fornecedorId,

        Integer clienteId
) {
}

