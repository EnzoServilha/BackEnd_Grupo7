package sptech.school.dto.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record UsuarioRequestDto(
        @NotBlank
        @Size(max = 100)
        String nome,

        @NotBlank
        @Size(max = 100)
        String email,

        @NotBlank
        @Size(max = 255)
        String senha,

        LocalDateTime dataCadastro,

        Integer permissaoId
) {
}

