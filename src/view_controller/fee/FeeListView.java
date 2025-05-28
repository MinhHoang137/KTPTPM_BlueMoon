package view_controller.fee;

import java.util.List;
import java.util.Scanner;

import entity.fee.FeeItem;

public class FeeListView {
    private List<FeeItem> fees;
    private Scanner scanner = new Scanner(System.in);
    private int selectedFeeId;
    private int actionChoice;

    public int getSelectedFeeId() {
        return selectedFeeId;
    }

    public int getActionChoice() {
        return actionChoice;
    }

    public void displayFeeList(List<FeeItem> fees) {
        this.fees = fees;
        System.out.println("=== Danh sach khoan thu ===");
        for (FeeItem fee : fees) {
            System.out.println(fee.getId() + ": " + fee.getTenKhoanThu() + " - " + fee.getSoTien());
        }
        System.out.println("===========================");
        System.out.print("Nhap ID khoan thu: ");
        selectedFeeId = Integer.parseInt(scanner.nextLine());
        System.out.print("Chon hanh dong (1-Xem, 2-Them, 3-Sua, 4-Xoa, 5-Thong ke): ");
        actionChoice = Integer.parseInt(scanner.nextLine());
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}
