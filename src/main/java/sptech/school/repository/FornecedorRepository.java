package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sptech.school.entity.Fornecedor;

import java.util.List;
import java.util.Optional;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Integer> {
    List<Fornecedor> findAllByAtivoTrue();
    Optional<Fornecedor> findByIdAndAtivoTrue(Integer id);

    @Query("SELECT f FROM Fornecedor f " +
            "WHERE f.ativo = true " +
            "AND LOWER(f.nomeContato) LIKE LOWER(CONCAT('%', :nomeContato, '%')) ESCAPE '\\'")
    List<Fornecedor> findByNomeContatoContaining(@Param("nomeContato") String nomeContato);

    @Query("SELECT f FROM Fornecedor f " +
            "WHERE f.ativo = true " +
            "AND LOWER(f.nomeEmpresa) LIKE LOWER(CONCAT('%', :nomeEmpresa, '%')) ESCAPE '\\'")
    List<Fornecedor> findByNomeEmpresaContaining(@Param("nomeEmpresa") String nomeEmpresa);

    List<Fornecedor> findByNomeContatoContainingAndAtivoTrue(String nomeContato);
    List<Fornecedor> findByNomeEmpresaContainingAndAtivoTrue(String nomeEmpresa);
    boolean existsByEnderecoIdAndAtivoTrue(Integer enderecoId);

    @Query("SELECT f FROM Fornecedor f JOIN f.marcas m WHERE f.ativo = true AND m.ativo = true AND m.id = :idMarca")
    List<Fornecedor> findByIdMarca(Integer idMarca);

    @Query("SELECT f FROM Fornecedor f JOIN f.categoria c WHERE f.ativo = true AND c.ativo = true AND c.id = :idCategoria")
    List<Fornecedor> findByIdCategoria(Integer idCategoria);
}
