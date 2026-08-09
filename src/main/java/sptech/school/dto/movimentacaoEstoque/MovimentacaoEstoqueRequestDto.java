package sptech.school.dto.movimentacaoEstoque;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MovimentacaoEstoqueRequestDto(
        @NotNull
        Long usuarioId,
        BigDecimal totalGastoImpostos,
        BigDecimal precoFrete,
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

