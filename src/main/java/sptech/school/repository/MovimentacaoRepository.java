package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sptech.school.entity.MovimentacaoEstoque;

import jakarta.persistence.LockModeType;

import java.time.LocalDate;
import java.time.LocalDateTime;
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


    @Query("SELECT DISTINCT m FROM MovimentacaoEstoque m " +
            "LEFT JOIN m.tipo t " +
            "LEFT JOIN m.status s " +
            "WHERE " +
            "(:tipo IS NULL OR LOWER(t.nome) LIKE LOWER(CONCAT('%', :tipo, '%'))) AND " +
            "(:status IS NULL OR LOWER(s.nome) LIKE LOWER(CONCAT('%', :status, '%'))) AND " +
            "(:dataEntrega IS NULL OR m.dataEntrega = :dataEntrega) AND " +
            "(:dataPrevista IS NULL OR m.dataEntregaPrevista = :dataPrevista) AND " +
            "(:dataMovimentacaoInicio IS NULL OR (m.dataMovimentacao >= :dataMovimentacaoInicio AND m.dataMovimentacao <= :dataMovimentacaoFim)) AND " +
            "(:temFrete IS NULL OR (:temFrete = TRUE AND m.precoFrete > 0) OR (:temFrete = FALSE AND (m.precoFrete IS NULL OR m.precoFrete = 0)))")
    List<MovimentacaoEstoque> pesquisarEFiltrar(
            @Param("tipo") String tipo,
            @Param("status") String status,
            @Param("dataEntrega") LocalDate dataEntrega,
            @Param("dataPrevista") LocalDate dataPrevista,
            @Param("dataMovimentacaoInicio") LocalDateTime dataMovimentacaoInicio,
            @Param("dataMovimentacaoFim") LocalDateTime dataMovimentacaoFim,
            @Param("temFrete") Boolean temFrete
    );
}
