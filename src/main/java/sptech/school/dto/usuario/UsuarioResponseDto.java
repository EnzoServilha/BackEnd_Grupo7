package sptech.school.dto.usuario;

import sptech.school.dto.permissao.PermissaoResponseDto;

import java.time.LocalDateTime;

public record UsuarioResponseDto(
        Long id,
        String nome,
        String email,
        LocalDateTime dataCadastro,
        PermissaoResponseDto permissao,
        Boolean ativo,
        Long desativadoPorId
) {
}
