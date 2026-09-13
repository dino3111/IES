package pt.ua.claudino.lab3_3;

import pt.ua.claudino.lab3_3.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}