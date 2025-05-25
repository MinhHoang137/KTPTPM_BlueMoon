package repository.user;

import entity.user.User;
import model.BaseModel;

import java.sql.*;
import java.util.Optional;

public class UserLoginRepositoryImpl extends BaseModel implements UserLoginRepository {

    public UserLoginRepositoryImpl() {
        super();
    }

    @Override
    public Optional<User> login(String username, String password) {
        String sql = "SELECT id, username, password, role, created_at, updated_at FROM users WHERE username = ? AND password = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));

                System.out.println("Đăng nhập thành công: " + user.getUsername());
                return Optional.of(user);
            } else {
                System.out.println("Đăng nhập thất bại: sai tên đăng nhập hoặc mật khẩu.");
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi kết nối hoặc truy vấn CSDL:");
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return Optional.empty();
    }
}