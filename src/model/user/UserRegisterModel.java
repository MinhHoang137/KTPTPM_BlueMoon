package model.user;

import entity.user.User;
import repository.user.UserRegisterRepository;

import java.util.Optional;

public class UserRegisterModel {

    private final UserRegisterRepository userRegisterRepository;

    public UserRegisterModel(UserRegisterRepository userRegisterRepository) {
        this.userRegisterRepository = userRegisterRepository;
    }

    public boolean register(User user) {
        return userRegisterRepository.save(user);
    }

    public Optional<User> findUserByUsername(String username) {
        return userRegisterRepository.findByUsername(username);
    }
}
