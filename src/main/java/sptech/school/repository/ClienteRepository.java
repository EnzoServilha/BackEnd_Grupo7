package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sptech.school.entity.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository  extends JpaRepository<Cliente, Integer> {

    // Contar clientes
    long count();

    // Listar movimentações no estoque por cliente
    List<Cliente> findByNomeEmpresaContainingIgnoreCaseOrNomeContatoContainingIgnoreCase(
            String nomeEmpresa,
            String nomeContato
    );

    // Buscar cliente por nome da empresa OU contato
    Optional<Cliente> findByCpfCnpj(String cpfCnpj);

    // Buscar cliente por CPF/CNPJ
    @Query("""
    SELECT c FROM Cliente c
    WHERE c.endereco.cidade = :cidade
    """)
    List<Cliente> findByCidade(String cidade);

    // Buscar clientes por cidade
    @Query("""
    SELECT c FROM Cliente c
    WHERE c.endereco.uf = :uf
    """)
    List<Cliente> findByUf(String uf);
}
