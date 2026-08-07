package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.entity.Tipo;

import java.util.Optional;

public interface TipoRepository extends JpaRepository<Tipo, Integer> {
	Optional<Tipo> findByNome(String nome);
}

