package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.entity.Marca;

import java.util.List;
import java.util.Optional;

public interface MarcaRepository extends JpaRepository<Marca, Integer> {
    List<Marca> findAllByAtivoTrue();
    Optional<Marca> findByIdAndAtivoTrue(Integer id);
    Marca findByNomeEmpresaContaining(String nomeContato);
    Marca findByNomeEmpresaContainingAndAtivoTrue(String nomeContato);
}
