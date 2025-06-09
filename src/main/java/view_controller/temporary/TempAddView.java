package view_controller.temporary;

import controller.temporary.TempController;
import entity.resident.Resident;
import entity.temporary.TemporaryRegistration;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.resident.ResidentModel;
import view_controller.BaseView;

import java.net.URL;
import java.time.LocalDate;
import java.util.Base64;
import java.util.ResourceBundle;

public class TempAddView extends BaseView {
    @FXML
    private TextField cccdField;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TextField addressField;
    @FXML
    private TextField reasonField;

    @FXML
    private void onAdd() throws Exception {
        String cccd = cccdField.getText().trim();
        String type = typeComboBox.getValue();
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();
        String address = addressField.getText().trim();
        String reason = reasonField.getText().trim();

        if (cccd.isEmpty() || type == null || start == null || address.isEmpty() || reason.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Vui lòng nhập đầy đủ thông tin.");
            alert.showAndWait();
            return;
        }
        Resident resident = ResidentModel.getInstance().getResidentByCCCD(cccd);
        if (resident == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Không tìm thấy cư dân với CCCD: " + cccd);
            alert.showAndWait();
            return;
        }
        if (end != null && end.isBefore(start)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ngày kết thúc không thể trước ngày bắt đầu.");
            alert.showAndWait();
            return;
        }
        int personId = resident.getId();
        TemporaryRegistration tempReg = new TemporaryRegistration(personId, cccd, type, start, end, address, reason);
        boolean isAdded = TempController.getInstance().add(tempReg);
        if (isAdded) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Thêm đăng ký tạm trú thành công.");
            alert.showAndWait();
            TempView.getCurrentView().refreshTable();
            cccdField.clear();
            typeComboBox.setValue(null);
            startDatePicker.setValue(null);
            endDatePicker.setValue(null);
            addressField.clear();
            reasonField.clear();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Thêm đăng ký tạm trú thất bại. Vui lòng thử lại.");
            alert.showAndWait();
        }
        TempView.getCurrentView().refreshTable();
    }

    @Override
    public String getFxmlPath() {
        return "/temporary/tempAddView.fxml";
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        typeComboBox.setItems(FXCollections.observableArrayList("Tạm trú", "Tạm vắng"));
    }
}
