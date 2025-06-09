package controller.user;

import entity.user.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import repository.user.UserLoginRepository;
import repository.user.UserLoginRepositoryImpl;

import java.util.Optional;

public class UserLoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button backButton;

    // Khởi tạo repository
    private final UserLoginRepository userLoginRepository = new UserLoginRepositoryImpl();

    @FXML
    private void initialize() {
        // Thiết lập hiệu ứng hover cho nút đăng nhập
        loginButton.setStyle(
                "-fx-background-color: #1a73e8; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 8 15; -fx-cursor: hand;");

        loginButton.setOnMouseEntered(e -> {
            loginButton.setStyle(
                    "-fx-background-color: #155ab6; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 8 15; -fx-cursor: hand;");
        });

        loginButton.setOnMouseExited(e -> {
            loginButton.setStyle(
                    "-fx-background-color: #1a73e8; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 8 15; -fx-cursor: hand;");
        });

        // Tùy chọn: hiệu ứng hover cho nút quay lại (nếu cần)
        if (backButton != null) {
            backButton.setStyle(
                    "-fx-background-color: #cccccc; -fx-text-fill: black; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 8 15; -fx-cursor: hand;");

            backButton.setOnMouseEntered(e -> {
                backButton.setStyle(
                        "-fx-background-color: #aaaaaa; -fx-text-fill: black; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 8 15; -fx-cursor: hand;");
            });

            backButton.setOnMouseExited(e -> {
                backButton.setStyle(
                        "-fx-background-color: #cccccc; -fx-text-fill: black; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 8 15; -fx-cursor: hand;");
            });
        }
    }

    @FXML
    private void onLoginClicked() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Vui lòng nhập đầy đủ thông tin đăng nhập");
            return;
        }

        Optional<User> userOptional = userLoginRepository.login(username, password);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            showAlert("Đăng nhập thành công với tài khoản: " + user.getUsername());
            goToHomePage();
        } else {
            showAlert("Tên đăng nhập hoặc mật khẩu không đúng");
        }
    }

    @FXML
    private void onBackClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user/userManage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Đăng ký");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Không thể quay lại. Lỗi: " + e.getMessage());
        }
    }

    private void goToHomePage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user/homePage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Home Page");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Không thể chuyển đến trang chủ. Lỗi: " + e.getMessage());
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
