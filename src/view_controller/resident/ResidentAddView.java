package view_controller.resident;

import controller.resident.ResidentController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import view_controller.BaseView;
import view_controller.notification.NotificationView;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class ResidentAddView extends BaseView {

    @FXML private TextField txtHoTen;
    @FXML private DatePicker dateNgaySinh;
    @FXML private TextField txtGioiTinh;
    @FXML private TextField txtDanToc;
    @FXML private TextField txtTonGiao;
    @FXML private TextField txtCCCD;
    @FXML private TextField txtNgheNghiep;
    @FXML private DatePicker dateNgayCap;
    @FXML private TextField txtNoiCap;
    @FXML private TextField txtMaHoKhau;

    @FXML
    private void onAddClick() throws Exception {
        String name = txtHoTen.getText();
        Date birthDate = convert(dateNgaySinh);
        String gender = txtGioiTinh.getText();
        String ethnicity = txtDanToc.getText();
        String religion = txtTonGiao.getText();
        String cccd = txtCCCD.getText();
        String occupation = txtNgheNghiep.getText();
        Date issueDate = convert(dateNgayCap);
        String issuePlace = txtNoiCap.getText();
        String txtHoussholdId = txtMaHoKhau.getText();
        int householdId = 0;
        if (txtHoussholdId != null && !txtHoussholdId.isEmpty()) {
            try {
                householdId = Integer.parseInt(txtHoussholdId);
            } catch (NumberFormatException e) {
                NotificationView.Create("Mã hộ khẩu không hợp lệ!");
                return;
            }
        }
        boolean result = ResidentController.getInstance().addResident(name, cccd, gender,
                birthDate, ethnicity, religion, occupation, issueDate, issuePlace, householdId);
        if (result) {
            NotificationView.Create("Thêm cư dân thành công!");
            ResidentView.getCurrentView().refreshTable();
        } else {
            NotificationView.Create("Thêm cư dân thất bại!");
        }
    }

    private Date convert(DatePicker picker) {
        return (picker.getValue() != null) ?
                java.sql.Date.valueOf(picker.getValue()) : null;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    @Override
    public String getFxmlPath() {
        return "/resident/residentAdd.fxml";
    }
}
