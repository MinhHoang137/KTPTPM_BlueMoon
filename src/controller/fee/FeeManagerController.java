package controller.fee;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class FeeManagerController {

    @FXML private Button btnFee;
    @FXML private Button btnPayment;

    @FXML
    public void onFeeClicked() throws Exception {
        Stage stage = (Stage) btnFee.getScene().getWindow();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/resources/view/fee/fee_list.fxml")));
        stage.setScene(scene);
        stage.setTitle("Quản lý khoản thu");
    }

    @FXML
    public void onPaymentClicked() throws Exception {
        Stage stage = (Stage) btnPayment.getScene().getWindow();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/resources/view/fee/payment_list.fxml")));
        stage.setScene(scene);
        stage.setTitle("Quản lý khoản nộp");
    }

    @FXML
    public void onBackClicked() throws IOException {
        // Lấy Stage hiện tại
        Stage stage = (Stage) btnFee.getScene().getWindow();

        // Tải giao diện mới
        Parent root = FXMLLoader.load(getClass().getResource("/resources/view/user/homePage.fxml"));

        // Thiết lập giao diện mới vào Stage
        stage.setScene(new Scene(root));

        // Cập nhật tiêu đề cho cửa sổ (tùy chọn)
        stage.setTitle("Trang chủ");
    }

}