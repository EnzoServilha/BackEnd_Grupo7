package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.entity.Cliente;

public interface ClienteRepository  extends JpaRepository<Cliente, Integer> {
}
