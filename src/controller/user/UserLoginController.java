package controller.user;

import entity.user.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

    // Khởi tạo repository
    private final UserLoginRepository userLoginRepository = new UserLoginRepositoryImpl();

    @FXML
    private void onLoginClicked() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Vui lòng nhập đầy đủ thông tin đăng nhập");
            return;
        }

        // Gọi repository để kiểm tra từ CSDL
        Optional<User> userOptional = userLoginRepository.login(username, password);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            showAlert("Đăng nhập thành công với tài khoản: " + user.getUsername());
            goToHomePage();
        } else {
            showAlert("Tên đăng nhập hoặc mật khẩu không đúng");
        }
    }

    private void goToHomePage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/view/user/homePage.fxml"));
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
