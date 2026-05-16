package sptech.school.src.dto.fechamentoMensal;

import jakarta.validation.constraints.NotNull;

public record FechamentoMensalRequestDto(
        @NotNull
        Integer mes,

        @NotNull
        Integer ano,

        Integer qtd,

        Integer itemId
) {
}

