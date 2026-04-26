package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.entity.ItensNaMovimentacao;
import sptech.school.entity.MovimentacaoEstoque;

import java.util.List;

public interface ItensNaMovimentacaoRepository extends JpaRepository<ItensNaMovimentacao, Integer> {
    List<ItensNaMovimentacao> findAllByItemId(Integer itemId);
    List<ItensNaMovimentacao> findAllByMovimentacaoEstoqueId(Integer movimentacaoEstoqueId);
}
