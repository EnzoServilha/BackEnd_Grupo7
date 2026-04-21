package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.entity.ItensNaMovimentacao;

public interface ItemMovimentacaoRepository extends JpaRepository<ItensNaMovimentacao, Integer> {
}
