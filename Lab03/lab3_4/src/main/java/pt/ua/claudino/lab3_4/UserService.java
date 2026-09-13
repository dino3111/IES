package pt.ua.claudino.lab3_4;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();
    Optional<User> findById(Long id);
    User create(User user);
    Optional<User> update(Long id, User user);
    boolean delete(Long id);
}
