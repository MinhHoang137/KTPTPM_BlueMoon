package model.user;

import entity.user.User;
import repository.user.UserLoginRepository;

import java.util.Optional;

public class UserLoginModel {
    private final UserLoginRepository userLoginRepository;

    public UserLoginModel(UserLoginRepository userLoginRepository) {
        this.userLoginRepository = userLoginRepository;
    }

    // Phương thức login trả về Optional<User> nếu tìm được user hợp lệ
    public Optional<User> login(String username, String password) {
        return userLoginRepository.login(username, password);
    }
}
