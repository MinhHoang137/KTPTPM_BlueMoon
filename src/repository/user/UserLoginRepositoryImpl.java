package repository.user;

import entity.user.User;

import java.sql.*;
import java.util.Optional;

public class UserLoginRepositoryImpl implements UserLoginRepository {

    private static final String URL = "jdbc:mysql://localhost:3306/chungcu_bluemoon?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "mysql123456";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    @Override
    public Optional<User> login(String username, String password) {
        // Lưu ý: 'user' là từ khóa trong MySQL, nên cần bọc bằng dấu huyền (`user`)
        String sql = "SELECT * FROM `user` WHERE username = ? AND password = ?";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Debug để kiểm tra đầu vào
            System.out.println("Thực hiện login với:");
            System.out.println("Username = [" + username + "]");
            System.out.println("Password = [" + password + "]");

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("userid"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("vaitro"));
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
