package view_controller.fee;

import java.util.Date;
import java.util.Scanner;

import entity.fee.FeeItem;

public class FeeCreateView {
    private Scanner scanner = new Scanner(System.in);
    private FeeItem newFeeItem;

    public FeeItem inputNewFeeData() {
        newFeeItem = new FeeItem();
        System.out.print("Ten khoan thu: ");
        newFeeItem.setTenKhoanThu(scanner.nextLine());
        System.out.print("So tien: ");
        newFeeItem.setSoTien(Double.parseDouble(scanner.nextLine()));
        System.out.print("Mo ta: ");
        newFeeItem.setMoTa(scanner.nextLine());
        newFeeItem.setNgayBatDau(new Date());
        newFeeItem.setNgayKetThuc(new Date());
        newFeeItem.setTrangThai("Dang thu");
        return newFeeItem;
    }
}
