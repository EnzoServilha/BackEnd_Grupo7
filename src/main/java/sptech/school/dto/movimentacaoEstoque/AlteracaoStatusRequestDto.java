package sptech.school.dto.movimentacaoEstoque;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AlteracaoStatusRequestDto(
        @NotEmpty(message = "Informe ao menos uma movimentação")
        List<@NotNull Integer> ids,

        @NotNull(message = "A ação é obrigatória")
        AcaoStatus acao
) {}