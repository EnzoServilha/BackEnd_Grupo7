package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.entity.CodigoAssociado;

import java.util.List;
import java.util.Optional;

public interface CodigoAssociadoRepository extends JpaRepository<CodigoAssociado, Integer> {
    List<CodigoAssociado> findAllByAtivoTrue();
    Optional<CodigoAssociado> findByIdAndAtivoTrue(Integer id);
    List<CodigoAssociado> findByCodigoContainingIgnoreCase(String codigo);
    List<CodigoAssociado> findByCodigoContainingIgnoreCaseAndAtivoTrue(String codigo);
}
