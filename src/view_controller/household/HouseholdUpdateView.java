package view_controller.household;

import controller.household.HouseholdController;
import controller.resident.ResidentController;
import entity.resident.Household;
import entity.resident.Resident;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.resident.ResidentModel;
import view_controller.BaseView;
import view_controller.DateConverter;
import view_controller.notification.NotificationView;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

public class HouseholdUpdateView extends BaseView {
    private HouseholdController householdController = HouseholdController.getInstance();
    // Private FXML components
    @FXML
    private TextField tfHouseholdNumber;

    @FXML
    private TextField tfAddressNumber;

    @FXML
    private DatePicker dpRegistrationDate;

    @FXML
    private TextField tfOwnerId;

    @FXML
    private Button btnSave;

    private boolean isUpdate = false;

    // Private attributes
    private Household household;

    // Private methods
    @FXML
    private void onSave() throws SQLException {
        if (validateInput()) {
            String householdNumber = tfHouseholdNumber.getText().trim().toUpperCase();
            String addressNumber = tfAddressNumber.getText().trim().toUpperCase();
            String ownerIdStr = tfOwnerId.getText().trim();
            Date registrationDate = DateConverter.convertToDate(dpRegistrationDate.getValue());

            // Allow owner to be null if no owner ID is provided
            Resident owner = null;
            if (!ownerIdStr.isEmpty()) {
                owner = ResidentController.getInstance().getResidentByCccd(ownerIdStr);
                if (owner == null) {
                    NotificationView.Create("Chủ hộ không tồn tại!");
                    return;
                }
            }

            if (!isUpdate)
                household = new Household();

            household.setHouseholdNumber(householdNumber);
            household.setAddressNumber(addressNumber);
            household.setRegistrationDate(registrationDate);

            // Set owner and ownerId (can be null/0)
            household.setOwner(owner);
            household.setOwnerId(owner != null ? owner.getId() : 0);

            boolean result;
            if (isUpdate) {
                result = householdController.updateHousehold(household);
            } else {
                result = householdController.addHousehold(household) > 0;
                // Only update resident's household ID if there's an owner
                if (owner != null) {
                    int newHouseholdId = householdController.getHouseholdByNumber(householdNumber).getId();
                    Resident updatedOwner = ResidentController.getInstance().getResidentByCccd(ownerIdStr);
                    if (updatedOwner != null) {
                        updatedOwner.setHouseholdId(newHouseholdId);
                        ResidentModel.getInstance().updateResident(updatedOwner);
                    }
                }
            }

            if (result) {
                NotificationView.Create("Cập nhật hộ gia đình thành công!");
                HouseholdView.getCurrentView().refreshTable();
                Stage stage = (Stage) btnSave.getScene().getWindow();
                stage.close();
            } else {
                NotificationView.Create("Cập nhật hộ gia đình thất bại!");
            }
        }
    }

    @Override
    public String getFxmlPath() {
        return "/resources/household/householdUpdate.fxml";
        // return "/household/householdUpdate.fxml";
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialization logic will be added later
    }

    public void setHousehold(Household household) {
        this.household = household;
        if (household != null) {
            tfHouseholdNumber.setText(household.getHouseholdNumber());
            tfAddressNumber.setText(household.getAddressNumber());
            dpRegistrationDate.setValue(DateConverter.convertToLocalDate(household.getRegistrationDate()));
            if (household.getOwner() != null) {
                tfOwnerId.setText(String.valueOf(household.getOwner().getIdentityNumber()));
            } else {
                tfOwnerId.clear();
            }
        } else {
            clearFields();
        }
    }

    public void setUpdateMode(boolean isUpdate) {
        this.isUpdate = isUpdate;
        if (isUpdate) {
            btnSave.setText("Cập nhật");
        } else {
            btnSave.setText("Thêm mới");
        }
    }

    private void clearFields() {
        tfHouseholdNumber.clear();
        tfAddressNumber.clear();
        dpRegistrationDate.setValue(null);
        tfOwnerId.clear();
    }

    private boolean validateInput() throws SQLException {
        String householdNumber = tfHouseholdNumber.getText().trim().toUpperCase();
        String oldHouseholdNumber = household != null ? household.getHouseholdNumber() : "";
        String addressNumber = tfAddressNumber.getText().trim().toUpperCase();
        String oldAddressNumber = household != null ? household.getAddressNumber() : "";
        String ownerIdStr = tfOwnerId.getText().trim();
        Date registrationDate = DateConverter.convertToDate(dpRegistrationDate.getValue());

        // Basic validation
        if (householdNumber.isEmpty() || addressNumber.isEmpty() || registrationDate == null) {
            if (isUpdate) {
                NotificationView.Create("Vui lòng điền đầy đủ thông tin!");
            } else {
                NotificationView.Create("Vui lòng điền đầy đủ thông tin (Chủ hộ có thể để trống khi thêm mới)!");
            }
            return false;
        }

        // Check household number uniqueness
        if (!householdNumber.equals(oldHouseholdNumber)
                && HouseholdController.getInstance().getHouseholdByNumber(householdNumber) != null) {
            NotificationView.Create("Số hộ gia đình đã tồn tại!");
            return false;
        }

        // Check address uniqueness
        if (householdController.getHouseholdByAddress(addressNumber) != null
                && !addressNumber.equals(oldAddressNumber)) {
            NotificationView.Create("Số địa chỉ đã tồn tại!");
            return false;
        }

        // Owner validation - required for updates, optional for new additions
        if (isUpdate && ownerIdStr.isEmpty()) {
            NotificationView.Create("Chủ hộ là bắt buộc khi cập nhật hộ gia đình!");
            return false;
        }

        // Validate owner if owner ID is provided
        if (!ownerIdStr.isEmpty()) {
            Resident owner = ResidentController.getInstance().getResidentByCccd(ownerIdStr);
            if (owner == null) {
                NotificationView.Create("Chủ hộ không tồn tại!");
                return false;
            }

            // Check if owner already belongs to another household
            if (owner.getHouseholdId() != 0 && (household == null || owner.getHouseholdId() != household.getId())) {
                NotificationView.Create("Chủ hộ đã thuộc về hộ gia đình khác!");
                return false;
            }
        }

        // Date validation
        Date now = DateConverter.convertToDate(LocalDate.now());
        if (registrationDate.after(now)) {
            NotificationView.Create("Ngày đăng ký không thể sau ngày hiện tại!");
            return false;
        }

        return true;
    }
}