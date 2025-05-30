package entity.fee;

import java.util.Date;

public class FeeItem {
    private int id;
    private String tenKhoanThu;
    private double soTien;
    private String moTa;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private String trangThai;

    public FeeItem() {}

    public FeeItem(int id, String tenKhoanThu, double soTien, String moTa, Date ngayBatDau, Date ngayKetThuc, String trangThai) {
        this.id = id;
        this.tenKhoanThu = tenKhoanThu;
        this.soTien = soTien;
        this.moTa = moTa;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.trangThai = trangThai;
    }

    // Getters và setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTenKhoanThu() { return tenKhoanThu; }
    public void setTenKhoanThu(String tenKhoanThu) { this.tenKhoanThu = tenKhoanThu; }

    public double getSoTien() { return soTien; }
    public void setSoTien(double soTien) { this.soTien = soTien; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public Date getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(Date ngayBatDau) { this.ngayBatDau = ngayBatDau; }

    public Date getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(Date ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
