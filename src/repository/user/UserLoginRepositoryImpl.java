package repository.user;

import entity.user.User;
import model.BaseModel;
import java.sql.*;
import java.util.Optional;

import javax.swing.SpringLayout.Constraints;

public class UserLoginRepositoryImpl extends BaseModel implements UserLoginRepository {
    private final String TABLE_NAME = "users";

    @Override
    public Optional<User> login(String username, String password) {

        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE username = ? AND password = ?";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            System.out.println("Thực hiện login với:");
            System.out.println("Username = [" + username + "]");
            System.out.println("Password = [" + password + "]");

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
                System.out.println("Đăng nhập thất bại: sai username hoặc password.");
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi kết nối hoặc truy vấn CSDL:");
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
