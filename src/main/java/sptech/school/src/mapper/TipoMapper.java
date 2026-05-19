package sptech.school.src.mapper;

import sptech.school.src.dto.tipo.TipoRequestDto;
import sptech.school.src.dto.tipo.TipoResponseDto;
import sptech.school.src.entity.Tipo;

import java.util.List;

public class TipoMapper {

    public static Tipo toEntity(TipoRequestDto dto) {
        Tipo tipo = new Tipo();
        tipo.setNome(dto.nome());
        return tipo;
    }

    public static TipoResponseDto toResponseDto(Tipo tipo) {
        return new TipoResponseDto(
                tipo.getId(),
                tipo.getNome()
        );
    }

    public static List<TipoResponseDto> toResponseDtoList(List<Tipo> tipos) {
        return tipos.stream().map(TipoMapper::toResponseDto).toList();
    }
}

