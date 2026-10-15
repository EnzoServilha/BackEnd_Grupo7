package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sptech.school.entity.MovimentacaoEstoque;

import jakarta.persistence.LockModeType;
import sptech.school.entity.Pageble;

import java.math.BigDecimal;
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

    // Paginação:

    @Query("SELECT COUNT(m) FROM MovimentacaoEstoque m WHERE m.periodo.id = :idPeriodo")
    long contarPorPeriodo(@Param("idPeriodo") Integer idPeriodo);

    @Query("SELECT m FROM MovimentacaoEstoque m WHERE m.periodo.id = :idPeriodo ORDER BY m.id ASC")
    List<MovimentacaoEstoque> listarComPaginacaoNativa(
            @Param("idPeriodo") Integer idPeriodo,
            @Param("limit") int limit,
            @Param("offset") int offset);


    @Query("SELECT DISTINCT m FROM MovimentacaoEstoque m " +
            "LEFT JOIN m.tipo t " +
            "LEFT JOIN m.status s " +
            "WHERE " +
            "(:tipo IS NULL OR LOWER(t.nome) LIKE LOWER(CONCAT('%', :tipo, '%'))) AND " +
            "(:status IS NULL OR LOWER(s.nome) LIKE LOWER(CONCAT('%', :status, '%'))) AND " +
            "(:dataEntrega IS NULL OR m.dataEntrega = :dataEntrega) AND " +
            "(:dataPrevista IS NULL OR m.dataEntregaPrevista = :dataPrevista) AND " +
            "(:dataMovimentacaoInicio IS NULL OR (m.dataMovimentacao >= :dataMovimentacaoInicio AND m.dataMovimentacao <= :dataMovimentacaoFim)) AND " +
            "(:pagadorFrete IS NULL OR (:pagadorFrete = TRUE AND m.precoFrete > 0) OR (:pagadorFrete = FALSE AND (m.precoFrete IS NULL OR m.precoFrete = 0)))")
    List<MovimentacaoEstoque> pesquisarEFiltrar(
            @Param("tipo") String tipo,
            @Param("status") String status,
            @Param("dataEntrega") LocalDate dataEntrega,
            @Param("dataPrevista") LocalDate dataPrevista,
            @Param("dataMovimentacaoInicio") LocalDateTime dataMovimentacaoInicio,
            @Param("dataMovimentacaoFim") LocalDateTime dataMovimentacaoFim,
            @Param("pagadorFrete") Boolean pagadorFrete
    );

    @Query("SELECT m.fornecedor.id, m.fornecedor.nomeEmpresa, " +
            "SUM(CASE WHEN m.dataEntrega <= m.dataEntregaPrevista THEN 1 ELSE 0 END) * 100.0 / COUNT(m), " +
            "SUM(CASE WHEN m.precoFrete > 0 THEN 1 ELSE 0 END) * 100.0 / COUNT(m) " +
            "FROM ItensNaMovimentacao i JOIN i.movimentacaoEstoque m " +
            "WHERE i.item.id = :itemId AND m.fornecedor IS NOT NULL " +
            "GROUP BY m.fornecedor.id, m.fornecedor.nomeEmpresa")
    List<Object[]> buscarEstatisticasPorFornecedor(@Param("itemId") Integer itemId);
}
