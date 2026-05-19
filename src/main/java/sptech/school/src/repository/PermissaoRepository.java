package sptech.school.src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sptech.school.src.entity.Permissao;

@Repository
public interface PermissaoRepository extends JpaRepository<Permissao, Integer> {

}
