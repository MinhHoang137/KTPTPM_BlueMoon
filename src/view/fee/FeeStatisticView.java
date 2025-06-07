package view.fee;

import java.util.List;
import java.util.Scanner;

public class FeeStatisticView {
    private int selectedFeeItemId;
    private Scanner scanner = new Scanner(System.in);

    public int getFeeItemIdForStatistics() {
        System.out.print("Nhap ID khoan thu de thong ke: ");
        selectedFeeItemId = Integer.parseInt(scanner.nextLine());
        return selectedFeeItemId;
    }

    public void displayStatistics(double tongCanThu, double tongDaThu, double tongConThieu, List<Integer> daNop, List<Integer> chuaNop) {
        System.out.println("=== Thong ke khoan thu ===");
        System.out.println("Tong can thu: " + tongCanThu);
        System.out.println("Tong da thu: " + tongDaThu);
        System.out.println("Tong con thieu: " + tongConThieu);
        System.out.println("Ho da nop: " + daNop);
        System.out.println("Ho chua nop: " + chuaNop);
    }
}
