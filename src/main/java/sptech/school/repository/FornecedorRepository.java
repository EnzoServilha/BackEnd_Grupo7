package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sptech.school.entity.Fornecedor;

import java.util.List;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Integer> {
    List<Fornecedor> findByNomeContatoContaining(String nomeContato);
    List<Fornecedor> findByNomeEmpresaContaining(String nomeEmpresa);

    @Query("SELECT f FROM Fornecedor f JOIN f.marcas m WHERE m.id = :idMarca")
    List<Fornecedor> findByIdMarca(Integer idMarca);

    @Query("SELECT f FROM Fornecedor f JOIN f.categoria c WHERE c.id = :idCategoria")
    List<Fornecedor> findByIdCategoria(Integer idCategoria);
}
