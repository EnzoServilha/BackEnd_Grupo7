package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sptech.school.entity.Permissao;

@Repository
public interface PermissaoRepository extends JpaRepository<Permissao, Integer> {

}
