package view_controller.household;

import controller.household.HouseholdController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import view_controller.BaseView;
import view_controller.notification.NotificationView;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class HouseholdDeleteView extends BaseView {
    @FXML private Button btnDelete;
    @FXML private TextField tfHouseholdNumber;

    @Override
    public String getFxmlPath() {
        return "/household/householdDelete.fxml";
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML private void onDelete(ActionEvent actionEvent) throws SQLException {
        String householdNumber = tfHouseholdNumber.getText().trim();
        if (householdNumber.isEmpty()) {
            NotificationView.Create("Vui lòng nhập số hộ gia đình!");
            return;
        }
        boolean res = HouseholdController.getInstance().deleteHouseholdByNumber(householdNumber);
        if (res) {
            NotificationView.Create("Xóa hộ gia đình thành công!");
            HouseholdView.getCurrentView().refreshTable();
        } else {
            NotificationView.Create("Xóa hộ gia đình thất bại!");
        }
    }
}
