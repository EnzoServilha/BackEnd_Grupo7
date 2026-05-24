package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.entity.Categoria;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria,Integer> {

    Categoria findByNomeContaining(String nome);
}
