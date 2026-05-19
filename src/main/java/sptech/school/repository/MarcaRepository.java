package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.entity.Marca;

public interface MarcaRepository extends JpaRepository<Marca, Integer> {
    Marca findByNomeEmpresaContaining(String nomeContato);
}
