package controller.user;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class UserManageController {

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnRegister;

    @FXML
    public void handleLogin() throws Exception {
        Stage stage = (Stage) btnLogin.getScene().getWindow();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/resources/view/user/userLogin.fxml")));
        stage.setScene(scene);
        stage.setTitle("Đăng nhập");
        stage.show();
    }

    @FXML
    public void handleRegister() throws Exception {
        Stage stage = (Stage) btnRegister.getScene().getWindow();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/resources/view/user/userRegister.fxml")));
        stage.setScene(scene);
        stage.setTitle("Đăng ký");
        stage.show();
    }
}
