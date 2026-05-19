package sptech.school.src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.src.entity.Marca;

public interface MarcaRepository extends JpaRepository<Marca, Integer> {
    Marca findByNomeEmpresaContaining(String nomeContato);
}
