package view_controller.resident;

import controller.resident.ResidentController;
import entity.resident.Household;
import entity.resident.Resident;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.household.HouseholdModel;
import model.resident.ResidentModel;
import view_controller.BaseView;
import view_controller.DateConverter;
import view_controller.notification.NotificationView;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class ResidentUpdateView extends BaseView {

    private Resident resident;
    private Household oldHousehold;
    private boolean isUpdate = false;
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
    private Button saveBtn;

    // @FXML
    // private void onSearchByCCCD(ActionEvent actionEvent) {
    //
    // String cccd = txtCCCD.getText().trim();
    // Resident resident = ResidentController.getInstance().getResidentByCccd(cccd);
    // if (resident != null) {
    // txtHoTen.setText(resident.getFullName());
    // txtGioiTinh.setText(resident.getGender());
    //
    // Date birthDate = resident.getBirthDate();
    // LocalDate _birthDate = LocalDate.of(birthDate.getYear() + 1900,
    // birthDate.getMonth() + 1,
    // birthDate.getDate());
    // dateNgaySinh.setValue(_birthDate);
    //
    // txtDanToc.setText(resident.getEthnicity());
    // txtTonGiao.setText(resident.getReligion());
    // txtNgheNghiep.setText(resident.getOccupation());
    //
    // Date issueDate = resident.getIssueDate();
    // LocalDate _issueDate = LocalDate.of(issueDate.getYear() + 1900,
    // issueDate.getMonth() + 1,
    // issueDate.getDate());
    // dateNgayCap.setValue(_issueDate);
    //
    // txtNoiCap.setText(resident.getIssuePlace());
    // txtHoKhauID.setText(String.valueOf(resident.getHouseholdId()));
    // } else {
    // NotificationView.Create("Không tìm thấy cư dân với CCCD: " + cccd);
    // }
    // }

    @FXML
    private void onUpdateResident(ActionEvent actionEvent) throws SQLException {
        String cccd = txtCCCD.getText().trim();
        String name = txtHoTen.getText().trim();
        String gender = txtGioiTinh.getText().trim();

        LocalDate birthLocalDate = dateNgaySinh.getValue();
        Date birthDate;
        if (birthLocalDate != null) {
            birthDate = Date.from(birthLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        } else {
            birthDate = null; // Handle case where date is not selected
        }

        String dantoc = txtDanToc.getText().trim();
        String tonGiao = txtTonGiao.getText().trim();
        String occupation = txtNgheNghiep.getText().trim();

        LocalDate issueLocalDate = dateNgayCap.getValue();
        Date issueDate;
        if (issueLocalDate != null) {
            issueDate = Date.from(issueLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        } else {
            issueDate = null; // Handle case where date is not selected
        }

        String issuePlace = txtNoiCap.getText().trim();
        String householdNumber = txtHoKhauID.getText().trim().toUpperCase();
        int householdId = 0;
        if (!isUpdate) {
            if (!householdNumber.isEmpty()) {
                try {
                    householdId = HouseholdModel.getInstance().getHouseholdByNumber(householdNumber).getId();
                } catch (SQLException e) {
                    NotificationView.Create("Không tìm thấy hộ khẩu với số: " + householdNumber);
                    return;
                }
            }
        } else {
            Household household = HouseholdModel.getInstance().getHouseholdByNumber(householdNumber);
            if (household == null) {
                NotificationView.Create("Không tìm thấy hộ khẩu với số: " + householdNumber);
                return;
            }
            if (oldHousehold.getOwner().getId() == resident.getId() && oldHousehold.getId() != household.getId()) {
                NotificationView.Create("Chủ hộ không thể chuyển sang hộ khẩu khác!");
                return;
            }
            householdId = household.getId();
        }
        if (!isUpdate) {
            resident = new Resident();
            if (cccd.isEmpty() || name.isEmpty() || gender.isEmpty() || birthDate == null ||
                    dantoc.isEmpty() || tonGiao.isEmpty() || occupation.isEmpty() ||
                    issueDate == null || issuePlace.isEmpty()) {
                NotificationView.Create("Vui lòng điền đầy đủ thông tin!");
                return;
            }
        }
        resident.setIdentityNumber(cccd);
        resident.setFullName(name);
        resident.setBirthDate(birthDate);
        resident.setGender(gender);
        resident.setEthnicity(dantoc);
        resident.setReligion(tonGiao);
        resident.setOccupation(occupation);
        resident.setIssueDate(issueDate);
        resident.setIssuePlace(issuePlace);
        resident.setHouseholdId(householdId);
        boolean success;
        if (isUpdate) {
            success = ResidentModel.getInstance().updateResident(resident);
            if (success) {
                NotificationView.Create("Cập nhật cư dân thành công!");
            } else {
                NotificationView.Create("Cập nhật cư dân thất bại!");
            }
        } else {
            success = ResidentModel.getInstance().addResident(resident);
            if (success) {
                NotificationView.Create("Thêm mới cư dân thành công!");
            } else {
                NotificationView.Create("Thêm mới cư dân thất bại!");
            }
        }
        ResidentView.getCurrentView().refreshTable();
    }

    @Override
    public String getFxmlPath() {
        return "/resources/resident/residentUpdate.fxml";
        // return "/resident/residentUpdate.fxml";
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setResident(Resident resident) throws SQLException {
        this.resident = resident;
        if (resident != null) {
            txtCCCD.setText(resident.getIdentityNumber());
            txtHoTen.setText(resident.getFullName());
            txtGioiTinh.setText(resident.getGender());

            Date birthDate = resident.getBirthDate();
            LocalDate _birthDate = DateConverter.convertToLocalDate(birthDate);
            dateNgaySinh.setValue(_birthDate);

            txtDanToc.setText(resident.getEthnicity());
            txtTonGiao.setText(resident.getReligion());
            txtNgheNghiep.setText(resident.getOccupation());

            Date issueDate = resident.getIssueDate();
            LocalDate _issueDate = DateConverter.convertToLocalDate(issueDate);
            dateNgayCap.setValue(_issueDate);
            txtNoiCap.setText(resident.getIssuePlace());
            Household household = HouseholdModel.getInstance().getHouseholdById(resident.getHouseholdId());
            if (household != null) {
                String householdNumber = household.getHouseholdNumber();
                txtHoKhauID.setText(householdNumber);
                oldHousehold = household;
            }
        }
    }

    public void setUpdateMode(boolean isUpdate) {
        this.isUpdate = isUpdate;
        if (isUpdate) {
            saveBtn.setText("Cập nhật");
        } else {
            saveBtn.setText("Thêm mới");
        }
    }
}
