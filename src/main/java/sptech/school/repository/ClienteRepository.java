package sptech.school.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sptech.school.entity.Cliente;
import sptech.school.entity.Fornecedor;


import java.util.List;
import java.util.Optional;

public interface ClienteRepository  extends JpaRepository<Cliente, Integer> {
    Page<Cliente> findAllByAtivoTrue(Pageable pageable);
	Optional<Cliente> findByIdAndAtivoTrue(Integer id);
	Optional<Cliente> findByCpfCnpj(String cpfCnpj);
	boolean existsByEnderecoIdAndAtivoTrue(Integer enderecoId);


    @Query("SELECT DISTINCT c FROM Cliente c " +
            "LEFT JOIN c.endereco e " +
            "WHERE " +
            "(:nomeEmpresa IS NULL OR LOWER(c.nomeEmpresa) LIKE LOWER(CONCAT('%', :nomeEmpresa, '%'))) AND " +
            "(:nomeContato IS NULL OR LOWER(c.nomeContato) LIKE LOWER(CONCAT('%', :nomeContato, '%'))) AND " +
            "(:telefone IS NULL OR c.telefone LIKE CONCAT('%', :telefone, '%')) AND " +
            "(:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
            "(:localizacao IS NULL OR (LOWER(e.cidade) LIKE LOWER(CONCAT('%', :localizacao, '%')) OR LOWER(e.logradouro) LIKE LOWER(CONCAT('%', :localizacao, '%'))))")

    List<Cliente> pesquisarEFiltrar(
            @Param("nomeEmpresa") String nomeEmpresa,
            @Param("nomeContato") String nomeContato,
            @Param("telefone") String telefone,
            @Param("email") String email,
            @Param("localizacao") String localizacao
    );
}
