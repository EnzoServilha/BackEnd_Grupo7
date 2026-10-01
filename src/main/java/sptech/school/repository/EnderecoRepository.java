package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.entity.*;

import java.util.List;
import java.util.Optional;

public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {
	List<Endereco> findAllByAtivoTrue();
	Optional<Endereco> findByIdAndAtivoTrue(Integer id);
}
