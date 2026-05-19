package sptech.school.dto.categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaRequestDto(
        @NotBlank
        @Size(max = 100)
        String nome
) {
}

