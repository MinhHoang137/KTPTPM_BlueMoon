package view_controller.household;

import controller.household.HouseholdController;
import entity.resident.Household;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import view_controller.BaseView;
import view_controller.DateConverter;
import view_controller.notification.NotificationView;

import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

public class HouseholdUpdateView extends BaseView {

    @FXML
    private TextField tfHouseholdNumber;
    @FXML
    private DatePicker dpRegistrationDate;
    @FXML
    private TextField tfOwnerId;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnSearch;

    @FXML
    private void onSave() throws SQLException {
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
        boolean res = HouseholdController.getInstance().updateHouseholdByNumber(householdNumber, ownerId,
                registrationDate);
        if (res) {
            NotificationView.Create("Cập nhật hộ gia đình thành công!");
            HouseholdView.getCurrentView().refreshTable();
        } else {
            NotificationView.Create("Cập nhật hộ gia đình thất bại!");
        }
    }

    @FXML
    private void onSearchByNumber() {
        String householdNumber = tfHouseholdNumber.getText();
        householdNumber = householdNumber.trim();
        if (householdNumber.isEmpty()) {
            NotificationView.Create("Vui lòng nhập số hộ gia đình!");
            return;
        }
        try {
            Household household = HouseholdController.getInstance().getHouseholdByNumber(householdNumber);
            dpRegistrationDate.setValue(DateConverter.convertToLocalDate(household.getRegistrationDate()));
            String ownerIdStr = String.valueOf(household.getOwnerId());
            tfOwnerId.setText(ownerIdStr);
        } catch (SQLException e) {
            NotificationView.Create("Không tìm thấy hộ gia đình!");
        }
    }

    @Override
    public String getFxmlPath() {
        return "/household/householdUpdate.fxml";
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
