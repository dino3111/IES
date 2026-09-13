package pt.ua.claudino.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ua.claudino.backend.model.Grade;

public interface GradeRepository extends JpaRepository<Grade, Long> {
}
