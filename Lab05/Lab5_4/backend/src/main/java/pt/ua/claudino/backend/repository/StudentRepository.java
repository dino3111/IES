package pt.ua.claudino.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ua.claudino.backend.model.Student;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);
}
