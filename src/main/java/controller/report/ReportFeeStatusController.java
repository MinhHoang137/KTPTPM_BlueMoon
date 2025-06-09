package controller.report;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import repository.report.ReportRepository;

import java.util.List;
import java.util.Map;

public class ReportFeeStatusController {

    @FXML private Button paidBtn;
    @FXML private Button unpaidBtn;
    @FXML private TableView<StatusRow> statusTable;
    @FXML private TableColumn<StatusRow, String> statusCol;
    @FXML private TableColumn<StatusRow, String> householdCol;

    private final ReportRepository repo = new ReportRepository();

    @FXML
    public void initialize() {
        statusCol.setCellValueFactory(data -> data.getValue().statusProperty());
        householdCol.setCellValueFactory(data -> data.getValue().householdProperty());
        // Default show “Chưa nộp”
        showUnpaid();
        unpaidBtn.setOnAction(e -> showUnpaid());
        paidBtn.setOnAction(e -> showPaid());
    }

    private void showPaid() {
        loadTable("Đã nộp");
    }

    private void showUnpaid() {
        loadTable("Chưa nộp");
    }

    private void loadTable(String key) {
        statusTable.getItems().clear();
        Map<String, List<String>> all = repo.getHouseholdPaymentStatusLists();
        List<String> list = all.get(key);
        ObservableList<StatusRow> data = FXCollections.observableArrayList();
        if (list != null) {
            for (String hh : list) {
                data.add(new StatusRow(key, hh));
            }
        }
        statusTable.setItems(data);
    }

    public static class StatusRow {
        private final javafx.beans.property.SimpleStringProperty status;
        private final javafx.beans.property.SimpleStringProperty household;

        public StatusRow(String status, String household) {
            this.status = new javafx.beans.property.SimpleStringProperty(status);
            this.household = new javafx.beans.property.SimpleStringProperty(household);
        }

        public javafx.beans.property.SimpleStringProperty statusProperty() {
            return status;
        }

        public javafx.beans.property.SimpleStringProperty householdProperty() {
            return household;
        }
    }
}
