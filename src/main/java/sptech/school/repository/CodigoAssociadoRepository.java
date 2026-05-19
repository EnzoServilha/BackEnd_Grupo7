package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.entity.CodigoAssociado;

import java.util.List;

public interface CodigoAssociadoRepository extends JpaRepository<CodigoAssociado, Integer> {
    List<CodigoAssociado> findByCodigoContainingIgnoreCase(String codigo);
}
