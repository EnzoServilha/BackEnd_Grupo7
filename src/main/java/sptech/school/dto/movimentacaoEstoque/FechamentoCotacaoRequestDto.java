package sptech.school.dto.movimentacaoEstoque;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record FechamentoCotacaoRequestDto(
        @NotEmpty List<@Valid ItemFechamentoCotacaoRequestDto> itens,
        @PositiveOrZero BigDecimal totalGastoImpostos,
        @PositiveOrZero BigDecimal precoFrete,
        LocalDate dataEntregaPrevista,
        LocalDate dataEntrega,
        String observacoes,
        String numeroNotaFiscal,
        @NotNull Integer periodoId
) {
}