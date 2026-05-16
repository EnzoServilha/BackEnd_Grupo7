package sptech.school.src.mapper;

import sptech.school.src.dto.status.StatusRequestDto;
import sptech.school.src.dto.status.StatusResponseDto;
import sptech.school.src.entity.Status;

import java.util.List;

public class StatusMapper {

    public static Status toEntity(StatusRequestDto dto) {
        Status status = new Status();
        status.setNome(dto.nome());
        return status;
    }

    public static StatusResponseDto toResponseDto(Status status) {
        return new StatusResponseDto(
                status.getId(),
                status.getNome()
        );
    }

    public static List<StatusResponseDto> toResponseDtoList(List<Status> statuses) {
        return statuses.stream().map(StatusMapper::toResponseDto).toList();
    }
}

