package model.user;

import entity.user.User;
import repository.user.UserRegisterRepository;

import java.util.Optional;

public class UserRegisterModel {

    private final UserRegisterRepository userRegisterRepository;

    public UserRegisterModel(UserRegisterRepository userRegisterRepository) {
        this.userRegisterRepository = userRegisterRepository;
    }

    // Hàm đăng ký trả về true nếu lưu thành công, false nếu không
    public boolean register(User user) {
        return userRegisterRepository.save(user);
    }

    // Tìm người dùng theo username
    public Optional<User> findUserByUsername(String username) {
        return userRegisterRepository.findByUsername(username);
    }
}
