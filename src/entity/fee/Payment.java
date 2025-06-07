package entity.fee;

import java.util.Date;

public class Payment {
    private int id;
    private int idHousehold;
    private double soTienNop;
    private FeeItem feeItem;
    private Date ngayNop;
    private String trangThai;

    public Payment() {}

    public Payment(int id, int idHousehold, double soTienNop, FeeItem feeItem, Date ngayNop, String trangThai) {
        this.id = id;
        this.idHousehold = idHousehold;
        this.soTienNop = soTienNop;
        this.feeItem = feeItem;
        this.ngayNop = ngayNop;
        this.trangThai = trangThai;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdHousehold() { return idHousehold; }
    public void setIdHousehold(int idHousehold) { this.idHousehold = idHousehold; }

    public double getSoTienNop() { return soTienNop; }
    public void setSoTienNop(double soTienNop) { this.soTienNop = soTienNop; }

    public FeeItem getFeeItem() { return feeItem; }
    public void setFeeItem(FeeItem feeItem) { this.feeItem = feeItem; }

    public Date getNgayNop() { return ngayNop; }
    public void setNgayNop(Date ngayNop) { this.ngayNop = ngayNop; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public void payment() {
        this.trangThai = "Đã nộp";
        this.ngayNop = new Date();
    }
}
