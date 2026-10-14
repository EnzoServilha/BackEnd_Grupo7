package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sptech.school.entity.MovimentacaoEstoque;

import jakarta.persistence.LockModeType;
import sptech.school.entity.Pageble;

import java.util.List;
import java.util.Optional;

public interface MovimentacaoRepository extends JpaRepository<MovimentacaoEstoque, Integer> {
    @Query("SELECT m FROM MovimentacaoEstoque m WHERE m.tipo.nome = :tipo")
    List<MovimentacaoEstoque> buscarPorTipo(String tipo);

    @Query("SELECT m FROM MovimentacaoEstoque m WHERE m.status.nome = :status")
    List<MovimentacaoEstoque> buscarPorStatus(String status);

    @Query("SELECT m FROM MovimentacaoEstoque m WHERE m.periodo.id = :idUltimoPeriodo")
    List<MovimentacaoEstoque> listarPorPeriodoAtual(Integer idUltimoPeriodo);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT m FROM MovimentacaoEstoque m WHERE m.id = :id")
    Optional<MovimentacaoEstoque> buscarPorIdComBloqueio(@Param("id") Integer id);

    // Paginação:

    @Query("SELECT COUNT(m) FROM MovimentacaoEstoque m WHERE m.periodo.id = :idPeriodo")
    long contarPorPeriodo(@Param("idPeriodo") Integer idPeriodo);

    @Query("SELECT m FROM MovimentacaoEstoque m WHERE m.periodo.id = :idPeriodo ORDER BY m.id ASC")
    List<MovimentacaoEstoque> listarComPaginacaoNativa(
            @Param("idPeriodo") Integer idPeriodo,
            @Param("limit") int limit,
            @Param("offset") int offset);
}
