package sptech.school.dto.codigoAssociado;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CodigoAssociadoRequestDto(
        @NotBlank
        @Size(max = 255)
        String codigo,

        Integer fornecedorId,

        Integer clienteId
) {
}

