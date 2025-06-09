package controller.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import view_controller.ViewController;
import view_controller.household.HouseholdView;
import view_controller.resident.ResidentView;
import view_controller.temporary.*;
import java.io.IOException;

import entity.resident.Household;

public class HomePageController {

    public void onFeeClicked(ActionEvent event) throws IOException {
        loadPage("/view/fee/feeManager.fxml", event);
    }

    @FXML
    private void onReportStatClicked(ActionEvent event) throws IOException {
        loadPage("/view/report/reportDashboard.fxml", event);
    }

    public void onResidentClicked(ActionEvent event) throws IOException {
        ViewController.getInstance().openView(new ResidentView(), "Danh sách dân cư", 700, 600);
    }

    public void onLogoutClicked(ActionEvent event) throws IOException {

        loadPage("/view/user/userManage.fxml", event);
    }

    public void onHouseHoldClicked(ActionEvent event) throws IOException {

        ViewController.getInstance().openView(new HouseholdView(), "Danh sách hộ khẩu", 700, 600);
    }

    public void onTemporaryClicked(ActionEvent event) throws IOException {

        ViewController.getInstance().openView(new TempView(), "Tạm trú tạm vắng", 700, 600);
    }

    private void loadPage(String fxmlPath, ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}