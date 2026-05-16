package sptech.school.src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.src.entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria,Integer> {

    Categoria findByNomeContaining(String nome);
}
