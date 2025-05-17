package view.fee;

import java.util.Date;
import java.util.Scanner;

import entity.fee.FeeItem;

public class FeeUpdateView {
    private Scanner scanner = new Scanner(System.in);
    private FeeItem feeItemToUpdate;
    private int infoNV;
    private String stateDV;

    public FeeItem updateFeeData(FeeItem feeItem) {
        this.feeItemToUpdate = feeItem;
        System.out.print("Sua ten khoan thu (" + feeItem.getTenKhoanThu() + "): ");
        feeItem.setTenKhoanThu(scanner.nextLine());
        System.out.print("Sua so tien (" + feeItem.getSoTien() + "): ");
        feeItem.setSoTien(Double.parseDouble(scanner.nextLine()));
        System.out.print("Sua mo ta (" + feeItem.getMoTa() + "): ");
        feeItem.setMoTa(scanner.nextLine());
        feeItem.setNgayKetThuc(new Date());
        return feeItem;
    }
}
