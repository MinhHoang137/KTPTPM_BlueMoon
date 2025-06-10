package view_controller.temporary;

import controller.temporary.TempController;
import entity.temporary.TemporaryRegistration;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.temporary.TempModel;
import view_controller.BaseView;
import view_controller.ViewController;
import view_controller.notification.NotificationView;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class TempView extends BaseView {
    private static TempView tempView;

    @FXML
    private TextField citizenIdField;
    @FXML
    private Button searchButton, addButton, deleteButton;

    @FXML
    private TableView<TemporaryRegistration> tempTable;
    @FXML
    private TableColumn<TemporaryRegistration, Integer> colId;
    @FXML
    private TableColumn<TemporaryRegistration, Integer> citizenId;
    @FXML
    private TableColumn<TemporaryRegistration, String> colType;
    @FXML
    private TableColumn<TemporaryRegistration, java.time.LocalDate> colStart;
    @FXML
    private TableColumn<TemporaryRegistration, java.time.LocalDate> colEnd;
    @FXML
    private TableColumn<TemporaryRegistration, String> colAddress;
    @FXML
    private TableColumn<TemporaryRegistration, String> colReason;

    private TempController controller = TempController.getInstance();

    @Override
    public String getFxmlPath() {
        return "/resources/temporary/tempView.fxml";
        // return "/temporary/tempView.fxml";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        citizenId.setCellValueFactory(new PropertyValueFactory<>("citizenId"));
        colType.setCellValueFactory(new PropertyValueFactory<>("registrationType"));
        colStart.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colEnd.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("addressOfStay"));
        colReason.setCellValueFactory(new PropertyValueFactory<>("reasonNotes"));

        refreshTable();
    }

    public static TempView getCurrentView() {
        return tempView;
    }

    @Override
    public BaseView loadAndShow(Stage stage, String title, int width, int height) {
        tempView = (TempView) super.loadAndShow(stage, title, width, height);
        return tempView;
    }

    @FXML
    public void onSearch() {
        String citizenId = citizenIdField.getText().trim();
        if (citizenId.isEmpty()) {
            // Hiển thị thông báo lỗi nếu không nhập CCCD
            NotificationView.Create("Vui lòng nhập CCCD để tìm kiếm!");
            refreshTable();
            return;
        } else {
            List<TemporaryRegistration> results = controller.findByCitizenId(citizenId);
            if (results.isEmpty()) {
                NotificationView.Create("Không tìm thấy đăng ký tạm trú cho CCCD: " + citizenId);
                refreshTable();
            } else {
                tempTable.getItems().setAll(results);
            }
        }
    }

    @FXML
    public void onAdd() {
        ViewController.getInstance().openView(new TempAddView(), "Thêm đăng ký tạm trú/tạm vắng", 700, 400);
    }

    @FXML
    public void onDelete() {
        TemporaryRegistration selectedRegistration = tempTable.getSelectionModel().getSelectedItem();
        if (selectedRegistration == null) {
            NotificationView.Create("Vui lòng chọn đăng ký tạm trú/tạm vắng để xóa.");
            return;
        }

        // Create confirmation dialog
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác nhận xóa");
        confirmAlert.setHeaderText("Xóa đăng ký tạm trú/tạm vắng");
        confirmAlert.setContentText("Bạn có chắc chắn muốn xóa đăng ký của CCCD: " +
                selectedRegistration.getCitizenId() +
                " (Loại: " + selectedRegistration.getRegistrationType() + ")?");

        // Add custom button types
        ButtonType buttonTypeYes = new ButtonType("Có", ButtonBar.ButtonData.YES);
        ButtonType buttonTypeNo = new ButtonType("Không", ButtonBar.ButtonData.NO);
        confirmAlert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == buttonTypeYes) {
            try {
                TemporaryRegistration registration = controller.findById(selectedRegistration.getId());
                boolean deleteResult = TempModel.getInstance().delete(registration);

                if (deleteResult) {
                    NotificationView.Create("Xóa đăng ký tạm trú/tạm vắng thành công!");
                    refreshTable(); // Refresh the table after successful deletion
                } else {
                    NotificationView.Create("Xóa đăng ký tạm trú/tạm vắng thất bại!");
                }
            } catch (Exception e) {
                NotificationView.Create("Lỗi khi xóa đăng ký: " + e.getMessage());
            }
        }
    }

    public void refreshTable() {
        List<TemporaryRegistration> registrations = controller.findAll();
        tempTable.getItems().setAll(registrations);
    }
}
