package controller.user;

import entity.user.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.user.UserRegisterModel;
import repository.user.UserRegisterRepositoryImpl;

import java.io.IOException;

public class UserRegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> roleField;

    private UserRegisterModel userRegisterModel;

    @FXML
    public void initialize() {
        // Khởi tạo model với repository thực thi
        userRegisterModel = new UserRegisterModel(new UserRegisterRepositoryImpl());

        // Thêm 2 vai trò cố định vào ComboBox
        roleField.getItems().addAll("Quản trị", "Ban quản lý dân cư");
        roleField.getSelectionModel().selectFirst(); // Chọn mặc định "Quản trị"
    }

    @FXML
    private void onRegisterClicked() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String role = roleField.getValue();

        if (username.isEmpty() || password.isEmpty() || role == null || role.isEmpty()) {
            showAlert("Vui lòng nhập đầy đủ thông tin.");
            return;
        }

        if (userRegisterModel.findUserByUsername(username).isPresent()) {
            showAlert("Tên đăng nhập đã tồn tại. Vui lòng chọn tên khác.");
            return;
        }

        User user = new User(0, username, password, role);
        boolean success = userRegisterModel.register(user);
        if (success) {
            showAlert("Đăng ký thành công người dùng: " + username);
            // Chuyển về giao diện đăng nhập
            switchToLoginView();
        } else {
            showAlert("Đăng ký thất bại.");
        }
    }

    @FXML
    private void onBackClicked() {
        switchToLoginView();
    }

    private void switchToLoginView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user/userManage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow(); // lấy stage hiện tại
            stage.setScene(new Scene(root));
            stage.setTitle("Đăng nhập");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi khi chuyển sang giao diện đăng nhập.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
