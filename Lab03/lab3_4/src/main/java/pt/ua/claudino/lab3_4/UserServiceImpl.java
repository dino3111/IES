package pt.ua.claudino.lab3_4;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public User create(User user) {
        return repository.save(user);
    }

    @Override
    public Optional<User> update(Long id, User user) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setName(user.getName());
                    existing.setEmail(user.getEmail());
                    existing.setPhone(user.getPhone());
                    return repository.save(existing);
                });
    }

    @Override
    public boolean delete(Long id) {
        return repository.findById(id)
                .map(existing -> {
                    repository.delete(existing);
                    return true;
                })
                .orElse(false);
    }
}
