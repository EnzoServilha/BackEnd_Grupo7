package sptech.school.dto.usuario;

import sptech.school.dto.permissao.PermissaoResponseDto;

import java.time.LocalDateTime;

public record UsuarioResponseDto(
        Integer id,
        String nome,
        String email,
        LocalDateTime dataCadastro,
        PermissaoResponseDto permissao
) {
}

