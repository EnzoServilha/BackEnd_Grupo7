package sptech.school.repository;


import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sptech.school.dto.periodo.PeriodoQtdPecasDTO;
import sptech.school.entity.Periodo;

import java.util.List;
import java.util.Optional;

public interface PeriodoRepository extends JpaRepository<Periodo, Integer> {
    Periodo findFirstByOrderByIdDesc();
    List<Periodo> findAllByOrderByIdDesc();
    Optional<Periodo> findFirstByFechadoTrueOrderByIdDesc();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Periodo> findFirstByFechadoFalseOrderByIdDesc();


    @Query("""
        SELECT COALESCE(SUM(
            CASE 
                WHEN t.nome = 'ENTRADA' THEN itm.qtd 
                WHEN t.nome = 'AJUSTE' THEN itm.qtd 
                WHEN t.nome = 'SAIDA' THEN itm.qtd * -1 
                ELSE 0 
            END
        ), 0)
        FROM Periodo p
        JOIN MovimentacaoEstoque me ON me.periodo.id = p.id
        JOIN ItensNaMovimentacao itm ON itm.movimentacaoEstoque.id = me.id
        JOIN me.tipo t
                WHERE p.id = :idPeriodo
                    AND me.status.nome = 'CONCLUIDO'
                    AND me.status.nome <> 'CANCELADO'
    """)
    Integer pegarTotalDePecasDoPeriodo(@Param("idPeriodo") Integer idPeriodo);

    // Esse "new PeriodoQtdPecasDTO(" serve para indicar o tipo de objeto que ele deve
    // devolver, já que aqui ele n devolve só um campo e nem um objeto de classe padrão
    @Query("""
    SELECT new sptech.school.dto.periodo.PeriodoQtdPecasDTO(itm.item.id,
           SUM(CASE 
                WHEN t.nome = 'ENTRADA' THEN itm.qtd 
                WHEN t.nome = 'AJUSTE' THEN itm.qtd 
                WHEN t.nome = 'SAIDA' THEN itm.qtd * -1 
                ELSE 0 END), itm.item.descricao)
    FROM MovimentacaoEstoque me
    JOIN ItensNaMovimentacao itm ON itm.movimentacaoEstoque.id = me.id
    JOIN me.tipo t
        WHERE me.periodo.id = :idPeriodo
            AND me.status.nome = 'CONCLUIDO'
            AND me.status.nome <> 'CANCELADO'
    GROUP BY itm.item.id
""")
    List<PeriodoQtdPecasDTO> pegarSaldoPorItemDoPeriodo(@Param("idPeriodo") Integer idPeriodo);

}
