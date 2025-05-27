package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class BaseModel {
    protected String url = "jdbc:mysql://localhost:3306/v1db"; 
    protected String username = "root"; 
    protected String password_db = "9012345";
    protected Connection connection = null;
    protected Connection getConnection()  {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            connection = DriverManager.getConnection(url, username, password_db);
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
