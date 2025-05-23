package view_controller.resident;

import controller.resident.ResidentController;
import entity.resident.Resident;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import view_controller.BaseView;
import view_controller.notification.NotificationView;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class ResidentUpdateView extends BaseView {
    @FXML
    private TextField txtCCCD;

    @FXML
    private TextField txtHoTen;

    @FXML
    private TextField txtGioiTinh;

    @FXML
    private DatePicker dateNgaySinh;

    @FXML
    private TextField txtDanToc;

    @FXML
    private TextField txtTonGiao;

    @FXML
    private TextField txtNgheNghiep;

    @FXML
    private DatePicker dateNgayCap;

    @FXML
    private TextField txtNoiCap;

    @FXML
    private TextField txtHoKhauID;

    @FXML
    private void onSearchByCCCD(ActionEvent actionEvent) {
        // TODO: xử lý tìm kiếm theo CCCD
        String cccd = txtCCCD.getText().trim();
        Resident resident = ResidentController.getInstance().getResidentByCccd(cccd);
        if (resident != null) {
            txtHoTen.setText(resident.getFullName());
            txtGioiTinh.setText(resident.getGender());

            Date birthDate = resident.getBirthDate();
            LocalDate _birthDate = LocalDate.of(birthDate.getYear() + 1900, birthDate.getMonth() + 1, birthDate.getDate());
            dateNgaySinh.setValue(_birthDate);

            txtDanToc.setText(resident.getEthnicity());
            txtTonGiao.setText(resident.getReligion());
            txtNgheNghiep.setText(resident.getOccupation());

            Date issueDate = resident.getIssueDate();
            LocalDate _issueDate = LocalDate.of(issueDate.getYear() + 1900, issueDate.getMonth() + 1, issueDate.getDate());
            dateNgayCap.setValue(_issueDate);

            txtNoiCap.setText(resident.getIssuePlace());
            txtHoKhauID.setText(String.valueOf(resident.getHouseholdId()));
        } else {
            NotificationView.Create("Không tìm thấy cư dân với CCCD: " + cccd);
        }
    }

    @FXML
    private void onUpdateResident(ActionEvent actionEvent) {
        // TODO: xử lý cập nhật thông tin cư dân
        String cccd = txtCCCD.getText().trim();
        String name = txtHoTen.getText().trim();
        String gender = txtGioiTinh.getText().trim();

        LocalDate birthLocalDate = dateNgaySinh.getValue();
        Date birthDate = Date.from(birthLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        String dantoc = txtDanToc.getText().trim();
        String tonGiao = txtTonGiao.getText().trim();
        String occupation = txtNgheNghiep.getText().trim();

        LocalDate issueLocalDate = dateNgayCap.getValue();
        Date issueDate = Date.from(issueLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        String issuePlace = txtNoiCap.getText().trim();
        int householdId = Integer.parseInt(txtHoKhauID.getText().trim());
        ResidentController.getInstance().updateResident(cccd, name, gender, birthDate,
                dantoc, tonGiao, occupation, issueDate, issuePlace, householdId);
        NotificationView.Create("Cập nhật cư dân thành công.");
        ResidentView.getCurrentView().refreshAResident(cccd);
    }

    @Override
    public String getFxmlPath() {
        return "/resident/residentUpdate.fxml";
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
