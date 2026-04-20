package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.entity.Categoria;
import sptech.school.entity.Fornecedor;

import java.util.List;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Integer> {
    Fornecedor findByNomeContatoContaining(String nomeContato);
    Fornecedor findByNomeEmpresaContaining(String nomeEmpresa);
    List<Fornecedor> findAllByCategoria(Categoria categoria);
}
