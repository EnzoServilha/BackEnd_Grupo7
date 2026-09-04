package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.entity.Status;

import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status, Integer> {
	Optional<Status> findByNome(String nome);
}

