package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sptech.school.entity.Marca;

import java.util.List;
import java.util.Optional;

public interface MarcaRepository extends JpaRepository<Marca, Integer> {
    List<Marca> findAllByAtivoTrue();
    Optional<Marca> findByIdAndAtivoTrue(Integer id);

    @Query("SELECT m FROM Marca m " +
            "WHERE m.ativo = true " +
            "AND LOWER(m.nomeEmpresa) LIKE LOWER(CONCAT('%', :nomeEmpresa, '%')) ESCAPE '\\'")
    Marca findByNomeEmpresaContaining(@Param("nomeEmpresa") String nomeEmpresa);

    Marca findByNomeEmpresaContainingAndAtivoTrue(String nomeContato);
}
