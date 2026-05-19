package sptech.school.mapper;

import sptech.school.dto.marca.MarcaRequestDto;
import sptech.school.dto.marca.MarcaResponseDto;
import sptech.school.entity.Marca;

import java.util.List;

public class MarcaMapper {

    public static Marca toEntity(MarcaRequestDto dto) {
        Marca marca = new Marca();

        marca.setNomeEmpresa(dto.getNome());

        return marca;
    }

    public static MarcaResponseDto toResponseDto(Marca marca) {
        return new MarcaResponseDto(marca.getId(), marca.getNomeEmpresa());
    }

    public static List<MarcaResponseDto> toResponseDtoList(List<Marca> fabricantes) {
        return fabricantes.stream().map(MarcaMapper::toResponseDto).toList();
    }
}

