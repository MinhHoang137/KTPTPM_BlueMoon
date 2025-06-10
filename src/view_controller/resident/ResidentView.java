package view_controller.resident;

import controller.household.HouseholdController;
import controller.resident.ResidentController;
import entity.resident.Household;
import entity.resident.Resident;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.resident.ResidentModel;
import view_controller.BaseView;
import view_controller.ViewController;
import view_controller.notification.NotificationView;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ResidentView extends BaseView {

    private static ResidentView residentView;

    @FXML
    private TextField txtSearch;
    @FXML
    private TableView<Resident> tableResidents;
    @FXML
    private TableColumn<Resident, String> colHoTen;
    @FXML
    private TableColumn<Resident, String> colNgaySinh;
    @FXML
    private TableColumn<Resident, String> colGioiTinh;
    @FXML
    private TableColumn<Resident, String> colDanToc;
    @FXML
    private TableColumn<Resident, String> colTonGiao;
    @FXML
    private TableColumn<Resident, String> colCMND;
    @FXML
    private TableColumn<Resident, String> colNgheNghiep;
    @FXML
    private TableColumn<Resident, String> colNgayCap;
    @FXML
    private TableColumn<Resident, String> colNoiCap;

    @FXML
    private TableColumn<Resident, String> colHoKhauID;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colHoTen.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colNgaySinh.setCellValueFactory(new PropertyValueFactory<>("birthDateString"));
        colGioiTinh.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colDanToc.setCellValueFactory(new PropertyValueFactory<>("ethnicity"));
        colTonGiao.setCellValueFactory(new PropertyValueFactory<>("religion"));
        colCMND.setCellValueFactory(new PropertyValueFactory<>("identityNumber"));
        colNgheNghiep.setCellValueFactory(new PropertyValueFactory<>("occupation"));
        colNgayCap.setCellValueFactory(new PropertyValueFactory<>("issueDateString"));
        colNoiCap.setCellValueFactory(new PropertyValueFactory<>("issuePlace"));
        // In your initialize() method
        colHoKhauID.setCellValueFactory(cellData -> {
            Resident resident = cellData.getValue();
            int householdId = resident.getHouseholdId();

            // Get related information based on household ID
            Household household = null;
            try {
                household = HouseholdController.getInstance().getHouseholdById(householdId);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            String displayValue = household != null ? household.getHouseholdNumber() : "Chua có hộ khẩu";

            return new SimpleStringProperty(displayValue);
        });

        List<Resident> residents = ResidentController.getInstance().getAllResidents();
        ObservableList<Resident> data = FXCollections.observableArrayList(residents);
        tableResidents.setItems(data);
    }

    @FXML
    private void onSearchByName() {
        String keyword = txtSearch.getText().trim();
        if (!keyword.isEmpty()) {

            List<Resident> result = ResidentController.getInstance().searchResidentsByName(keyword);
            tableResidents.setItems(FXCollections.observableArrayList(result));
        } else {
            NotificationView.Create("vui long nhap ten hoac CCCD:");
            refreshTable();
        }
    }

    @FXML
    private void onSearchByCCCD() {
        String keyword = txtSearch.getText().trim();
        if (!keyword.isEmpty()) {
            Resident r = ResidentController.getInstance().getResidentByCccd(keyword);
            if (r != null) {
                tableResidents.setItems(FXCollections.observableArrayList(r));
            } else {
                tableResidents.setItems(FXCollections.observableArrayList());
            }
        } else {
            NotificationView.Create("vui long nhap ten hoac CCCD:");
            refreshTable();
        }
    }

    @FXML
    private void onAddResident(ActionEvent actionEvent) {
        ResidentUpdateView residentUpdateView = (ResidentUpdateView) ViewController
                .getInstance().openView(new ResidentUpdateView(), "Cập nhật cư dân", 700, 600);

        residentUpdateView.setUpdateMode(false);
    }

    @FXML
    private void onUpdateResident(ActionEvent actionEvent) throws SQLException {
        Resident selectedResident = tableResidents.getSelectionModel().getSelectedItem();
        if (selectedResident == null) {
            NotificationView.Create("Vui lòng chọn một cư dân để cập nhật.");
            return;
        }
        ResidentUpdateView residentUpdateView = (ResidentUpdateView) ViewController.getInstance()
                .openView(new ResidentUpdateView(), "Cập nhật cư dân", 700, 600);
        Resident resident = ResidentController.getInstance().getResidentByCccd(selectedResident.getIdentityNumber());
        residentUpdateView.setUpdateMode(true);
        if (resident != null) {
            residentUpdateView.setResident(resident);
        } else {
            NotificationView.Create("Không tìm thấy cư dân với CCCD: " + selectedResident.getIdentityNumber());
        }
    }

    @FXML
    private void onDeleteResident(ActionEvent actionEvent) {
        Resident selectedResident = tableResidents.getSelectionModel().getSelectedItem();

        if (selectedResident == null) {
            NotificationView.Create("Vui lòng chọn cư dân cần xóa!");
            return;
        }

        // Create confirmation dialog
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác nhận xóa");
        confirmAlert.setHeaderText("Xóa cư dân");
        confirmAlert.setContentText("Bạn có chắc chắn muốn xóa cư dân: " + selectedResident.getFullName() + "?");

        // Add custom button types
        ButtonType buttonTypeYes = new ButtonType("Có", ButtonBar.ButtonData.YES);
        ButtonType buttonTypeNo = new ButtonType("Không", ButtonBar.ButtonData.NO);
        confirmAlert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == buttonTypeYes) {
            try {
                Resident resident = ResidentController.getInstance()
                        .getResidentByCccd(selectedResident.getIdentityNumber());
                boolean deleteResult = ResidentModel.getInstance().deleteResident(resident);

                if (deleteResult) {
                    NotificationView.Create("Xóa cư dân thành công!");
                    refreshTable(); // Refresh the table after successful deletion
                } else {
                    NotificationView.Create("Xóa cư dân thất bại!");
                }
            } catch (Exception e) {
                NotificationView.Create("Lỗi khi xóa cư dân, vui lòng thử lại sau.");
            }
        }
    }

    @Override
    public String getFxmlPath() {
        return "/resources/resident/resident.fxml";
        // return "/resident/resident.fxml";
    }

    @Override
    public BaseView loadAndShow(Stage stage, String title, int width, int height) {
        residentView = (ResidentView) super.loadAndShow(stage, title, width, height);
        return residentView;
    }

    public static ResidentView getCurrentView() {
        return residentView;
    }

    public void refreshTable() {
        List<Resident> residents = ResidentController.getInstance().getAllResidents();
        ObservableList<Resident> data = FXCollections.observableArrayList(residents);
        tableResidents.setItems(data);
    }

}