package repository.user;

import entity.user.User;

import java.util.Optional;

public interface UserRegisterRepository {
    boolean save(User user);

    Optional<User> findByUsername(String username);
}
