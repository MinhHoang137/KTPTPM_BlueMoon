package controller.fee;

import controller.user.HomePageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FeeManagerController {

    @FXML private Label lblTitle;
    @FXML private Button btnManageFeeItems;
    @FXML private Button btnManagePayments;

    private HomePageController homePageController;

    public void setHomePageController(HomePageController homePageController) {
        this.homePageController = homePageController;
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void onManageFeeItemsClicked(ActionEvent event) {
        if (homePageController != null) {
            homePageController.loadContentIntoCenter("/resources/view/fee/feeList.fxml");
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải trang: Controller không được thiết lập đúng.");
        }
    }

    @FXML
    private void onManagePaymentsClicked(ActionEvent event) {
        if (homePageController != null) {
            homePageController.loadContentIntoCenter("/resources/view/fee/paymentList.fxml");
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải trang: Controller không được thiết lập đúng.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}