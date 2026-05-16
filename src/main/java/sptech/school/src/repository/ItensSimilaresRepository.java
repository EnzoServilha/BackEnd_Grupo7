package sptech.school.src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.src.entity.Item;

public interface ItensSimilaresRepository extends JpaRepository<Item, Integer> {
}
