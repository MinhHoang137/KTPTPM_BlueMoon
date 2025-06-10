package view_controller.household;

import controller.household.HouseholdController;
import entity.resident.Household;
import entity.resident.Resident;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import view_controller.BaseView;
import view_controller.ViewController;
import view_controller.notification.NotificationView;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HouseholdView extends BaseView {

    @FXML
    private Button btnDetail;
    @FXML
    private TextField tfSearch;
    @FXML
    private Button btnSearchById, btnSearchByNumber, btnSearchByAddress;
    @FXML
    private Button btnAdd, btnUpdate, btnDelete;

    @FXML
    private TableView<Household> householdTable;
    @FXML
    private TableColumn<Household, String> colHouseholdNumber;
    @FXML
    private TableColumn<Household, String> colAddressNumber;
    @FXML
    private TableColumn<Household, String> colRegistrationDate;
    @FXML
    private TableColumn<Household, String> colOwnerName;

    private static HouseholdView householdView;

    public static HouseholdView getCurrentView() {
        return householdView;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colHouseholdNumber.setCellValueFactory(new PropertyValueFactory<>("householdNumber"));
        colAddressNumber.setCellValueFactory(new PropertyValueFactory<>("addressNumber"));
        colRegistrationDate.setCellValueFactory(cellData -> javafx.beans.binding.Bindings
                .createStringBinding(() -> cellData.getValue().getFormattedRegistrationDate()));
        colOwnerName.setCellValueFactory(cellData -> {
            if (cellData.getValue().getOwner() != null) {
                return javafx.beans.binding.Bindings
                        .createStringBinding(() -> cellData.getValue().getOwner().getFullName());
            } else {
                return javafx.beans.binding.Bindings.createStringBinding(() -> "Chưa có");
            }
        });

        refreshTable();
    }

    @Override
    public String getFxmlPath() {
        return "/resources/household/household.fxml";
    }

    @FXML
    private void onSearchById(ActionEvent e) {
        String idText = tfSearch.getText().trim();
        if (idText.isEmpty()) {
            NotificationView.Create("Vui lòng nhập mã hộ khẩu!");
            refreshTable();
            return;
        }
        try {
            int id = Integer.parseInt(idText);
            Household household = HouseholdController.getInstance().getHouseholdById(id);
            if (household != null) {
                householdTable.setItems(FXCollections.observableArrayList(household));
            } else {
                householdTable.getItems().clear();
                NotificationView.Create("Không tìm thấy hộ khẩu với ID đã nhập.");
            }
        } catch (NumberFormatException ex) {
            NotificationView.Create("ID không hợp lệ!");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void onSearchByNumber(ActionEvent e) {
        String number = tfSearch.getText().trim();
        if (number.isEmpty()) {
            NotificationView.Create("Vui lòng nhập số hộ khẩu!");
            refreshTable();
            return;
        }
        try {
            Household household = HouseholdController.getInstance().getHouseholdByNumber(number);
            if (household != null) {
                householdTable.setItems(FXCollections.observableArrayList(household));
            } else {
                householdTable.getItems().clear();
                NotificationView.Create("Không tìm thấy hộ khẩu với số đã nhập.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void onSearchByAddress(ActionEvent e) {
        String address = tfSearch.getText().trim();
        if (address.isEmpty()) {
            NotificationView.Create("Vui lòng nhập số phòng!");
            refreshTable();
            return;
        }
        Household household = HouseholdController.getInstance().getHouseholdsByAddress(address);
        if (household != null) {
            householdTable.setItems(FXCollections.observableArrayList(household));
        } else {
            householdTable.getItems().clear();
            NotificationView.Create("Không tìm thấy hộ khẩu với số phòng đã nhập.");
            refreshTable();
        }
    }

    @FXML
    private void onAdd(ActionEvent e) {
        HouseholdUpdateView addView = (HouseholdUpdateView) ViewController.getInstance()
                .openView(new HouseholdUpdateView(), "Thêm hộ gia đình", 700, 700);
        addView.setUpdateMode(false);
    }

    @FXML
    private void onUpdate(ActionEvent e) throws SQLException {
        Household selected = householdTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            NotificationView.Create("Vui lòng chọn một hộ khẩu để cập nhật.");
            return;
        }
        Household household = HouseholdController.getInstance().getHouseholdByNumber(selected.getHouseholdNumber());
        HouseholdUpdateView updateView = (HouseholdUpdateView) ViewController.getInstance()
                .openView(new HouseholdUpdateView(), "Cập nhật hộ gia đình", 700, 700);
        updateView.setHousehold(household);
        updateView.setUpdateMode(true);
    }

    @FXML
    private void onDelete(ActionEvent e) {
        Household selected = householdTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn một hộ khẩu để xóa.");
            alert.showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận xóa");
        confirm.setHeaderText(null);
        confirm.setContentText("Bạn có chắc chắn muốn xóa hộ khẩu số " + selected.getHouseholdNumber() + "?");

        // Hiển thị hộp thoại và chờ người dùng phản hồi
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // TODO: Logic xóa hộ khẩu
                try {
                    Household deleteHousehold = HouseholdController.getInstance()
                            .getHouseholdByNumber(selected.getHouseholdNumber());
                    boolean result = HouseholdController.getInstance().deleteHousehold(deleteHousehold);
                    if (result) {
                        NotificationView.Create("Xóa hộ khẩu thành công!");
                        refreshTable();
                    } else {
                        NotificationView.Create("Xóa hộ khẩu thất bại!");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    NotificationView.Create("Lỗi khi xóa hộ khẩu");
                }
                refreshTable();
            }
        });
    }

    public void refreshTable() {
        try {
            List<Household> households = HouseholdController.getInstance().getAllHouseholds();
            ObservableList<Household> data = FXCollections.observableArrayList(households);
            householdTable.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BaseView loadAndShow(Stage stage, String title, int width, int height) {
        householdView = (HouseholdView) super.loadAndShow(stage, title, width, height);
        return householdView;
    }

    @FXML
    private void onDetail(ActionEvent actionEvent) throws SQLException {
        HouseholdDetailView householdDetailView = (HouseholdDetailView) ViewController.getInstance()
                .openView(new HouseholdDetailView(), "Chi tiết hộ gia đình", 800, 600);
        Household selected = householdTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            NotificationView.Create("Vui lòng chọn một hộ khẩu để xem chi tiết.");
            return;
        }
        Household household = HouseholdController.getInstance()
                .getHouseholdByNumber(selected.getHouseholdNumber());
        householdDetailView.setHousehold(household);
    }
}
