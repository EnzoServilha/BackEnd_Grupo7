package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sptech.school.entity.Categoria;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria,Integer> {

    @Query("SELECT c FROM Categoria c " +
            "WHERE LOWER(c.nome) LIKE LOWER(CONCAT('%', :nome, '%')) ESCAPE '\\'")
    Categoria findByNomeContaining(@Param("nome") String nome);
}
