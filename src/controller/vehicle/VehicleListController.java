package controller.vehicle;

import entity.vehicle.Vehicle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets; // Import Insets
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.Dialog; // Import Dialog
import javafx.scene.control.ButtonType; // Import ButtonType
import javafx.scene.control.Label; // Import Label
import javafx.scene.layout.GridPane; // Import GridPane
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import model.vehicle.VehicleModel;
import controller.user.HomePageController;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class VehicleListController implements Initializable {

    @FXML private TextField tfSearch;
    @FXML private Button btnSearchById;
    @FXML private Button btnSearchByLicensePlate;
    @FXML private Button btnReload;
    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;

    @FXML private TableView<Vehicle> vehicleTable;
    @FXML private TableColumn<Vehicle, Integer> colId;
    @FXML private TableColumn<Vehicle, Integer> colOwnerPersonId;
    @FXML private TableColumn<Vehicle, String> colBrand;
    @FXML private TableColumn<Vehicle, String> colModel;
    @FXML private TableColumn<Vehicle, String> colLicensePlate;
    @FXML private TableColumn<Vehicle, String> colColor;
    @FXML private TableColumn<Vehicle, String> colImagePath;
    @FXML private TableColumn<Vehicle, Date> colCreatedAt;
    @FXML private TableColumn<Vehicle, Date> colUpdatedAt;
    @FXML private TableColumn<Vehicle, Void> colDetail;

    private VehicleModel vehicleModel;
    private HomePageController homePageController;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void setHomePageController(HomePageController homePageController) {
        this.homePageController = homePageController;
    }

    public VehicleListController() {
        this.vehicleModel = new VehicleModel();
    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        onReloadTable();
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colOwnerPersonId.setCellValueFactory(new PropertyValueFactory<>("ownerPersonId"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colLicensePlate.setCellValueFactory(new PropertyValueFactory<>("licensePlate"));
        colColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        colImagePath.setCellValueFactory(new PropertyValueFactory<>("imagePath"));
        colCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        colUpdatedAt.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));

        colDetail.setCellFactory(col -> new TableCell<Vehicle, Void>() {
            private final Button btn = new Button("Chi tiết");
            {
                btn.setOnAction(event -> {
                    Vehicle item = getTableView().getItems().get(getIndex());
                    showVehicleDetailDialog(item);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    @FXML
    private void onSearchById(ActionEvent event) {
        String idText = tfSearch.getText().trim();
        if (idText.isEmpty()) {
            onReloadTable();
            return;
        }
        try {
            int id = Integer.parseInt(idText);
            Vehicle vehicle = vehicleModel.getVehicleById(id);
            if (vehicle != null) {
                ObservableList<Vehicle> data = FXCollections.observableArrayList(vehicle);
                vehicleTable.setItems(data);
            } else {
                vehicleTable.getItems().clear();
                showAlert(Alert.AlertType.INFORMATION, "Thông báo", "Không tìm thấy xe với ID: " + id);
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu", "ID phải là một số nguyên.");
        }
    }

    @FXML
    private void onSearchByLicensePlate(ActionEvent event) {
        String licensePlate = tfSearch.getText().trim();
        if (licensePlate.isEmpty()) {
            onReloadTable();
            return;
        }
        Vehicle vehicle = vehicleModel.getVehicleByLicensePlate(licensePlate);
        if (vehicle != null) {
            ObservableList<Vehicle> data = FXCollections.observableArrayList(vehicle);
            vehicleTable.setItems(data);
        } else {
            vehicleTable.getItems().clear();
            showAlert(Alert.AlertType.INFORMATION, "Thông báo", "Không tìm thấy xe với Biển số: " + licensePlate);
        }
    }

    @FXML
    public void onReloadTable() {
        List<Vehicle> vehicles = vehicleModel.getAllVehicles();
        if (vehicles != null) {
            ObservableList<Vehicle> data = FXCollections.observableArrayList(vehicles);
            vehicleTable.setItems(data);
        } else {
            vehicleTable.getItems().clear();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải danh sách xe. Kiểm tra kết nối CSDL và log.");
        }
    }

    @FXML
    private void onAdd(ActionEvent event) {
        openVehicleAddEditDialog(null); // Mở dialog thêm mới
    }

    @FXML
    private void onUpdate(ActionEvent event) {
        Vehicle selectedVehicle = vehicleTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn một xe để cập nhật.");
            return;
        }
        openVehicleAddEditDialog(selectedVehicle); // Mở dialog sửa
    }

    @FXML
    private void onDelete(ActionEvent event) {
        Vehicle selectedVehicle = vehicleTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn một xe để xóa.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận xóa");
        confirm.setHeaderText("Xóa Xe Biển số " + selectedVehicle.getLicensePlate());
        confirm.setContentText("Bạn có chắc chắn muốn xóa xe này?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = vehicleModel.deleteVehicle(selectedVehicle.getId());
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã xóa xe.");
                onReloadTable();
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xóa xe.");
            }
        }
    }
    
    

    // --- Phương thức để mở dialog Thêm/Sửa Xe ---
    private void openVehicleAddEditDialog(Vehicle vehicleToEdit) {
        Dialog<Vehicle> dialog = new Dialog<>();
        dialog.setTitle(vehicleToEdit == null ? "Thêm Xe mới" : "Sửa Xe");

        // Tạo layout cho dialog (sử dụng GridPane)
        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        // Các TextField cho input
        TextField tfOwnerPersonId = new TextField();
        TextField tfBrand = new TextField();
        TextField tfModel = new TextField();
        TextField tfLicensePlate = new TextField();
        TextField tfColor = new TextField();
        TextField tfImagePath = new TextField();

        // Đặt giá trị cho các trường nếu ở chế độ SỬA
        if (vehicleToEdit != null) {
            tfOwnerPersonId.setText(String.valueOf(vehicleToEdit.getOwnerPersonId()));
            tfBrand.setText(vehicleToEdit.getBrand());
            tfModel.setText(vehicleToEdit.getModel());
            tfLicensePlate.setText(vehicleToEdit.getLicensePlate());
            tfColor.setText(vehicleToEdit.getColor());
            tfImagePath.setText(vehicleToEdit.getImagePath());
            tfLicensePlate.setEditable(false); // Biển số thường không sửa khi update
            tfLicensePlate.setDisable(true);
        }

        // Thêm các Label và TextField vào GridPane
        grid.add(new Label("ID Chủ sở hữu:"), 0, 0); grid.add(tfOwnerPersonId, 1, 0);
        grid.add(new Label("Hãng xe:"), 0, 1);       grid.add(tfBrand, 1, 1);
        grid.add(new Label("Mẫu xe:"), 0, 2);        grid.add(tfModel, 1, 2);
        grid.add(new Label("Biển số:"), 0, 3);       grid.add(tfLicensePlate, 1, 3);
        grid.add(new Label("Màu sắc:"), 0, 4);       grid.add(tfColor, 1, 4);
        grid.add(new Label("Đường dẫn ảnh:"), 0, 5); grid.add(tfImagePath, 1, 5);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Xử lý kết quả dialog
        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                try {
                    Vehicle resultVehicle = (vehicleToEdit != null) ? vehicleToEdit : new Vehicle(); // Dùng lại hoặc tạo mới
                    resultVehicle.setOwnerPersonId(Integer.parseInt(tfOwnerPersonId.getText().trim()));
                    resultVehicle.setBrand(tfBrand.getText());
                    resultVehicle.setModel(tfModel.getText());
                    resultVehicle.setLicensePlate(tfLicensePlate.getText());
                    resultVehicle.setColor(tfColor.getText());
                    resultVehicle.setImagePath(tfImagePath.getText());
                    return resultVehicle;
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu", "ID Chủ sở hữu không hợp lệ (phải là số).");
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Lỗi", "Đã xảy ra lỗi khi xử lý dữ liệu: " + e.getMessage());
                }
            }
            return null;
        });

        // Hiển thị dialog và xử lý kết quả
        Optional<Vehicle> result = dialog.showAndWait();
        result.ifPresent(item -> {
            boolean success;
            if (vehicleToEdit == null) { // Chế độ thêm
                success = vehicleModel.addVehicle(item);
            } else { // Chế độ sửa
                success = vehicleModel.updateVehicle(item);
            }
            
            if (success) {
                onReloadTable(); // Tải lại bảng sau khi thao tác thành công
                showAlert(Alert.AlertType.INFORMATION, "Thành công", (vehicleToEdit == null ? "Đã thêm" : "Đã cập nhật") + " xe.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", (vehicleToEdit == null ? "Không thể thêm" : "Không thể cập nhật") + " xe. Kiểm tra thông tin hoặc biển số xe trùng lặp.");
            }
        });
    }

    private void showVehicleDetailDialog(Vehicle vehicle) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Chi tiết Xe");
        alert.setHeaderText("Thông tin Xe: " + vehicle.getBrand() + " " + vehicle.getModel());

        String details = "ID: " + vehicle.getId() + "\n" +
                         "ID Chủ sở hữu: " + vehicle.getOwnerPersonId() + "\n" +
                         "Hãng xe: " + vehicle.getBrand() + "\n" +
                         "Mẫu xe: " + vehicle.getModel() + "\n" +
                         "Biển số: " + vehicle.getLicensePlate() + "\n" +
                         "Màu sắc: " + vehicle.getColor() + "\n" +
                         "Đường dẫn ảnh: " + vehicle.getImagePath() + "\n" +
                         "Ngày tạo: " + (vehicle.getCreatedAt() != null ? dateFormat.format(vehicle.getCreatedAt()) : "N/A") + "\n" +
                         "Ngày cập nhật: " + (vehicle.getUpdatedAt() != null ? dateFormat.format(vehicle.getUpdatedAt()) : "N/A");
        
        alert.setContentText(details);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}