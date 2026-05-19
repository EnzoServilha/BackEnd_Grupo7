package sptech.school.src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.src.entity.Cliente;

public interface ClienteRepository  extends JpaRepository<Cliente, Integer> {
}
