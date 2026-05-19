package sptech.school.mapper;

import sptech.school.dto.periodo.PeriodoResponseDto;
import sptech.school.entity.Periodo;

import java.util.List;

public class PeriodoMapper {

    public static PeriodoResponseDto toResponseDto(Periodo periodo) {
        if (periodo == null) {
            return null;
        }

        PeriodoResponseDto dto = new PeriodoResponseDto();
        dto.setId(periodo.getId());
        dto.setDataCriacao(periodo.getDataCriacao());
        dto.setAnotacao(periodo.getAnotacao());
        dto.setQtdPecas(periodo.getQtdPecas());

        return dto;
    }

    public static List<PeriodoResponseDto> toResponseDtoList(List<Periodo> periodos) {
        if (periodos == null) {
            return null;
        }
        return periodos.stream().map(PeriodoMapper::toResponseDto).toList();
    }
}

