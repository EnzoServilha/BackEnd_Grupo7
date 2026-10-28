package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sptech.school.entity.MovimentacaoEstoque;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Integer> {
    List<MovimentacaoEstoque> findByClienteId(Integer clienteId);
    List<MovimentacaoEstoque> findAllByPeriodoId(Integer periodoId);
    List<MovimentacaoEstoque> findAllByPeriodoIdAndTipoNomeAndStatusNome(
            Integer periodoId, String tipoNome, String statusNome);
    List<MovimentacaoEstoque> findAllByPeriodoIdAndStatusNome(Integer periodoId, String statusNome);
}
