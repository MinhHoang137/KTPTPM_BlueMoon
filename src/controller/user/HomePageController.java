package controller.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomePageController {

    public void onFeeClicked(ActionEvent event) throws IOException {
        loadPage("/resources/view/fee/feeManager.fxml", event);
    }

    public void onReportClicked(ActionEvent event) throws IOException {
        loadPage("/resources/view/report/reportManager.fxml", event);
    }

    public void onResidentClicked(ActionEvent event) throws IOException {
        loadPage("", event);
    }

    public void onLogoutClicked(ActionEvent event) throws IOException {

        loadPage("", event);
    }

    private void loadPage(String fxmlPath, ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}