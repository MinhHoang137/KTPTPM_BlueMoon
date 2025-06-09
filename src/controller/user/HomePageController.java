package controller.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view_controller.ViewController;
import view_controller.household.HouseholdView;
import view_controller.resident.ResidentView;
import view_controller.temporary.*;
import java.io.IOException;

import entity.resident.Household;

public class HomePageController {

    public void onFeeClicked(ActionEvent event) throws IOException {
        loadPage("/resources/view/fee/feeManager.fxml", event);
    }

    public void onReportClicked(ActionEvent event) throws IOException {
        loadPage("/resources/view/report/reportManager.fxml", event);
    }

    public void onResidentClicked(ActionEvent event) throws IOException {
        ViewController.getInstance().openView(new ResidentView(), "Danh sách dân cư", 700, 600);
    }

    public void onLogoutClicked(ActionEvent event) throws IOException {

        loadPage("/resources/view/user/userManage.fxml", event);
    }

    public void onHouseHoldClicked(ActionEvent event) throws IOException {

        ViewController.getInstance().openView(new HouseholdView(), "Danh sách hộ khẩu", 700, 600);
    }

    public void onTemporaryClicked(ActionEvent event) throws IOException {

        ViewController.getInstance().openView(new TempView(), "Tạm trú tạm vắng", 700, 600);
    }

    public void onVehicleClicked(ActionEvent event) throws IOException {

        loadPage("/resources/vehicle/VehicleList.fxml", event);
    }

    private void loadPage(String fxmlPath, ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}