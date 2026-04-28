package sptech.school.dto.movimentacaoEstoque;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record MovimentacaoEstoqueRequestDto(
        @NotNull
        Long usuarioId,
        Double totalGastoImpostos,
        Double precoFrete,
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

