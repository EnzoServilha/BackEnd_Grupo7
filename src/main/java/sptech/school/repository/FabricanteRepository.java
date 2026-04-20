package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.entity.Fabricante;
import sptech.school.entity.Fornecedor;

public interface FabricanteRepository extends JpaRepository<Fabricante, Integer> {
    Fabricante findByNomeContatoContaining(String nomeContato);
}
