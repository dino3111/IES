package pt.ua.claudino.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ua.claudino.backend.model.Course;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByName(String name);
}
