package view_controller.temporary;

import controller.temporary.TempController;
import entity.temporary.TemporaryRegistration;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import view_controller.BaseView;

import java.net.URL;
import java.util.ResourceBundle;

public class TempDeleteView extends BaseView {
    @FXML
    private TextField recordIdField;

    @FXML
    private void onDelete() {
        String id = recordIdField.getText().trim();
        if (!id.isEmpty()) {
            // Thực hiện xóa bản ghi theo mã
            System.out.println("Đang xóa bản ghi có mã: " + id);
            // Gọi DAO hoặc Service để xử lý
            TemporaryRegistration tempReg = TempController.getInstance().findById(Integer.parseInt(id));
            if (tempReg != null) {
                boolean isDeleted = TempController.getInstance().delete(tempReg);
                if (isDeleted) {
                    // Hiển thị thông báo thành công
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Xóa bản ghi thành công.");
                    alert.showAndWait();
                    TempView.getCurrentView().refreshTable();
                    recordIdField.clear();
                } else {
                    // Hiển thị thông báo lỗi nếu không xóa được
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Không thể xóa bản ghi. Vui lòng thử lại.");
                    alert.showAndWait();
                }
            } else {
                // Hiển thị thông báo nếu không tìm thấy bản ghi
                Alert alert = new Alert(Alert.AlertType.WARNING, "Không tìm thấy bản ghi với mã: " + id);
                alert.showAndWait();
            }
        } else {
            // Cảnh báo người dùng
            Alert alert = new Alert(Alert.AlertType.WARNING, "Vui lòng nhập mã để xóa.");
            alert.showAndWait();
        }
    }

    @Override
    public String getFxmlPath() {
        return "/resources/temporary/tempDeleteView.fxml";
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
