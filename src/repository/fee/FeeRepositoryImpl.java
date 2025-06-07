package repository.fee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import entity.fee.FeeItem;

public class FeeRepositoryImpl implements FeeRepository {

    private static final String URL = "jdbc:mysql://localhost:3306/chungcu_bluemoon?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "mysql123456";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    @Override
    public List<FeeItem> findAll() {
        List<FeeItem> feeItems = new ArrayList<>();
        String sql = "SELECT * FROM fee_items";

        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                FeeItem item = new FeeItem();
                item.setId(rs.getInt("id"));
                item.setTenKhoanThu(rs.getString("ten_khoan_thu"));
                item.setSoTien(rs.getDouble("so_tien"));
                item.setMoTa(rs.getString("mo_ta"));
                item.setNgayBatDau(rs.getDate("ngay_bat_dau"));
                item.setNgayKetThuc(rs.getDate("ngay_ket_thuc"));
                item.setTrangThai(rs.getString("trang_thai"));
                feeItems.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return feeItems;
    }

    @Override
    public FeeItem findById(int id) {
        String sql = "SELECT * FROM fee_items WHERE id = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                FeeItem item = new FeeItem();
                item.setId(rs.getInt("id"));
                item.setTenKhoanThu(rs.getString("ten_khoan_thu"));
                item.setSoTien(rs.getDouble("so_tien"));
                item.setMoTa(rs.getString("mo_ta"));
                item.setNgayBatDau(rs.getDate("ngay_bat_dau"));
                item.setNgayKetThuc(rs.getDate("ngay_ket_thuc"));
                item.setTrangThai(rs.getString("trang_thai"));
                return item;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void save(FeeItem feeItem) {
        String sql = "INSERT INTO fee_items (ten_khoan_thu, so_tien, mo_ta, ngay_bat_dau, ngay_ket_thuc, trang_thai) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, feeItem.getTenKhoanThu());
            stmt.setDouble(2, feeItem.getSoTien());
            stmt.setString(3, feeItem.getMoTa());
            stmt.setDate(4, new java.sql.Date(feeItem.getNgayBatDau().getTime()));
            stmt.setDate(5, new java.sql.Date(feeItem.getNgayKetThuc().getTime()));
            stmt.setString(6, feeItem.getTrangThai());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(FeeItem feeItem) {
        String sql = "UPDATE fee_items SET ten_khoan_thu = ?, so_tien = ?, mo_ta = ?, ngay_bat_dau = ?, ngay_ket_thuc = ?, trang_thai = ? WHERE id = ?";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, feeItem.getTenKhoanThu());
            stmt.setDouble(2, feeItem.getSoTien());
            stmt.setString(3, feeItem.getMoTa());
            stmt.setDate(4, new java.sql.Date(feeItem.getNgayBatDau().getTime()));
            stmt.setDate(5, new java.sql.Date(feeItem.getNgayKetThuc().getTime()));
            stmt.setString(6, feeItem.getTrangThai());
            stmt.setInt(7, feeItem.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM fee_items WHERE id = ?";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
