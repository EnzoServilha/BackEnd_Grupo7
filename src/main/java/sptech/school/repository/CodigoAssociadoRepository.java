package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sptech.school.entity.CodigoAssociado;

import java.util.List;

public interface CodigoAssociadoRepository extends JpaRepository<CodigoAssociado, Integer> {
    @Query("SELECT c FROM CodigoAssociado c " +
            "WHERE LOWER(c.codigo) LIKE LOWER(CONCAT('%', :codigo, '%')) ESCAPE '\\'")
    List<CodigoAssociado> findByCodigoContainingIgnoreCase(@Param("codigo") String codigo);
}
