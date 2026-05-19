package sptech.school.dto.permissao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PermissaoRequestDto(
        @NotBlank
        @Size(max = 45)
        String nome
) {
}

