package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.entity.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository  extends JpaRepository<Cliente, Integer> {
	List<Cliente> findAllByAtivoTrue();
	Optional<Cliente> findByIdAndAtivoTrue(Integer id);
	Optional<Cliente> findByCpfCnpj(String cpfCnpj);
	boolean existsByEnderecoIdAndAtivoTrue(Integer enderecoId);
}
