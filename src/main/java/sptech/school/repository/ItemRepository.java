package sptech.school.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sptech.school.entity.Item;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    Optional<Item> findByCodigoInterno(String codigoInterno);
    Optional<Item> findByIdAndAtivoTrue(Integer id);
    Page<Item> findAllByAtivoTrue(Pageable pageable);

    @Query("SELECT i FROM Item i " +
            "WHERE i.ativo = true " +
            "AND LOWER(i.marca) LIKE LOWER(CONCAT('%', :marca, '%')) ESCAPE '\\'")
    List<Item> findByMarcaContainingIgnoreCase(@Param("marca") String marca);

    List<Item> findByMarcaContainingIgnoreCaseAndAtivoTrue(String marca);

    @Query("SELECT i FROM Item i " +
            "WHERE i.ativo = true AND (" +
            "LOWER(i.codigoInterno) LIKE LOWER(CONCAT('%', :termo, '%')) ESCAPE '\\' " +
            "OR    LOWER(i.marca)         LIKE LOWER(CONCAT('%', :termo, '%')) ESCAPE '\\' " +
            "OR    LOWER(i.descricao)     LIKE LOWER(CONCAT('%', :termo, '%')) ESCAPE '\\')")
    List<Item> pesquisarPorTermo(@Param("termo") String termo);

    @Query("SELECT DISTINCT i FROM Item i " +
            "JOIN i.codigosAssociados ca " +
            "WHERE i.ativo = true AND ca.ativo = true " +
            "AND LOWER(ca.codigo) LIKE LOWER(CONCAT('%', :codigo, '%')) ESCAPE '\\'")
    List<Item> buscarPorCodigoAssociado(@Param("codigo") String codigo);

    @Query("SELECT DISTINCT i FROM Item i " +
            "LEFT JOIN i.codigosAssociados ca " +
            "WHERE " +
            "(:codigoAssociado IS NULL OR LOWER(ca.codigo) LIKE LOWER(CONCAT('%', :codigoAssociado, '%'))) AND " +
            "(:codigoInterno IS NULL OR LOWER(i.codigoInterno) LIKE LOWER(CONCAT('%', :codigoInterno, '%'))) AND " +
            "(:localizacao IS NULL OR LOWER(i.localizacao) LIKE LOWER(CONCAT('%', :localizacao, '%'))) AND " +
            "(:marcas IS NULL OR i.marca IN :marcas) AND " +
            "(:dataInicial IS NULL OR i.dataCadastro >= :dataInicial) AND " +
            "(:dataFinal IS NULL OR i.dataCadastro <= :dataFinal)")
    List<Item> pesquisarEFiltrar(
            @Param("codigoAssociado") String codigoAssociado,
            @Param("codigoInterno") String codigoInterno,
            @Param("localizacao") String localizacao,
            @Param("marcas") List<String> marcas,
            @Param("dataInicial") LocalDate dataInicial,
            @Param("dataFinal") LocalDate dataFinal
    );
}
