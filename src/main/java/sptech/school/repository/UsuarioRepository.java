package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sptech.school.entity.Usuario;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByEmailAndAtivoTrue(String email);
    Optional<Usuario> findByIdAndAtivoTrue(Long id);
    List<Usuario> findAllByAtivoTrue();
    long countByPermissaoNomeAndAtivoTrue(String nomePermissao);


    @Query("SELECT DISTINCT u FROM Usuario u " +
            "LEFT JOIN u.permissao p " +
            "WHERE " +
            "(:nome IS NULL OR LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
            "(:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
            "(:dataCadastro IS NULL OR u.dataCadastro <= :dataCadastro) AND " +
            "(:cargo IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :cargo, '%'))) AND " +
            "(:ativo IS NULL OR u.ativo = :ativo)")
    List<Usuario> pesquisarEFiltrar(
            @Param("nome") String nome,
            @Param("email") String email,
            @Param("dataCadastro") LocalDateTime dataCadastro,
            @Param("cargo") String cargo,
            @Param("ativo") Boolean ativo
    );
}
