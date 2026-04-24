package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.entity.Status;

public interface StatusRepository extends JpaRepository<Status, Integer> {
}

