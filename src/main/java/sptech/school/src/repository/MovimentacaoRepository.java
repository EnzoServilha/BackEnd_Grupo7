package sptech.school.src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sptech.school.src.entity.MovimentacaoEstoque;

import java.util.List;

public interface MovimentacaoRepository extends JpaRepository<MovimentacaoEstoque, Integer> {
    @Query("SELECT m FROM MovimentacaoEstoque m WHERE m.tipo.nome = :tipo")
    List<MovimentacaoEstoque> buscarPorTipo(String tipo);

    @Query("SELECT m FROM MovimentacaoEstoque m WHERE m.status.nome = :status")
    List<MovimentacaoEstoque> buscarPorStatus(String status);
}
