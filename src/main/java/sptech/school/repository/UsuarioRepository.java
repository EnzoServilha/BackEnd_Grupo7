package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByEmailAndAtivoTrue(String email);
    Optional<Usuario> findByIdAndAtivoTrue(Long id);
    List<Usuario> findAllByAtivoTrue();
    long countByPermissaoNomeAndAtivoTrue(String nomePermissao);

}
