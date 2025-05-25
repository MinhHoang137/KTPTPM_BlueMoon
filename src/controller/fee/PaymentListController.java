package controller.fee;

import entity.fee.FeeItem;
import entity.fee.Payment;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
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

public class PaymentListController implements Initializable {

    @FXML private TableView<Payment> tablePayments;
    @FXML private TextField tfSearch;
    @FXML private Button btnSearchByHouseholdId;
    @FXML private Button btnSearchByFeeItemId;
    @FXML private Button btnReload;
    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnBack;
    @FXML private TableColumn<Payment, Integer> colId;
    @FXML private TableColumn<Payment, Integer> colHouseholdId;
    @FXML private TableColumn<Payment, Integer> colFeeItemId;
    @FXML private TableColumn<Payment, Double> colAmountPaid;
    @FXML private TableColumn<Payment, Date> colPaymentDate;
    @FXML private TableColumn<Payment, String> colStatus;
    @FXML private TableColumn<Payment, Void> colDetail;

    private PaymentModel paymentModel;
    private FeeModel feeModel;
    private HomePageController homePageController;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public void setHomePageController(HomePageController homePageController) {
        this.homePageController = homePageController;
    }

    public PaymentListController() {
        FeeRepositoryImpl feeRepo = new FeeRepositoryImpl();
        PaymentRepositoryImpl paymentRepo = new PaymentRepositoryImpl(feeRepo);
        paymentModel = new PaymentModel(paymentRepo, feeRepo);
        feeModel = new FeeModel(feeRepo, paymentRepo);
    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        reloadTable();
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colHouseholdId.setCellValueFactory(new PropertyValueFactory<>("householdId"));
        colFeeItemId.setCellValueFactory(data -> {
            FeeItem feeItem = data.getValue().getFeeItem();
            return new ReadOnlyObjectWrapper<>(feeItem != null ? feeItem.getId() : null);
        });
        colAmountPaid.setCellValueFactory(new PropertyValueFactory<>("amountPaid"));
        colPaymentDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        colDetail.setCellFactory(col -> new TableCell<Payment, Void>() {
            private final Button btn = new Button("Chi tiết");
            {
                btn.setOnAction(event -> {
                    Payment item = getTableView().getItems().get(getIndex());
                    showPaymentDetailDialog(item);
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
    private void reloadTable() {
        List<Payment> payments = paymentModel.getAllPayments();
        if (payments != null) {
            ObservableList<Payment> data = FXCollections.observableArrayList(payments);
            tablePayments.setItems(data);
        } else {
            tablePayments.getItems().clear();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải danh sách khoản nộp. Kiểm tra kết nối CSDL và log.");
        }
    }

    @FXML
    private void onAddClicked() {
        openPaymentAddEditDialog(null);
    }

    @FXML
    private void onUpdateClicked() {
        Payment selected = tablePayments.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn khoản nộp để sửa.");
            return;
        }
        openPaymentAddEditDialog(selected);
    }

    @FXML
    public void onDeleteClicked() {
        Payment selected = tablePayments.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn khoản nộp để xóa.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận xóa");
        confirm.setHeaderText("Bạn có chắc muốn xóa khoản nộp ID " + selected.getId() + "?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = paymentModel.deletePayment(selected.getId());
            if (success) {
                reloadTable();
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã xóa khoản nộp.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xóa khoản nộp.");
            }
        }
    }

    @FXML
    private void onSearchByHouseholdId(ActionEvent event) {
        String idText = tfSearch.getText().trim();
        if (idText.isEmpty()) {
            reloadTable();
            return;
        }
        try {
            int householdId = Integer.parseInt(idText);
            List<Payment> payments = paymentModel.getPaymentsByHouseholdId(householdId);
            if (payments != null && !payments.isEmpty()) {
                tablePayments.setItems(FXCollections.observableArrayList(payments));
            } else {
                tablePayments.getItems().clear();
                showAlert(Alert.AlertType.INFORMATION, "Thông báo", "Không tìm thấy khoản nộp cho hộ khẩu ID: " + householdId);
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu", "ID Hộ khẩu phải là một số nguyên.");
        }
    }

    @FXML
    private void onSearchByFeeItemId(ActionEvent event) {
        String idText = tfSearch.getText().trim();
        if (idText.isEmpty()) {
            reloadTable();
            return;
        }
        try {
            int feeItemId = Integer.parseInt(idText);
            List<Payment> payments = paymentModel.getPaymentsByFeeItemId(feeItemId);
            if (payments != null && !payments.isEmpty()) {
                tablePayments.setItems(FXCollections.observableArrayList(payments));
            } else {
                tablePayments.getItems().clear();
                showAlert(Alert.AlertType.INFORMATION, "Thông báo", "Không tìm thấy khoản nộp cho khoản thu ID: " + feeItemId);
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu", "ID Khoản thu phải là một số nguyên.");
        }
    }

    @FXML
    public void onBackClicked() {
        if (homePageController != null) {
            homePageController.loadContentIntoCenter("/resources/view/fee/feeManager.fxml");
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể quay lại: HomePageController không được thiết lập.");
            Stage stage = (Stage) btnBack.getScene().getWindow();
            if (stage != null) {
                stage.close();
            }
        }
    }

    private void openPaymentAddEditDialog(Payment paymentToEdit) {
        Dialog<Payment> dialog = new Dialog<>();
        dialog.setTitle(paymentToEdit == null ? "Thêm Khoản nộp mới" : "Sửa Khoản nộp");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField tfHouseholdId = new TextField();
        TextField tfFeeItemId = new TextField();
        TextField tfAmountPaid = new TextField();
        DatePicker dpPaymentDate = new DatePicker();
        TextField tfStatus = new TextField();

        if (paymentToEdit != null) {
            tfHouseholdId.setText(String.valueOf(paymentToEdit.getHouseholdId()));
            tfFeeItemId.setText(String.valueOf(paymentToEdit.getFeeItem().getId()));
            tfAmountPaid.setText(String.valueOf(paymentToEdit.getAmountPaid()));
            if (paymentToEdit.getPaymentDate() != null) {
                Date date = paymentToEdit.getPaymentDate();
                if (date instanceof java.sql.Date) {
                    dpPaymentDate.setValue(((java.sql.Date) date).toLocalDate());
                } else {
                    dpPaymentDate.setValue(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                }
            }
            tfStatus.setText(paymentToEdit.getStatus());
            tfHouseholdId.setEditable(false);
            tfHouseholdId.setDisable(true);
            tfFeeItemId.setEditable(false);
            tfFeeItemId.setDisable(true);
        } else {
            dpPaymentDate.setValue(LocalDate.now());
            tfStatus.setText("Chưa nộp");
        }

        grid.add(new Label("ID Hộ khẩu:"), 0, 0);
        grid.add(tfHouseholdId, 1, 0);
        grid.add(new Label("ID Khoản thu:"), 0, 1);
        grid.add(tfFeeItemId, 1, 1);
        grid.add(new Label("Số tiền nộp:"), 0, 2);
        grid.add(tfAmountPaid, 1, 2);
        grid.add(new Label("Ngày nộp:"), 0, 3);
        grid.add(dpPaymentDate, 1, 3);
        grid.add(new Label("Trạng thái:"), 0, 4);
        grid.add(tfStatus, 1, 4);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                try {
                    Payment resultPayment = (paymentToEdit != null) ? paymentToEdit : new Payment();
                    resultPayment.setHouseholdId(Integer.parseInt(tfHouseholdId.getText().trim()));
                    resultPayment.setAmountPaid(Double.parseDouble(tfAmountPaid.getText().trim()));
                    resultPayment.setPaymentDate(Date.from(dpPaymentDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    resultPayment.setStatus(tfStatus.getText());

                    FeeItem feeItem = feeModel.getFeeItemById(Integer.parseInt(tfFeeItemId.getText().trim()));
                    if (feeItem == null) {
                        showAlert(Alert.AlertType.ERROR, "Lỗi", "Không tìm thấy Khoản thu với ID đã nhập.");
                        return null;
                    }
                    resultPayment.setFeeItem(feeItem);
                    return resultPayment;
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu", "ID hoặc số tiền không hợp lệ.");
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Lỗi", "Đã xảy ra lỗi khi xử lý dữ liệu: " + e.getMessage());
                }
            }
            return null;
        });

        Optional<Payment> result = dialog.showAndWait();
        result.ifPresent(item -> {
            boolean success = (paymentToEdit == null)
                    ? paymentModel.createPayment(item)
                    : paymentModel.updatePayment(item);
            if (success) {
                reloadTable();
                showAlert(Alert.AlertType.INFORMATION, "Thành công", (paymentToEdit == null ? "Đã thêm" : "Đã cập nhật") + " khoản nộp.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", (paymentToEdit == null ? "Không thể thêm" : "Không thể cập nhật") + " khoản nộp.");
            }
        });
    }

    private void showPaymentDetailDialog(Payment payment) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Chi tiết Khoản nộp");
        alert.setHeaderText("Thông tin Khoản nộp ID: " + payment.getId());

        String feeItemName = (payment.getFeeItem() != null) ? payment.getFeeItem().getFeeName() : "Không xác định";

        String details = "ID: " + payment.getId() + "\n" +
                "ID Hộ khẩu: " + payment.getHouseholdId() + "\n" +
                "Khoản thu: " + feeItemName + " (ID: " + payment.getFeeItem().getId() + ")\n" +
                "Số tiền nộp: " + payment.getAmountPaid() + "\n" +
                "Ngày nộp: " + (payment.getPaymentDate() != null ? dateFormat.format(payment.getPaymentDate()) : "Chưa nộp") + "\n" +
                "Trạng thái: " + payment.getStatus();

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
