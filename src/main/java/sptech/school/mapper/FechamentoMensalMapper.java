package sptech.school.mapper;

import sptech.school.dto.fechamentoMensal.FechamentoMensalRequestDto;
import sptech.school.dto.fechamentoMensal.FechamentoMensalResponseDto;
import sptech.school.entity.FechamentoMensal;
import sptech.school.entity.Item;

import java.util.List;

public class FechamentoMensalMapper {

    public static FechamentoMensal toEntity(FechamentoMensalRequestDto dto) {
        FechamentoMensal fechamento = new FechamentoMensal();
        fechamento.setMes(dto.mes());
        fechamento.setAno(dto.ano());
        fechamento.setQtd(dto.qtd());
        if (dto.itemId() != null) {
            Item item = new Item();
            item.setId(dto.itemId());
            fechamento.setItem(item);
        }
        return fechamento;
    }

    public static FechamentoMensalResponseDto toResponseDto(FechamentoMensal fechamento) {
        return new FechamentoMensalResponseDto(
                fechamento.getId(),
                fechamento.getMes(),
                fechamento.getAno(),
                fechamento.getQtd(),
                fechamento.getItem() != null ? ItemMapper.toResponseDto(fechamento.getItem()) : null
        );
    }

    public static List<FechamentoMensalResponseDto> toResponseDtoList(List<FechamentoMensal> fechamentos) {
        return fechamentos.stream().map(FechamentoMensalMapper::toResponseDto).toList();
    }
}

