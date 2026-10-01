package sptech.school.mapper;

import sptech.school.dto.categoria.CategoriaRequestDto;
import sptech.school.dto.categoria.CategoriaResponseDto;
import sptech.school.entity.Categoria;

import java.util.List;

public class CategoriaMapper {

    public static Categoria toEntity(CategoriaRequestDto dto) {
        Categoria categoria = new Categoria();
        categoria.setNome(dto.nome());
        return categoria;
    }

    public static CategoriaResponseDto toResponseDto(Categoria categoria) {
        return new CategoriaResponseDto(
                categoria.getId(),
            categoria.getNome(),
            categoria.getAtivo(),
            categoria.getDesativadoPor() != null ? categoria.getDesativadoPor().getId() : null
        );
    }

    public static List<CategoriaResponseDto> toResponseDtoList(List<Categoria> categorias) {
        return categorias.stream().map(CategoriaMapper::toResponseDto).toList();
    }
}

