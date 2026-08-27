package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sptech.school.entity.Marca;

public interface MarcaRepository extends JpaRepository<Marca, Integer> {
    @Query("SELECT m FROM Marca m " +
            "WHERE LOWER(m.nomeEmpresa) LIKE LOWER(CONCAT('%', :nomeEmpresa, '%')) ESCAPE '\\'")
    Marca findByNomeEmpresaContaining(@Param("nomeEmpresa") String nomeEmpresa);
}
