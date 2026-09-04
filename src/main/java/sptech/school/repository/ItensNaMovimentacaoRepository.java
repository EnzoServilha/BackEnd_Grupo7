package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sptech.school.entity.ItensNaMovimentacao;
import sptech.school.entity.MovimentacaoEstoque;

import java.util.List;

public interface ItensNaMovimentacaoRepository extends JpaRepository<ItensNaMovimentacao, Integer> {
    List<ItensNaMovimentacao> findAllByItemId(Integer itemId);
    List<ItensNaMovimentacao> findAllByMovimentacaoEstoqueId(Integer movimentacaoEstoqueId);
    boolean existsByMovimentacaoEstoqueIdAndItemId(Integer movimentacaoEstoqueId, Integer itemId);
    void deleteAllByMovimentacaoEstoqueId(Integer movimentacaoEstoqueId);

    List<ItensNaMovimentacao> findByMovimentacaoEstoque_Periodo_IdAndMovimentacaoEstoque_Tipo_NomeIn(
            Integer periodoId, List<String> nomesTipo);
}
