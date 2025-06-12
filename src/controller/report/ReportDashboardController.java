package controller.report;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ReportDashboardController {

    @FXML private Button reportBtn;
    @FXML private Button statsBtn;

    @FXML
    public void initialize() {
        // nothing to initialize
    }

    @FXML
    private void onReportBtnClicked(ActionEvent event) {
        openPopup("/resources/view/report/reportFeeDashboard.fxml", "Báo cáo");
    }

    @FXML
    private void onStatsBtnClicked(ActionEvent event) {
        openPopup("/resources/view/statistics/statisticsDashboard.fxml", "Thống kê");
    }

    private void openPopup(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle(title);
            popupStage.setScene(scene);
            popupStage.setWidth(1000);
            popupStage.setHeight(700);
            popupStage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void onBackClicked() throws IOException {
    // Lấy Stage hiện tại
    Stage stage = (Stage) reportBtn.getScene().getWindow();

    // Tải giao diện mới
    Parent root = FXMLLoader.load(getClass().getResource("/resources/view/user/homePage.fxml"));

    // Thiết lập giao diện mới vào Stage
    stage.setScene(new Scene(root));

    // Cập nhật tiêu đề cho cửa sổ (tùy chọn)
    stage.setTitle("Trang chủ");
}

}
