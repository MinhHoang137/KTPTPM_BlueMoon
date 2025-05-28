package view_controller.resident;

import controller.resident.ResidentController;
import entity.resident.Resident;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import view_controller.BaseView;
import view_controller.ViewController;

import java.net.URL;
import java.util.List;
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
    private TableColumn<Resident, Integer> colID;
    @FXML
    private TableColumn<Resident, Integer> colHoKhauID;

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
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colHoKhauID.setCellValueFactory(new PropertyValueFactory<>("householdId"));

        List<Resident> residents = ResidentController.getInstance().getAllResidents();
        ObservableList<Resident> data = FXCollections.observableArrayList(residents);
        tableResidents.setItems(data);
    }

    @FXML
    private void onSearchByName() {
        String keyword = txtSearch.getText().trim();
        if (!keyword.isEmpty()) {
            // Tìm kiếm danh sách cư dân theo tên → hiển thị trên bảng
            List<Resident> result = ResidentController.getInstance().searchResidentsByName(keyword);
            tableResidents.setItems(FXCollections.observableArrayList(result));
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
                tableResidents.setItems(FXCollections.observableArrayList()); // Trống nếu không tìm thấy
            }
        }
    }

    @FXML
    private void onAddResident(ActionEvent actionEvent) {
        ViewController.getInstance().openView(new ResidentAddView(), "Thêm cư dân", 1000, 700);
    }

    @FXML
    private void onUpdateResident(ActionEvent actionEvent) {
        ViewController.getInstance().openView(new ResidentUpdateView(), "Cập nhật cư dân", 1000, 700);
    }

    @FXML
    private void onDeleteResident(ActionEvent actionEvent) {
        ViewController.getInstance().openView(new ResidentDeleteView(), "Xóa cư dân", 1000, 700);
    }

    @Override
    public String getFxmlPath() {
        return "/resources/resident/resident.fxml";
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

    public void refreshAResident(String cccd) {
        Resident resident = ResidentController.getInstance().getResidentByCccd(cccd);
        List<Resident> data = tableResidents.getItems();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getIdentityNumber().equals(cccd)) {
                data.set(i, resident);
                break;
            }
        }
    }
}