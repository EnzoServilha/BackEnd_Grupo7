package sptech.school.dto.movimentacaoEstoque;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MovimentacaoEstoqueRequestDto(
        @PositiveOrZero BigDecimal totalGastoImpostos,
        @PositiveOrZero BigDecimal precoFrete,
        LocalDate dataEntregaPrevista,
        LocalDate dataEntrega,
        String observacoes,
        Integer tipoId,
        Integer statusId,
        Integer clienteId,
        Integer fornecedorId,
        Integer movimentacaoOriginalId,
        String numeroNotaFiscal,

        @NotNull
        Integer periodoId
) {
}

