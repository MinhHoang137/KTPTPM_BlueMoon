package view_controller.household;

import controller.household.HouseholdController;
import entity.resident.Household;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import view_controller.BaseView;
import view_controller.ViewController;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class HouseholdView extends BaseView {

    @FXML private TextField tfSearch;
    @FXML private Button btnSearchById, btnSearchByNumber, btnAdd, btnUpdate, btnDelete;
    @FXML private TableView<Household> householdTable;
    @FXML private TableColumn<Household, Integer> colId;
    @FXML private TableColumn<Household, String> colHouseholdNumber;
    @FXML private TableColumn<Household, String> colOwnerId;
//    @FXML private TableColumn<Household, String> colOwnerName;
    @FXML private TableColumn<Household, java.util.Date> colRegistrationDate;

    private static HouseholdView householdView;
    public static HouseholdView getCurrentView() {
        return householdView;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)  {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colHouseholdNumber.setCellValueFactory(new PropertyValueFactory<>("householdNumber"));
        colOwnerId.setCellValueFactory(new PropertyValueFactory<>("ownerId"));
        colRegistrationDate.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
        try {
            List<Household> households = HouseholdController.getInstance().getAllHouseholds();
            ObservableList<Household> data = FXCollections.observableArrayList(households);
            householdTable.setItems(data);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getFxmlPath() {
        return "/household/household.fxml";
    }

    @FXML private void onSearchById(ActionEvent e) throws SQLException {
        String idText = tfSearch.getText().trim();
        if (idText.isEmpty()) {
            List<Household> households = HouseholdController.getInstance().getAllHouseholds();
            ObservableList<Household> data = FXCollections.observableArrayList(households);
            householdTable.setItems(data);
            return;
        }
        int id = Integer.parseInt(idText);
        try {
            Household household = HouseholdController.getInstance().getHouseholdById(id);
            if (household != null) {
                ObservableList<Household> data = FXCollections.observableArrayList(household);
                householdTable.setItems(data);
            } else {
                householdTable.getItems().clear();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    @FXML private void onSearchByNumber(ActionEvent e) {
        String householdNumber = tfSearch.getText().trim();
        if (householdNumber.isEmpty()) {
            return;
        }
        try {
            Household household = HouseholdController.getInstance().getHouseholdByNumber(householdNumber);
            if (household != null) {
                ObservableList<Household> data = FXCollections.observableArrayList(household);
                householdTable.setItems(data);
            } else {
                householdTable.getItems().clear();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    @FXML private void onAdd(ActionEvent e) {
        ViewController.getInstance().openView(new HouseholdAddView(), "Thêm hộ gia đình", 1000, 700);
    }
    @FXML private void onUpdate(ActionEvent e) {
        ViewController.getInstance().openView(new HouseholdUpdateView(), "Cập nhật hộ gia đình", 700, 400);
    }
    @FXML private void onDelete(ActionEvent e) {
        ViewController.getInstance().openView(new HouseholdDeleteView(), "Xóa hộ gia đình", 1000, 700);
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
}
