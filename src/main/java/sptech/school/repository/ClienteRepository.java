package sptech.school.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.entity.Cliente;


import java.util.List;
import java.util.Optional;

public interface ClienteRepository  extends JpaRepository<Cliente, Integer> {
    Page<Cliente> findAllByAtivoTrue(Pageable pageable);
	Optional<Cliente> findByIdAndAtivoTrue(Integer id);
	Optional<Cliente> findByCpfCnpj(String cpfCnpj);
	boolean existsByEnderecoIdAndAtivoTrue(Integer enderecoId);
}
