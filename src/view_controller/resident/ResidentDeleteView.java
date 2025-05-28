package view_controller.resident;

import controller.resident.ResidentController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import view_controller.BaseView;
import view_controller.notification.NotificationView;

import java.net.URL;
import java.util.ResourceBundle;

public class ResidentDeleteView extends BaseView {
    @FXML
    private TextField txtCCCD;

    @FXML
    private void onDeleteClick(ActionEvent actionEvent) {
        String cccd = txtCCCD.getText().trim();
        if (cccd.isEmpty()) {
            // Hiển thị thông báo lỗi nếu không nhập CCCD
            NotificationView.Create("Vui lòng nhập CCCD để xóa cư dân!");
            return;
        }

        boolean result = ResidentController.getInstance().removeResidentByCccd(cccd);
        if (result) {
            NotificationView.Create("Xóa cư dân thành công!");
            ResidentView.getCurrentView().refreshTable();
        } else {
            NotificationView.Create("Xóa cư dân thất bại! Vui lòng kiểm tra lại CCCD.");
        }
    }

    @Override
    public String getFxmlPath() {
        return "/resources/resident/residentDelete.fxml";
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
