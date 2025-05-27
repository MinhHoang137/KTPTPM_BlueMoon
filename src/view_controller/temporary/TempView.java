package view_controller.temporary;

import controller.temporary.TempController;
import entity.temporary.TemporaryRegistration;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import view_controller.BaseView;
import view_controller.notification.NotificationView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TempView extends BaseView {

    @FXML private TextField citizenIdField;
    @FXML private Button searchButton, addButton, editButton, deleteButton;

    @FXML private TableView<TemporaryRegistration> tempTable;
    @FXML private TableColumn<TemporaryRegistration, Integer> colId;
    @FXML private TableColumn<TemporaryRegistration, Integer> colPersonId;
    @FXML private TableColumn<TemporaryRegistration, String> colType;
    @FXML private TableColumn<TemporaryRegistration, java.time.LocalDate> colStart;
    @FXML private TableColumn<TemporaryRegistration, java.time.LocalDate> colEnd;
    @FXML private TableColumn<TemporaryRegistration, String> colAddress;
    @FXML private TableColumn<TemporaryRegistration, String> colReason;

    private TempController controller = TempController.getInstance();
    @Override
    public String getFxmlPath() {
        return "/view/temporary/tempView.fxml";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPersonId.setCellValueFactory(new PropertyValueFactory<>("personId"));
        colType.setCellValueFactory(new PropertyValueFactory<>("registrationType"));
        colStart.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colEnd.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("addressOfStay"));
        colReason.setCellValueFactory(new PropertyValueFactory<>("reasonNotes"));

        refreshTable();
    }

    @FXML
    public void onSearch() {
        String citizenId = citizenIdField.getText().trim();
        if (citizenId.isEmpty()) {
            // Hiển thị thông báo lỗi nếu không nhập CCCD
            NotificationView.Create("Vui lòng nhập CCCD để tìm kiếm!");
            refreshTable();
            return;
        } else{
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
        // Hàm để trống, sẽ implement sau
    }

    @FXML
    public void onEdit() {
        // Hàm để trống, sẽ implement sau
    }

    @FXML
    public void onDelete() {
        // Hàm để trống, sẽ implement sau
    }

    private void refreshTable() {
        List<TemporaryRegistration> registrations = controller.findAll();
        tempTable.getItems().setAll(registrations);
    }
}
