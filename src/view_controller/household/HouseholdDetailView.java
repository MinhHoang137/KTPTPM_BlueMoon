package view_controller.household;

import entity.resident.Household;
import entity.resident.Resident;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import view_controller.BaseView;

import java.net.URL;
import java.util.ResourceBundle;

public class HouseholdDetailView extends BaseView {

    // FXML components - household information
    @FXML
    private Label lblHouseholdNumber;
    @FXML
    private Label lblAddressNumber;
    @FXML
    private Label lblOwnerName;
    @FXML
    private Label lblRegistrationDate;
    @FXML
    private Label lblMemberCount;

    // FXML components - members table
    @FXML
    private TableView<Resident> tvMembers;
    @FXML
    private TableColumn<Resident, String> colMemberName;
    @FXML
    private TableColumn<Resident, String> colMemberCCCD;
    @FXML
    private TableColumn<Resident, String> colMemberGender;
    @FXML
    private TableColumn<Resident, String> colMemberBirthDate;

    // FXML components - buttons
    @FXML
    private Button btnClose;

    // Data
    private Household household;
    private ObservableList<Resident> membersList = FXCollections.observableArrayList();

    @Override
    public String getFxmlPath() {
        return "/resources/household/householdDetailView.fxml";
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
    }

    /**
     * Thiết lập cấu hình cho bảng thành viên
     */
    private void setupTable() {
        // Cấu hình các cột của bảng
        colMemberName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colMemberCCCD.setCellValueFactory(new PropertyValueFactory<>("identityNumber"));
        colMemberGender.setCellValueFactory(new PropertyValueFactory<>("gender"));

        // Cột ngày sinh cần format đặc biệt
        colMemberBirthDate.setCellValueFactory(cellData -> {
            Resident resident = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(
                    resident.getBirthDateString());
        });

        // Gán dữ liệu cho bảng
        tvMembers.setItems(membersList);

        // Cấu hình để bảng chiếm hết không gian có thể
        tvMembers.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /**
     * Thiết lập dữ liệu hộ khẩu để hiển thị
     * 
     * @param household đối tượng Household cần hiển thị
     */
    public void setHousehold(Household household) {
        this.household = household;
        updateDisplay();
    }

    /**
     * Cập nhật hiển thị thông tin hộ khẩu
     */
    private void updateDisplay() {
        if (household == null) {
            clearDisplay();
            return;
        }

        // Hiển thị thông tin cơ bản của hộ khẩu
        lblHouseholdNumber.setText(household.getHouseholdNumber() != null ? household.getHouseholdNumber() : "-");

        lblAddressNumber.setText(household.getAddressNumber() != null ? household.getAddressNumber() : "-");

        lblOwnerName.setText(household.getOwner() != null ? household.getOwner().getFullName() : "-");

        lblRegistrationDate
                .setText(household.getRegistrationDate() != null ? household.getFormattedRegistrationDate() : "-");

        // Cập nhật danh sách thành viên
        updateMembersList();
    }

    /**
     * Cập nhật danh sách thành viên
     */
    private void updateMembersList() {
        membersList.clear();

        if (household != null && household.getMembers() != null) {
            membersList.addAll(household.getMembers());
        }

        // Cập nhật số lượng thành viên
        int memberCount = membersList.size();
        lblMemberCount.setText("(" + memberCount + " thành viên)");
    }

    /**
     * Xóa toàn bộ hiển thị
     */
    private void clearDisplay() {
        lblHouseholdNumber.setText("-");
        lblAddressNumber.setText("-");
        lblOwnerName.setText("-");
        lblRegistrationDate.setText("-");
        lblMemberCount.setText("(0 thành viên)");
        membersList.clear();
    }

    /**
     * Xử lý sự kiện nhấn nút "Đóng"
     */
    @FXML
    private void onClose() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    /**
     * Hiển thị thông báo alert
     * 
     * @param title     tiêu đề
     * @param message   nội dung
     * @param alertType loại alert
     */
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Getter cho household hiện tại
     * 
     * @return household đang được hiển thị
     */
    public Household getHousehold() {
        return household;
    }
}