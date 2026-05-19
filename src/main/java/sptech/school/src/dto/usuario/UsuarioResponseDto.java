package sptech.school.src.dto.usuario;

import sptech.school.src.dto.permissao.PermissaoResponseDto;

import java.time.LocalDateTime;

public record UsuarioResponseDto(
        Long id,
        String nome,
        String email,
        LocalDateTime dataCadastro,
        PermissaoResponseDto permissao
) {
}

