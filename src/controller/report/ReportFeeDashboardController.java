package controller.report;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ReportFeeDashboardController {

    @FXML private Button feeCollectionBtn;
    @FXML private Button feeStatusBtn;

    @FXML
    public void initialize() {
        // nothing special
    }

    @FXML
    private void onFeeCollectionBtnClicked(ActionEvent event) {
        openPopup("/resources/view/report/reportFeeCollections.fxml", "Báo cáo Thu Phí (Tổng hợp)");
    }

    @FXML
    private void onFeeStatusBtnClicked(ActionEvent event) {
        openPopup("/resources/view/report/reportFeeStatus.fxml", "Trạng thái Thu Phí");
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
