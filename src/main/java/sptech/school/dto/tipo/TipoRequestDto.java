package sptech.school.dto.tipo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TipoRequestDto(
        @NotBlank
        @Size(max = 45)
        String nome
) {
}

