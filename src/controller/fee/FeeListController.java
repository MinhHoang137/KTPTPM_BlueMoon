package controller.fee;

import entity.fee.FeeItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import model.fee.FeeModel;
import model.fee.PaymentModel;
import repository.fee.FeeRepositoryImpl;
import repository.fee.PaymentRepositoryImpl;
import controller.user.HomePageController;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class FeeListController implements Initializable {

    @FXML private TableView<FeeItem> tableFeeList;

    @FXML private TextField tfSearch;
    @FXML private Button btnSearchById;
    @FXML private Button btnSearchByName;

    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnBack;

    @FXML private TableColumn<FeeItem, Integer> colId;
    @FXML private TableColumn<FeeItem, String> colFeeName;
    @FXML private TableColumn<FeeItem, Double> colAmount;
    @FXML private TableColumn<FeeItem, String> colDescription;
    @FXML private TableColumn<FeeItem, Date> colStartDate;
    @FXML private TableColumn<FeeItem, Date> colEndDate;
    @FXML private TableColumn<FeeItem, String> colStatus;
    @FXML private TableColumn<FeeItem, Void> colDetail;

    private FeeModel feeModel;
    private PaymentModel paymentModel;
    private HomePageController homePageController;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public void setHomePageController(HomePageController homePageController) {
        this.homePageController = homePageController;
    }

    public FeeListController() {
        FeeRepositoryImpl feeRepo = new FeeRepositoryImpl();
        PaymentRepositoryImpl paymentRepo = new PaymentRepositoryImpl(feeRepo); 
        feeModel = new FeeModel(feeRepo, paymentRepo);
        paymentModel = new PaymentModel(paymentRepo, feeRepo);
    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        reloadTable();
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFeeName.setCellValueFactory(new PropertyValueFactory<>("feeName"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        colDetail.setCellFactory(col -> new TableCell<FeeItem, Void>() {
            private final Button btn = new Button("Chi tiết");
            {
                btn.setOnAction(event -> {
                    FeeItem item = getTableView().getItems().get(getIndex());
                    showFeeDetailDialog(item);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    public void reloadTable() {
        List<FeeItem> feeItems = feeModel.getAllFeeItems();
        if (feeItems != null) {
            ObservableList<FeeItem> data = FXCollections.observableArrayList(feeItems);
            tableFeeList.setItems(data);
        } else {
            tableFeeList.getItems().clear();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải danh sách khoản thu. Kiểm tra kết nối CSDL và log.");
        }
    }

    @FXML
    public void onAddClicked() {
        openFeeAddEditDialog(null);
    }

    @FXML
    public void onEditClicked() {
        FeeItem selected = tableFeeList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn khoản thu để sửa.");
            return;
        }
        openFeeAddEditDialog(selected);
    }

    @FXML
    public void onDeleteClicked() {
        FeeItem selected = tableFeeList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn khoản thu để xóa.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận xóa");
        confirm.setHeaderText("Bạn có chắc muốn xóa khoản thu '" + selected.getFeeName() + "'?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = true;
            feeModel.deleteFeeItem(selected.getId());
            if (success) {
                reloadTable();
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã xóa khoản thu.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xóa khoản thu.");
            }
        }
    }

    @FXML
    public void onSearchById(ActionEvent event) {
        String idText = tfSearch.getText().trim();
        if (idText.isEmpty()) {
            reloadTable();
            return;
        }
        try {
            int id = Integer.parseInt(idText);
            FeeItem feeItem = feeModel.getFeeItemById(id);
            if (feeItem != null) {
                ObservableList<FeeItem> data = FXCollections.observableArrayList(feeItem);
                tableFeeList.setItems(data);
            } else {
                tableFeeList.getItems().clear();
                showAlert(Alert.AlertType.INFORMATION, "Thông báo", "Không tìm thấy khoản thu với ID: " + id);
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu", "ID phải là một số nguyên.");
        }
    }

    @FXML
    public void onSearchByName(ActionEvent event) {
        String name = tfSearch.getText().trim();
        if (name.isEmpty()) {
            reloadTable();
            return;
        }
        List<FeeItem> allItems = feeModel.getAllFeeItems();
        if (allItems != null) {
            ObservableList<FeeItem> filteredData = allItems.stream()
                .filter(item -> item.getFeeName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
            tableFeeList.setItems(filteredData);
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tìm kiếm: Lỗi tải dữ liệu gốc.");
        }
    }

    @FXML
    public void onBackClicked() {
        if (homePageController != null) {
            homePageController.loadContentIntoCenter("/resources/view/fee/FeeManager.fxml");
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể quay lại: HomePageController không được thiết lập.");
            Stage stage = (Stage) btnBack.getScene().getWindow();
            if (stage != null) {
                 stage.close();
            }
        }
    }

    private void openFeeAddEditDialog(FeeItem feeItemToEdit) {
        Dialog<FeeItem> dialog = new Dialog<>();
        dialog.setTitle(feeItemToEdit == null ? "Thêm Khoản thu mới" : "Sửa Khoản thu");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        TextField amountField = new TextField();
        TextField descriptionField = new TextField();
        DatePicker startDatePicker = new DatePicker();
        DatePicker endDatePicker = new DatePicker();
        TextField statusField = new TextField();

        if (feeItemToEdit != null) {
            nameField.setText(feeItemToEdit.getFeeName());
            amountField.setText(String.valueOf(feeItemToEdit.getAmount()));
            descriptionField.setText(feeItemToEdit.getDescription());
            
            // Sửa lỗi: Chuyển đổi java.sql.Date sang java.util.Date trước khi gọi toInstant()
            if (feeItemToEdit.getStartDate() != null) {
                Date utilStartDate = new Date(feeItemToEdit.getStartDate().getTime());
                startDatePicker.setValue(utilStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            }
            if (feeItemToEdit.getEndDate() != null) {
                Date utilEndDate = new Date(feeItemToEdit.getEndDate().getTime());
                endDatePicker.setValue(utilEndDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            }
            statusField.setText(feeItemToEdit.getStatus());
        } else {
            startDatePicker.setValue(LocalDate.now());
            endDatePicker.setValue(LocalDate.now().plusMonths(1));
            statusField.setText("Đang thu");
        }

        grid.add(new Label("Tên Khoản thu:"), 0, 0);        grid.add(nameField, 1, 0);
        grid.add(new Label("Số tiền:"), 0, 1);              grid.add(amountField, 1, 1);
        grid.add(new Label("Mô tả:"), 0, 2);                grid.add(descriptionField, 1, 2);
        grid.add(new Label("Ngày bắt đầu (yyyy-MM-dd):"), 0, 3); grid.add(startDatePicker, 1, 3);
        grid.add(new Label("Ngày kết thúc (yyyy-MM-dd):"), 0, 4); grid.add(endDatePicker, 1, 4);
        grid.add(new Label("Trạng thái:"), 0, 5);           grid.add(statusField, 1, 5);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                try {
                    FeeItem resultItem = (feeItemToEdit != null) ? feeItemToEdit : new FeeItem();
                    resultItem.setFeeName(nameField.getText());
                    resultItem.setAmount(Double.parseDouble(amountField.getText()));
                    resultItem.setDescription(descriptionField.getText());
                    
                    resultItem.setStartDate(Date.from(startDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    resultItem.setEndDate(Date.from(endDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    
                    resultItem.setStatus(statusField.getText());
                    return resultItem;
                } catch (NumberFormatException | NullPointerException e) {
                    showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu", "Vui lòng nhập đúng định dạng số tiền và chọn ngày.");
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Lỗi", "Đã xảy ra lỗi khi xử lý dữ liệu: " + e.getMessage());
                }
            }
            return null;
        });

        Optional<FeeItem> result = dialog.showAndWait();
        result.ifPresent(item -> {
            boolean success = false; // Khởi tạo biến success
            try {
                if (feeItemToEdit == null) {
                    success = true;
                    feeModel.createFeeItem(item); // Gán kết quả từ addFeeItem
                } else {
                    success = true;
                    feeModel.updateFeeItem(item); // Gán kết quả từ updateFeeItem
                }
                
                if (success) { // Kiểm tra biến success
                    reloadTable();
                    showAlert(Alert.AlertType.INFORMATION, "Thành công", (feeItemToEdit == null ? "Đã thêm" : "Đã cập nhật") + " khoản thu.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Lỗi", (feeItemToEdit == null ? "Không thể thêm" : "Không thể cập nhật") + " khoản thu. Có thể tên khoản thu đã tồn tại hoặc lỗi CSDL.");
                }
            } catch (Exception e) { 
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể thực hiện thao tác: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private void showFeeDetailDialog(FeeItem feeItem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Chi tiết Khoản thu");
        alert.setHeaderText("Thông tin Khoản thu: " + feeItem.getFeeName());

        String details = "ID: " + feeItem.getId() + "\n" +
                         "Tên: " + feeItem.getFeeName() + "\n" +
                         "Số tiền: " + feeItem.getAmount() + "\n" +
                         "Mô tả: " + feeItem.getDescription() + "\n" +
                         "Bắt đầu: " + (feeItem.getStartDate() != null ? dateFormat.format(feeItem.getStartDate()) : "N/A") + "\n" +
                         "Kết thúc: " + (feeItem.getEndDate() != null ? dateFormat.format(feeItem.getEndDate()) : "N/A") + "\n" +
                         "Trạng thái: " + feeItem.getStatus();
        try {
            double totalExpected = feeModel.getTotalExpectedForFeeItem(feeItem.getId());
            double totalPaid = feeModel.getTotalPaidForFeeItem(feeItem.getId());
            double totalMissing = feeModel.getTotalMissingForFeeItem(feeItem.getId());
     
            details += "\n\nTổng cần thu: " + totalExpected + "\nĐã thu: " + totalPaid + "\nCòn thiếu: " + totalMissing;

            alert.setContentText(details);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi tính toántoán");

        }
        
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}