package sptech.school.dto.movimentacaoEstoque;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record MovimentacaoEstoqueRequestDto(
        Integer usuarioId,
        BigDecimal totalGastoImpostos,
        BigDecimal precoFrete,
        LocalDateTime dataMovimentacao,
        LocalDate dataEntregaPrevista,
        LocalDate dataEntrega,
        String observacoes,
        Integer tipoId,
        Integer statusId,
        Integer clienteId,
        Integer fornecedorId,
        Integer movimentacaoOriginalId,
        String numeroNotaFiscal
) {
}

