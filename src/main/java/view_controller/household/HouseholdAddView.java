package view_controller.household;

import controller.household.HouseholdController;
import javafx.fxml.FXML;
import view_controller.BaseView;
import view_controller.DateConverter;
import view_controller.notification.NotificationView;

import javafx.scene.control.*;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class HouseholdAddView extends BaseView {
    @FXML
    private TextField tfHouseholdNumber;
    @FXML
    private DatePicker dpRegistrationDate;
    @FXML
    private TextField tfOwnerId;
    @FXML
    private Button btnSave;

    @FXML
    private void onSave() {
        String householdNumber = tfHouseholdNumber.getText();
        String ownerIdStr = tfOwnerId.getText();
        int ownerId = 0;
        if (ownerIdStr != null && !ownerIdStr.isEmpty()) {
            try {
                ownerId = Integer.parseInt(ownerIdStr);
            } catch (NumberFormatException e) {
                NotificationView.Create("Mã chủ hộ không hợp lệ!");
                return;
            }
        }
        Date registrationDate = DateConverter.convertToDate(dpRegistrationDate.getValue());
        boolean res = HouseholdController.getInstance().addHousehold(householdNumber, ownerId, registrationDate);
        if (res) {
            NotificationView.Create("Thêm hộ gia đình thành công!");
            HouseholdView.getCurrentView().refreshTable();
        } else {
            NotificationView.Create("Thêm hộ gia đình thất bại!");
        }
    }

    @Override
    public String getFxmlPath() {
        return "/household/householdAdd.fxml";
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
