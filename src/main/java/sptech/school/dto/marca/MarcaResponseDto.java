package sptech.school.dto.marca;

public record MarcaResponseDto(
        Integer id,
        String nome,
        Boolean ativo,
        Long desativadoPorId
) {
}

