package view_controller.fee;

import entity.fee.FeeItem;

public class FeeDetailView {
    private FeeItem feeItem;

    public void displayFeeDetail(FeeItem feeItem) {
        this.feeItem = feeItem;
        System.out.println("=== Chi tiet khoan thu ===");
        System.out.println("Ten: " + feeItem.getTenKhoanThu());
        System.out.println("So tien: " + feeItem.getSoTien());
        System.out.println("Mo ta: " + feeItem.getMoTa());
        System.out.println("Trang thai: " + feeItem.getTrangThai());
    }
}
