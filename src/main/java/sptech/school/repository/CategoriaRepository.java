package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sptech.school.entity.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria,Integer> {

    List<Categoria> findAllByAtivoTrue();
    Optional<Categoria> findByIdAndAtivoTrue(Integer id);

    @Query("SELECT c FROM Categoria c " +
            "WHERE c.ativo = true " +
            "AND LOWER(c.nome) LIKE LOWER(CONCAT('%', :nome, '%')) ESCAPE '\\'")
    Categoria findByNomeContaining(@Param("nome") String nome);

    Categoria findByNomeContainingAndAtivoTrue(String nome);
}
