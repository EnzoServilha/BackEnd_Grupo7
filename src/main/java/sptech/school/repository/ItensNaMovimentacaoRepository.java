package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.entity.ItensNaMovimentacao;
import sptech.school.entity.MovimentacaoEstoque;

public interface ItensNaMovimentacaoRepository extends JpaRepository<ItensNaMovimentacao, Integer> {
}
