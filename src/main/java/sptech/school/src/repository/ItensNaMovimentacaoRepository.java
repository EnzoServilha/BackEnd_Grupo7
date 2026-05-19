package sptech.school.src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.src.entity.ItensNaMovimentacao;

import java.util.List;

public interface ItensNaMovimentacaoRepository extends JpaRepository<ItensNaMovimentacao, Integer> {
    List<ItensNaMovimentacao> findAllByItemId(Integer itemId);
    List<ItensNaMovimentacao> findAllByMovimentacaoEstoqueId(Integer movimentacaoEstoqueId);
}
