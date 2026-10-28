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

    @Query("SELECT DISTINCT f FROM Fornecedor f " +
            "LEFT JOIN f.endereco e " +
            "LEFT JOIN f.marcas m " +
            "LEFT JOIN f.categoria c " +
            "WHERE " +
            "(:nomeEmpresa IS NULL OR LOWER(f.nomeEmpresa) LIKE LOWER(CONCAT('%', :nomeEmpresa, '%'))) AND " +
            "(:nomeContato IS NULL OR LOWER(f.nomeContato) LIKE LOWER(CONCAT('%', :nomeContato, '%'))) AND " +
            "(:telefone IS NULL OR f.telefone LIKE CONCAT('%', :telefone, '%')) AND " +
            "(:email IS NULL OR LOWER(f.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
            "(:localizacao IS NULL OR (LOWER(e.cidade) LIKE LOWER(CONCAT('%', :localizacao, '%')) OR LOWER(e.logradouro) LIKE LOWER(CONCAT('%', :localizacao, '%')))) AND " +
            "(:fabricantes IS NULL OR m.nomeEmpresa IN :fabricantes) AND " +
            "(:categorias IS NULL OR c.nome IN :categorias)")
    List<Fornecedor> pesquisarEFiltrar(
            @Param("nomeEmpresa") String nomeEmpresa,
            @Param("nomeContato") String nomeContato,
            @Param("telefone") String telefone,
            @Param("email") String email,
            @Param("localizacao") String localizacao,
            @Param("fabricantes") List<String> fabricantes,
            @Param("categorias") List<String> categorias
    );

    Optional<Fornecedor> findByCnpj(String cnpj);

}

