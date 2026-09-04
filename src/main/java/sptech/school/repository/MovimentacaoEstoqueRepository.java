package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.entity.MovimentacaoEstoque;

import java.util.List;

public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Integer> {
    List<MovimentacaoEstoque> findByClienteId(Integer clienteId);
    List<MovimentacaoEstoque> findAllByPeriodoId(Integer periodoId);
    List<MovimentacaoEstoque> findAllByPeriodoIdAndTipoNomeAndStatusNome(
            Integer periodoId, String tipoNome, String statusNome);
}
