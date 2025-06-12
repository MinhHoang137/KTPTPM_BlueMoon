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
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import model.fee.FeeModel;
import model.fee.PaymentModel;
import repository.fee.FeeRepositoryImpl;
import repository.fee.PaymentRepositoryImpl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class FeeListController {

    @FXML
    private TableView<FeeItem> tableFeeList;

    private FeeModel feeModel;
    private PaymentModel paymentModel;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @FXML
    public void initialize() {
        feeModel = new FeeModel(new FeeRepositoryImpl(), new PaymentRepositoryImpl());
        paymentModel = new PaymentModel(new PaymentRepositoryImpl(), new FeeRepositoryImpl());
        setupTable();
        reloadTable();
    }

    private void setupTable() {
        TableColumn<FeeItem, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<FeeItem, String> colTen = new TableColumn<>("Tên khoản thu");
        colTen.setCellValueFactory(new PropertyValueFactory<>("tenKhoanThu"));

        TableColumn<FeeItem, Double> colTien = new TableColumn<>("Số tiền");
        colTien.setCellValueFactory(new PropertyValueFactory<>("soTien"));

        TableColumn<FeeItem, String> colMoTa = new TableColumn<>("Mô tả");
        colMoTa.setCellValueFactory(new PropertyValueFactory<>("moTa"));

        TableColumn<FeeItem, Date> colNgayBD = new TableColumn<>("Ngày bắt đầu");
        colNgayBD.setCellValueFactory(new PropertyValueFactory<>("ngayBatDau"));

        TableColumn<FeeItem, Date> colNgayKT = new TableColumn<>("Ngày kết thúc");
        colNgayKT.setCellValueFactory(new PropertyValueFactory<>("ngayKetThuc"));

        TableColumn<FeeItem, String> colTrangThai = new TableColumn<>("Trạng thái");
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));

        TableColumn<FeeItem, Void> colDetail = new TableColumn<>("Chi tiết");
        colDetail.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Chi tiết");

            {
                btn.setOnAction(event -> {
                    FeeItem item = getTableView().getItems().get(getIndex());
                    showDetailDialog(item);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        tableFeeList.getColumns().addAll(colId, colTen, colTien, colMoTa, colNgayBD, colNgayKT, colTrangThai, colDetail);
    }

    private void reloadTable() {
        ObservableList<FeeItem> data = FXCollections.observableArrayList(feeModel.getAllFees());
        tableFeeList.setItems(data);
    }

    @FXML
    public void onAddClicked() {
        Dialog<FeeItem> dialog = new Dialog<>();
        dialog.setTitle("Thêm khoản thu mới");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField tenField = new TextField();
        TextField soTienField = new TextField();
        TextField moTaField = new TextField();
    
        // Sử dụng DatePicker cho Ngày bắt đầu và Ngày kết thúc
        DatePicker ngayBDField = new DatePicker();
        DatePicker ngayKTField = new DatePicker();
    
        TextField trangThaiField = new TextField();

        grid.add(new Label("Tên khoản thu:"), 0, 0);        grid.add(tenField, 1, 0);
        grid.add(new Label("Số tiền:"), 0, 1);              grid.add(soTienField, 1, 1);
        grid.add(new Label("Mô tả:"), 0, 2);                grid.add(moTaField, 1, 2);
        grid.add(new Label("Ngày bắt đầu:"), 0, 3);          grid.add(ngayBDField, 1, 3);
        grid.add(new Label("Ngày kết thúc:"), 0, 4);         grid.add(ngayKTField, 1, 4);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                try {
                    FeeItem feeItem = new FeeItem();
                    feeItem.setTenKhoanThu(tenField.getText());
                    feeItem.setSoTien(Double.parseDouble(soTienField.getText()));
                    feeItem.setMoTa(moTaField.getText());

                    // Lấy ngày từ DatePicker và chuyển thành Date
                    if (ngayBDField.getValue() != null && ngayKTField.getValue() != null) {
                        feeItem.setNgayBatDau(java.sql.Date.valueOf(ngayBDField.getValue()));
                        feeItem.setNgayKetThuc(java.sql.Date.valueOf(ngayKTField.getValue()));
                    }

                    feeItem.setTrangThai("Dang thu");
                    return feeItem;
                } catch (Exception e) {
                    showAlert("Lỗi nhập liệu: " + e.getMessage());
                }
            }
            return null;
        });

        Optional<FeeItem> result = dialog.showAndWait();
        result.ifPresent(item -> {
            feeModel.createFee(item);
            reloadTable();
            showAlert("Đã thêm khoản thu mới.");
        });
    }


    @FXML
    public void onEditClicked() {
        FeeItem selected = tableFeeList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Vui lòng chọn khoản thu để sửa.");
            return;
        }

        Dialog<FeeItem> dialog = new Dialog<>();
        dialog.setTitle("Sửa khoản thu");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField tenField = new TextField(selected.getTenKhoanThu());
        TextField soTienField = new TextField(String.valueOf(selected.getSoTien()));
        TextField moTaField = new TextField(selected.getMoTa());
        TextField ngayBDField = new TextField(dateFormat.format(selected.getNgayBatDau()));
        TextField ngayKTField = new TextField(dateFormat.format(selected.getNgayKetThuc()));
        TextField trangThaiField = new TextField(selected.getTrangThai());

        grid.add(new Label("Tên khoản thu:"), 0, 0);        grid.add(tenField, 1, 0);
        grid.add(new Label("Số tiền:"), 0, 1);              grid.add(soTienField, 1, 1);
        grid.add(new Label("Mô tả:"), 0, 2);                grid.add(moTaField, 1, 2);
        grid.add(new Label("Ngày bắt đầu (yyyy-MM-dd):"), 0, 3); grid.add(ngayBDField, 1, 3);
        grid.add(new Label("Ngày kết thúc (yyyy-MM-dd):"), 0, 4); grid.add(ngayKTField, 1, 4);
        grid.add(new Label("Trạng thái:"), 0, 5);           grid.add(trangThaiField, 1, 5);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                try {
                    selected.setTenKhoanThu(tenField.getText());
                    selected.setSoTien(Double.parseDouble(soTienField.getText()));
                    selected.setMoTa(moTaField.getText());
                    selected.setNgayBatDau(dateFormat.parse(ngayBDField.getText()));
                    selected.setNgayKetThuc(dateFormat.parse(ngayKTField.getText()));
                    selected.setTrangThai(trangThaiField.getText());
                    return selected;
                } catch (ParseException | NumberFormatException e) {
                    showAlert("Lỗi nhập liệu: " + e.getMessage());
                }
            }
            return null;
        });

        Optional<FeeItem> result = dialog.showAndWait();
        result.ifPresent(item -> {
            feeModel.updateFee(item);
            reloadTable();
            showAlert("Đã cập nhật khoản thu.");
        });
    }

    @FXML
    public void onDeleteClicked() {
        FeeItem selected = tableFeeList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Vui lòng chọn khoản thu để xóa.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận xóa");
        confirm.setHeaderText("Bạn có chắc muốn xóa khoản thu '" + selected.getTenKhoanThu() + "'?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            feeModel.deleteFee(selected.getId());
            reloadTable();
            showAlert("Đã xóa khoản thu.");
        }
    }

    @FXML
    public void onBackClicked() throws IOException {
        Stage stage = (Stage) tableFeeList.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/resources/view/fee/feeManager.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Quản lý phí chung cư");
    }

    private void showDetailDialog(FeeItem feeItem) {
        int feeItemId = feeItem.getId();
        double soTienThu = feeItem.getSoTien();
        int tongHo = 10;

        double tongCanThu = feeModel.getTotalExpectedForFee(feeItemId);
        double daThu = feeModel.getTotalPaidForFee(feeItemId);
        double conThieu = feeModel.getTotalMissingForFee(feeItemId);

        List<Payment> payments = paymentModel.getAllPayment();
        Map<Integer, Double> tongTienTheoHo = new HashMap<>();

        for (Payment p : payments) {
            if (p.getFeeItem() != null && p.getFeeItem().getId() == feeItemId) {
                int ho = p.getIdHousehold();
                tongTienTheoHo.put(ho, tongTienTheoHo.getOrDefault(ho, 0.0) + p.getSoTienNop());
            }
        }


        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Chi tiết khoản thu");
        alert.setHeaderText("Thông tin khoản thu: " + feeItem.getTenKhoanThu());
        alert.setContentText(
                "Tổng cần thu: " + tongCanThu + "\n" +
                "Đã thu: " + daThu + "\n" +
                "Còn thiếu: " + conThieu + "\n\n"
        );
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
