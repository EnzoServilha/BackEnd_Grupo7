package sptech.school.dto.movimentacaoEstoque;

import java.util.List;

public record MovimentacaoEstoquePaginacaoResponseDto(
        List<MovimentacaoEstoqueResponseDto> conteudo,
        int offset,
        int limit,
        long totalItens
) {
}
