package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sptech.school.entity.ItensNaMovimentacao;
import sptech.school.entity.MovimentacaoEstoque;

import java.util.List;

public interface ItensNaMovimentacaoRepository extends JpaRepository<ItensNaMovimentacao, Integer> {
    List<ItensNaMovimentacao> findAllByItemId(Integer itemId);
    List<ItensNaMovimentacao> findAllByMovimentacaoEstoqueId(Integer movimentacaoEstoqueId);
    boolean existsByMovimentacaoEstoqueIdAndItemId(Integer movimentacaoEstoqueId, Integer itemId);
    void deleteAllByMovimentacaoEstoqueId(Integer movimentacaoEstoqueId);

    List<ItensNaMovimentacao> findByMovimentacaoEstoque_Periodo_IdAndMovimentacaoEstoque_Tipo_NomeIn(
            Integer periodoId, List<String> nomesTipo);


    @Query("SELECT DISTINCT im FROM ItensNaMovimentacao im " +
            "JOIN im.movimentacaoEstoque m " +
            "LEFT JOIN m.cliente cl " +
            "LEFT JOIN m.fornecedor f " +
            "JOIN im.item i " +
            "LEFT JOIN i.codigosAssociados ca " +
            "LEFT JOIN m.tipo t " +
            "WHERE " +
            "(:contato IS NULL OR (LOWER(cl.nomeContato) LIKE LOWER(CONCAT('%', :contato, '%')) OR LOWER(f.nomeContato) LIKE LOWER(CONCAT('%', :contato, '%')))) AND " +
            "(:codigo IS NULL OR (LOWER(i.codigoInterno) LIKE LOWER(CONCAT('%', :codigo, '%')) OR LOWER(ca.codigo) LIKE LOWER(CONCAT('%', :codigo, '%')))) AND " +
            "(:tipo IS NULL OR LOWER(t.nome) LIKE LOWER(CONCAT('%', :tipo, '%')))")
    List<ItensNaMovimentacao> pesquisarEFiltrar(
            @Param("contato") String contato,
            @Param("codigo") String codigo,
            @Param("tipo") String tipo
    );
}

