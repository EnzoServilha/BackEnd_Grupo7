package sptech.school.dto.movimentacaoEstoque;

import sptech.school.dto.cliente.ClienteResponseDto;
import sptech.school.dto.fornecedor.FornecedorResponseDto;
import sptech.school.dto.periodo.PeriodoResponseDto;
import sptech.school.dto.status.StatusResponseDto;
import sptech.school.dto.tipo.TipoResponseDto;
import sptech.school.dto.usuario.UsuarioResponseDto;

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

