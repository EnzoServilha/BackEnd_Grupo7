package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.entity.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria,Integer> {

    List<Categoria> findAllByAtivoTrue();
    Optional<Categoria> findByIdAndAtivoTrue(Integer id);
    Categoria findByNomeContaining(String nome);
    Categoria findByNomeContainingAndAtivoTrue(String nome);
}
