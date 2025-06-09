package view.fee;

import java.util.Scanner;

import entity.fee.FeeItem;

public class FeeDeleteView {
    private Scanner scanner = new Scanner(System.in);
    private FeeItem feeItemToDelete;

    public boolean confirmDelete(FeeItem feeItem) {
        this.feeItemToDelete = feeItem;
        System.out.print("Xac nhan xoa khoan thu '" + feeItem.getTenKhoanThu() + "' (y/n)? ");
        String input = scanner.nextLine();
        return input.equalsIgnoreCase("y");
    }
}
