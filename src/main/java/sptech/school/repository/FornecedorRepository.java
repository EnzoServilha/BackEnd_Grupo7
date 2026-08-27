package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sptech.school.entity.Fornecedor;

import java.util.List;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Integer> {
    @Query("SELECT f FROM Fornecedor f " +
            "WHERE LOWER(f.nomeContato) LIKE LOWER(CONCAT('%', :nomeContato, '%')) ESCAPE '\\'")
    List<Fornecedor> findByNomeContatoContaining(@Param("nomeContato") String nomeContato);

    @Query("SELECT f FROM Fornecedor f " +
            "WHERE LOWER(f.nomeEmpresa) LIKE LOWER(CONCAT('%', :nomeEmpresa, '%')) ESCAPE '\\'")
    List<Fornecedor> findByNomeEmpresaContaining(@Param("nomeEmpresa") String nomeEmpresa);

    @Query("SELECT f FROM Fornecedor f JOIN f.marcas m WHERE m.id = :idMarca")
    List<Fornecedor> findByIdMarca(Integer idMarca);

    @Query("SELECT f FROM Fornecedor f JOIN f.categoria c WHERE c.id = :idCategoria")
    List<Fornecedor> findByIdCategoria(Integer idCategoria);
}
