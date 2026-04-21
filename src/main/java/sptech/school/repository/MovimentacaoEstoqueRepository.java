package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.entity.MovimentacaoEstoque;
import sptech.school.entity.Periodo;

import java.util.List;

public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Integer> {
    List<MovimentacaoEstoque> findByClienteId(Integer clienteId);
}
