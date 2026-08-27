package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sptech.school.entity.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    Optional<Item> findByCodigoInterno(String codigoInterno);

    @Query("SELECT i FROM Item i " +
            "WHERE LOWER(i.marca) LIKE LOWER(CONCAT('%', :marca, '%')) ESCAPE '\\'")
    List<Item> findByMarcaContainingIgnoreCase(@Param("marca") String marca);

    @Query("SELECT i FROM Item i " +
            "WHERE LOWER(i.codigoInterno) LIKE LOWER(CONCAT('%', :termo, '%')) ESCAPE '\\' " +
            "OR    LOWER(i.marca)         LIKE LOWER(CONCAT('%', :termo, '%')) ESCAPE '\\' " +
            "OR    LOWER(i.descricao)     LIKE LOWER(CONCAT('%', :termo, '%')) ESCAPE '\\'")
    List<Item> pesquisarPorTermo(@Param("termo") String termo);

    @Query("SELECT DISTINCT i FROM Item i " +
            "JOIN i.codigosAssociados ca " +
            "WHERE LOWER(ca.codigo) LIKE LOWER(CONCAT('%', :codigo, '%')) ESCAPE '\\'")
    List<Item> buscarPorCodigoAssociado(@Param("codigo") String codigo);
}
