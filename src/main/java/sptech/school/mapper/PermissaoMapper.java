package sptech.school.mapper;

import sptech.school.dto.permissao.PermissaoRequestDto;
import sptech.school.dto.permissao.PermissaoResponseDto;
import sptech.school.entity.Permissao;

import java.util.List;

public class PermissaoMapper {

    public static Permissao toEntity(PermissaoRequestDto dto) {
        Permissao permissao = new Permissao();
        permissao.setNome(dto.nome());
        return permissao;
    }

    public static PermissaoResponseDto toResponseDto(Permissao permissao) {
        return new PermissaoResponseDto(
                permissao.getId(),
                permissao.getNome()
        );
    }

    public static List<PermissaoResponseDto> toResponseDtoList(List<Permissao> permissoes) {
        return permissoes.stream().map(PermissaoMapper::toResponseDto).toList();
    }
}

