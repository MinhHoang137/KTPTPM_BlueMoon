package controller.user;

import controller.fee.FeeManagerController;
import controller.fee.FeeListController;
import controller.fee.PaymentListController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomePageController {

    @FXML private Button btnFee;
    @FXML private Button btnReport;
    @FXML private Button btnHouseholdPerson;
    @FXML private Button btnVehicle;
    @FXML private Button btnLogout;

    @FXML private StackPane contentPane;

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        loadContentIntoCenter("/resources/view/main/WelcomeScreen.fxml");
    }

    public void onFeeClicked(ActionEvent event) {
        loadContentIntoCenter("/resources/view/fee/feeManager.fxml");
    }

    public void onReportClicked(ActionEvent event) {
        loadContentIntoCenter("/resources/view/report/ReportManager.fxml");
    }

    public void onHouseholdPersonClicked(ActionEvent event) {
        loadContentIntoCenter("/resources/view/household/HouseholdList.fxml");
    }

    public void onVehicleClicked(ActionEvent event) {
        loadContentIntoCenter("/resources/view/vehicle/VehicleList.fxml");
    }

    public void onLogoutClicked(ActionEvent event) {
        System.out.println("Người dùng đã đăng xuất.");
        
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/view/user/UserLogin.fxml"));
            Parent root = loader.load();

            stage.setScene(new Scene(root));
            stage.setTitle("Đăng nhập");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể quay lại màn hình đăng nhập.");
            e.printStackTrace();
        }
    }

    public void loadContentIntoCenter(String fxmlPath) {
        try {
            if (getClass().getResource(fxmlPath) == null) {
                throw new IOException("FXML file not found: " + fxmlPath + ". Please check the path and ensure the file is in resources folder.");
            }
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent content = loader.load();
            Object controller = loader.getController();

            // Truyền tham chiếu HomePageController cho các controller con
            if (controller instanceof FeeManagerController) {
                ((FeeManagerController) controller).setHomePageController(this);
            } else if (controller instanceof FeeListController) {
                ((FeeListController) controller).setHomePageController(this);
            } else if (controller instanceof PaymentListController) {
                //((PaymentListController) controller).setHomePageController(this);
            }
            // Thêm các trường hợp khác nếu các màn hình khác cũng cần tham chiếu HomePageController

            contentPane.getChildren().setAll(content);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi tải giao diện", "Không thể tải nội dung: " + e.getMessage());
            e.printStackTrace();
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