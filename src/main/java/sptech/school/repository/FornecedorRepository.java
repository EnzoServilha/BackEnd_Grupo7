package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.entity.Fornecedor;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Integer> {
}
