package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class BaseModel {
    protected String url = "jdbc:mysql://localhost:3306/chungcu_bluemoon"; // Tên database bạn tạo
    protected String username = "root"; // Tên người dùng MySQL
    protected String password = "mysql123456"; // Mật khẩu MySQL
    protected Connection connection = null;

    protected Connection getConnection() {
        try {
            // Bắt buộc nạp driver nếu không dùng build tool như Maven hoặc chạy từ IDE
            Class.forName("com.mysql.cj.jdbc.Driver"); // Hoặc com.mysql.jdbc.Driver với bản cũ
            connection = DriverManager.getConnection(url, username, password);
            return connection;
        } catch (ClassNotFoundException e) {
            System.err.println("Không tìm thấy JDBC Driver. Kiểm tra thư viện mysql-connector-j.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Lỗi khi kết nối đến CSDL:");
            e.printStackTrace();
        }
        return null;
    }

    protected void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng kết nối:");
                e.printStackTrace();
            }
        }
    }

}
