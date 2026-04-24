package sptech.school.dto.marca;

import sptech.school.dto.endereco.EnderecoResponseDto;

import java.time.LocalDateTime;

public record MarcaResponseDto(
        Integer id,
        String nome
) {
}

