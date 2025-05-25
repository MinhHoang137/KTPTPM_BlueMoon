package repository.user;

import entity.user.User;
import java.util.Optional;

public interface UserLoginRepository {
    Optional<User> login(String username, String password);
}
