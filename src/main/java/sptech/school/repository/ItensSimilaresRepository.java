package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.entity.Item;

public interface ItensSimilaresRepository extends JpaRepository<Item, Integer> {
}
