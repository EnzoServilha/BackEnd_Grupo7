package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sptech.school.entity.CodigoAssociado;

import java.util.List;
import java.util.Optional;

public interface CodigoAssociadoRepository extends JpaRepository<CodigoAssociado, Integer> {
    List<CodigoAssociado> findAllByAtivoTrue();
    Optional<CodigoAssociado> findByIdAndAtivoTrue(Integer id);

    @Query("SELECT c FROM CodigoAssociado c " +
            "WHERE c.ativo = true " +
            "AND LOWER(c.codigo) LIKE LOWER(CONCAT('%', :codigo, '%')) ESCAPE '\\'")
    List<CodigoAssociado> findByCodigoContainingIgnoreCase(@Param("codigo") String codigo);

    List<CodigoAssociado> findByCodigoContainingIgnoreCaseAndAtivoTrue(String codigo);

    @Query("SELECT DISTINCT c FROM CodigoAssociado c " +
            "LEFT JOIN c.fornecedor f " +
            "LEFT JOIN c.cliente cl " +
            "WHERE " +
            "(:termo IS NULL OR " +
            "LOWER(f.nomeEmpresa) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
            "LOWER(f.nomeContato) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
            "LOWER(cl.nomeEmpresa) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
            "LOWER(cl.nomeContato) LIKE LOWER(CONCAT('%', :termo, '%')))")
    List<CodigoAssociado> pesquisarEFiltrar(@Param("termo") String termo);
}
