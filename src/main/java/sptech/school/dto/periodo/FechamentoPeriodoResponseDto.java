package sptech.school.dto.periodo;

import java.util.List;

public record FechamentoPeriodoResponseDto(
        PeriodoResponseDto periodoFechado,
        PeriodoResponseDto novoPeriodo,
        Integer movimentacaoAjusteId,
        List<PeriodoQtdPecasDTO> saldosTransferidos
) {
}