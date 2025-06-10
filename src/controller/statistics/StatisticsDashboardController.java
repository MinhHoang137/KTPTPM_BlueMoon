package controller.statistics;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StatisticsDashboardController {

    @FXML private Button demoBtn;
    @FXML private Button popBtn;

    @FXML
    public void initialize() {
        demoBtn.setOnAction(this::openDemographics);
        popBtn.setOnAction(this::openPopulationByWard);
    }

    @FXML
    private void openDemographics(ActionEvent event) {
        openPopup("/resources/view/statistics/statsDemographics.fxml", "Thống kê Nhân khẩu");
    }

    @FXML
    private void openPopulationByWard(ActionEvent event) {
        openPopup("/resources/view/statistics/statsPopulationByWard.fxml", "Nhân khẩu theo Phường");
    }

    private void openPopup(String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Scene scene = new Scene(root);
            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.setTitle(title);
            popup.setScene(scene);
            popup.setWidth(1000);
            popup.setHeight(700);
            popup.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
