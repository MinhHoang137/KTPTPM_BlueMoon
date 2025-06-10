package repository.fee;

import entity.fee.FeeItem;
import entity.fee.Payment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentRepositoryImpl implements PaymentRepository {

    private static final String URL = "jdbc:mysql://localhost:3306/chungcu_bluemoon?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "Hoang!3070$";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private Payment extractPayment(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        FeeItem feeItem = new FeeItem();

        payment.setId(rs.getInt("id"));
        payment.setIdHousehold(rs.getInt("id_household"));
        payment.setSoTienNop(rs.getDouble("so_tien_nop"));
        payment.setNgayNop(rs.getDate("ngay_nop"));
        payment.setTrangThai(rs.getString("trang_thai"));

        feeItem.setId(rs.getInt("fee_item_id"));
        payment.setFeeItem(feeItem);

        return payment;
    }

    @Override
    public List<Payment> findAll() {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM payments";

        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(extractPayment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Payment findById(int id) {
        String sql = "SELECT * FROM payments WHERE id = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return extractPayment(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(Payment payment) {
        String sql = "INSERT INTO payments (id_household, so_tien_nop, fee_item_id, ngay_nop, trang_thai) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, payment.getIdHousehold());
            stmt.setDouble(2, payment.getSoTienNop());
            stmt.setInt(3, payment.getFeeItem().getId());
            stmt.setDate(4, new java.sql.Date(payment.getNgayNop().getTime()));
            stmt.setString(5, payment.getTrangThai());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Payment payment) {
        String sql = "UPDATE payments SET id_household = ?, so_tien_nop = ?, fee_item_id = ?, ngay_nop = ?, trang_thai = ? WHERE id = ?";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, payment.getIdHousehold());
            stmt.setDouble(2, payment.getSoTienNop());
            stmt.setInt(3, payment.getFeeItem().getId());
            stmt.setDate(4, new java.sql.Date(payment.getNgayNop().getTime()));
            stmt.setString(5, payment.getTrangThai());
            stmt.setInt(6, payment.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM payments WHERE id = ?";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Payment> findByHouseholdId(int householdId) {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM payments WHERE id_household = ?";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, householdId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractPayment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Payment> findByFeeItemId(int feeItemId) {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM payments WHERE fee_item_id = ?";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, feeItemId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractPayment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Integer> findPaidHouseholdsByFeeItemId(int feeItemId) {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT DISTINCT id_household FROM payments WHERE fee_item_id = ? AND trang_thai = 'Đã nộp'";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, feeItemId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ids.add(rs.getInt("id_household"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }

    @Override
    public List<Integer> findUnpaidHouseholdsByFeeItemId(int feeItemId) {
        // Tuỳ vào thiết kế DB có bảng "households" hay không, đoạn này là placeholder
        // Để đơn giản, ta return empty list
        return new ArrayList<>();
    }

    @Override
    public double sumTotalPaidByFeeItemId(int feeItemId) {
        String sql = "SELECT SUM(so_tien_nop) AS total FROM payments WHERE fee_item_id = ? AND trang_thai = 'Đã nộp'";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, feeItemId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int countUnpaidHouseholdsByFeeItemId(int feeItemId) {
        // Tuỳ thiết kế bảng, có thể join households nếu có. Tạm thời trả về 0
        return 0;
    }
}
