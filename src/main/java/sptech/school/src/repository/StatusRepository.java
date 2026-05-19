package sptech.school.src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.src.entity.Status;

public interface StatusRepository extends JpaRepository<Status, Integer> {
}

