package sptech.school.mapper;

import sptech.school.dto.status.StatusRequestDto;
import sptech.school.dto.status.StatusResponseDto;
import sptech.school.entity.Status;

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

