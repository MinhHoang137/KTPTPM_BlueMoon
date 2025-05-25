package controller.fee;

import entity.fee.FeeItem;
import entity.fee.Payment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.fee.PaymentModel;
import repository.fee.FeeRepositoryImpl;
import repository.fee.PaymentRepositoryImpl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.List;
import javafx.beans.property.ReadOnlyObjectWrapper;

public class PaymentListController {

    @FXML private TableView<Payment> tablePayments;

    private PaymentModel paymentModel;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @FXML
    public void initialize() {
        paymentModel = new PaymentModel(new PaymentRepositoryImpl(), new FeeRepositoryImpl());
        setupTable();
        reloadTable();
    }

    private void setupTable() {
        TableColumn<Payment, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Payment, Integer> colHo = new TableColumn<>("ID Hộ");
        colHo.setCellValueFactory(new PropertyValueFactory<>("idHousehold"));

        TableColumn<Payment, Double> colSoTien = new TableColumn<>("Số tiền");
        colSoTien.setCellValueFactory(new PropertyValueFactory<>("soTienNop"));

        TableColumn<Payment, Date> colNgayNop = new TableColumn<>("Ngày nộp");
        colNgayNop.setCellValueFactory(new PropertyValueFactory<>("ngayNop"));

        TableColumn<Payment, String> colTrangThai = new TableColumn<>("Trạng thái");
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));

        TableColumn<Payment, Integer> colFeeId = new TableColumn<>("Khoản thu");
        colFeeId.setCellValueFactory(data -> {
            FeeItem feeItem = data.getValue().getFeeItem();
            return new ReadOnlyObjectWrapper<>(feeItem != null ? feeItem.getId() : 0);
        });

        tablePayments.getColumns().addAll(colId, colHo, colSoTien, colNgayNop, colTrangThai, colFeeId);
    }

    private void reloadTable() {
        ObservableList<Payment> data = FXCollections.observableArrayList(paymentModel.getAllPayment());
        tablePayments.setItems(data);
    }

    @FXML
    public void onAddClicked() {
        Dialog<Payment> dialog = new Dialog<>();
        dialog.setTitle("Thêm khoản nộp");

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);

        TextField idHoField = new TextField();
        TextField soTienField = new TextField();
        TextField feeIdField = new TextField();

        grid.add(new Label("ID Hộ:"), 0, 0); grid.add(idHoField, 1, 0);
        grid.add(new Label("Số tiền:"), 0, 1); grid.add(soTienField, 1, 1);
        grid.add(new Label("ID Khoản thu:"), 0, 2); grid.add(feeIdField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    Payment p = new Payment();
                    p.setIdHousehold(Integer.parseInt(idHoField.getText()));
                    p.setSoTienNop(Double.parseDouble(soTienField.getText()));
                    p.setNgayNop(new Date());

                    FeeItem f = new FeeRepositoryImpl().findById(Integer.parseInt(feeIdField.getText()));
                    p.setFeeItem(f);
                    return p;
                } catch (Exception e) {
                    showAlert("Lỗi nhập liệu: " + e.getMessage());
                }
            }
            return null;
        });

        Optional<Payment> result = dialog.showAndWait();
        result.ifPresent(p -> {
            paymentModel.createPayment(p);
            paymentModel.validatePaymentStatusForFeeItem(p.getFeeItem().getId(), p.getFeeItem().getSoTien());
            reloadTable();
            showAlert("Đã thêm khoản nộp.");
        });
    }

    @FXML
    public void onEditClicked() {
        Payment selected = tablePayments.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Vui lòng chọn khoản nộp để sửa.");
            return;
        }

        Dialog<Payment> dialog = new Dialog<>();
        dialog.setTitle("Sửa khoản nộp");

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);

        TextField soTienField = new TextField(String.valueOf(selected.getSoTienNop()));

        grid.add(new Label("Số tiền:"), 0, 0); grid.add(soTienField, 1, 0);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    selected.setSoTienNop(Double.parseDouble(soTienField.getText()));
                    selected.setNgayNop(new Date());
                    return selected;
                } catch (Exception e) {
                    showAlert("Lỗi nhập.");
                }
            }
            return null;
        });

        Optional<Payment> result = dialog.showAndWait();
        result.ifPresent(p -> {
            paymentModel.updatePayment(p);
            paymentModel.validatePaymentStatusForFeeItem(p.getFeeItem().getId(), p.getFeeItem().getSoTien());
            reloadTable();
            showAlert("Đã cập nhật khoản nộp.");
        });
    }

    @FXML
    public void onDeleteClicked() {
        Payment selected = tablePayments.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Chọn khoản nộp để xóa.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setHeaderText("Xác nhận xóa khoản nộp của hộ " + selected.getIdHousehold());
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            paymentModel.deletePayment(selected.getId());
            paymentModel.validatePaymentStatusForFeeItem(selected.getFeeItem().getId(), selected.getFeeItem().getSoTien());
            reloadTable();
            showAlert("Đã xóa.");
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    public void onBackClicked() throws IOException {
        Stage stage = (Stage) tablePayments.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/resources/view/fee/feeManager.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Quản lý phí chung cư");
    }
}
