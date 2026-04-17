package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.entity.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    Optional<Item> findByCodigoInterno(String codigoInterno);
    List<Item> findByMarcaContainingIgnoreCase(String marca);
    List<Item> findByCodigoInternoContainingIgnoreCaseOrMarcaContainingIgnoreCaseOrDescricaoContainingIgnoreCase(String codigo, String marca, String descricao);
}
