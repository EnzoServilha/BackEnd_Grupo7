package sptech.school.dto.categoria;

public record CategoriaResponseDto(
        Integer id,
        String nome,
        Boolean ativo,
        Long desativadoPorId
) {
}

