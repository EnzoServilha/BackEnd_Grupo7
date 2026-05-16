package sptech.school.src.dto.movimentacaoEstoque;

import sptech.school.src.dto.cliente.ClienteResponseDto;
import sptech.school.src.dto.fornecedor.FornecedorResponseDto;
import sptech.school.src.dto.periodo.PeriodoResponseDto;
import sptech.school.src.dto.status.StatusResponseDto;
import sptech.school.src.dto.tipo.TipoResponseDto;
import sptech.school.src.dto.usuario.UsuarioResponseDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record MovimentacaoEstoqueResponseDto(
        Integer id,
        UsuarioResponseDto usuario,
        Double totalGastoImpostos,
        Double precoFrete,
        LocalDateTime dataMovimentacao,
        LocalDate dataEntregaPrevista,
        LocalDate dataEntrega,
        String observacoes,
        TipoResponseDto tipo,
        StatusResponseDto status,
        ClienteResponseDto cliente,
        PeriodoResponseDto periodo,
        FornecedorResponseDto fornecedor,
        Integer movimentacaoOriginalId,
        String numeroNotaFiscal,
        Double valorTotal,
        Double precoProdutos,
        Integer qtdItens,
        Long qtdDiasPrevistos,
        Long qtdDiasReal
) {
}

