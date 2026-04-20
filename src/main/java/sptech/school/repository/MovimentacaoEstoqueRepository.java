package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.entity.MovimentacaoEstoque;
import sptech.school.entity.Periodo;

public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Integer> {
}
