package sptech.school.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sptech.school.dto.periodo.PeriodoQtdPecasDTO;
import sptech.school.entity.Periodo;

import java.util.List;

public interface PeriodoRepository extends JpaRepository<Periodo, Integer> {
    Periodo findFirstByOrderByIdDesc();
    List<Periodo> findAllByOrderByIdDesc();


    @Query("""
        SELECT COALESCE(SUM(
            CASE 
                WHEN t.nome = 'compra' THEN itm.qtd 
                WHEN t.nome = 'venda' THEN itm.qtd * -1 
                ELSE 0 
            END
        ), 0)
        FROM Periodo p
        JOIN MovimentacaoEstoque me ON me.periodo.id = p.id
        JOIN ItensNaMovimentacao itm ON itm.movimentacaoEstoque.id = me.id
        JOIN me.tipo t
        WHERE p.id = :idPeriodo
    """)
    Integer pegarTotalDePecasDoPeriodo(@Param("idPeriodo") Integer idPeriodo);


    @Query("""
    SELECT itm.item_id, 
           SUM(CASE WHEN t.nome = 'compra' THEN itm.qtd 
                    WHEN t.nome = 'venda' THEN itm.qtd * -1 
                    ELSE 0 END)
    FROM MovimentacaoEstoque me
    JOIN ItensNaMovimentacao itm ON itm.movimentacaoEstoque.id = me.id
    JOIN me.tipo t
    WHERE me.periodo.id = :idPeriodo
    GROUP BY itm.item_id
""")
    List<PeriodoQtdPecasDTO> pegarSaldoPorItemDoPeriodo(@Param("idPeriodo") Integer idPeriodo);

}
