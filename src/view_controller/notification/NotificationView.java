package view_controller.notification;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view_controller.BaseView;

import java.net.URL;
import java.util.ResourceBundle;

public class NotificationView extends BaseView {

    public static String message;

    @FXML
    private Label lblMessage;

    @FXML
    private void onOKClick() {
        Stage stage = (Stage) lblMessage.getScene().getWindow();
        stage.close();
    }

    @Override
    public String getFxmlPath() {
        return "/resources/notification.fxml";
    }

    @Override
    public BaseView loadAndShow(Stage stage, String title, int width, int height) {
        try {
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL); // khóa các cửa sổ khác
            URL resource = getClass().getResource(getFxmlPath());
            if (resource == null) {
                resource = getClass().getResource(RESOURCE_PATH + getFxmlPath());
                return null;
            }
            System.out.println("Resource: " + resource);
            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();

            stage.setTitle(title);
            stage.setScene(new Scene(root, width, height));
            ((NotificationView) loader.getController()).setMessage(message); // truyền message vào controller
            stage.showAndWait(); // hiện modal và chặn đến khi đóng

            return loader.getController();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void setMessage(String message) {
        lblMessage.setText(message);
    }

    public static void Create(String message) {
        NotificationView.message = message;
        Stage stage = new Stage();
        NotificationView notificationView = new NotificationView();
        notificationView.loadAndShow(stage, "Thông báo", 400, 200);
    }
}
