package repository.fee;

import entity.fee.FeeItem;
import entity.fee.Payment;
import model.BaseModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PaymentRepositoryImpl extends BaseModel implements PaymentRepository {

    private FeeRepository feeRepository;

    public PaymentRepositoryImpl(FeeRepository feeRepository) {
        super();
        this.feeRepository = feeRepository;
    }

    private Payment extractPayment(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setId(rs.getInt("id"));
        payment.setHouseholdId(rs.getInt("household_id"));
        int feeItemId = rs.getInt("fee_item_id");
        payment.setAmountPaid(rs.getDouble("amount_paid"));
        payment.setPaymentDate(rs.getDate("payment_date"));
        payment.setStatus(rs.getString("status"));
        payment.setCreatedAt(rs.getTimestamp("created_at"));
        payment.setUpdatedAt(rs.getTimestamp("updated_at"));

        FeeItem feeItem = null;
        try {
            feeItem = feeRepository.findById(feeItemId);
        } catch (Exception e) {
            System.err.println("Lỗi khi tải FeeItem cho Payment ID " + payment.getId() + ": " + e.getMessage());
            // In stack trace hoặc xử lý lỗi loading FeeItem cho Payment nếu cần
            // e.printStackTrace(); 
        }
        payment.setFeeItem(feeItem);

        return payment;
    }

    @Override
    public List<Payment> findAll() throws SQLException {
        String sql = "SELECT id, household_id, fee_item_id, amount_paid, payment_date, status, created_at, updated_at FROM payments";
        try (Connection conn = getConnection(); // getConnection() giờ ném SQLException
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            List<Payment> payments = new ArrayList<>();
            while (rs.next()) {
                payments.add(extractPayment(rs));
            }
            return payments;
        } finally {
            closeConnection();
        }
    }

    @Override
    public Payment findById(int id) throws SQLException {
        String sql = "SELECT id, household_id, fee_item_id, amount_paid, payment_date, status, created_at, updated_at FROM payments WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractPayment(rs);
            }
        } finally {
            closeConnection();
        }
        return null;
    }

    @Override
    public boolean save(Payment payment) throws SQLException {
        String sql = "INSERT INTO payments (household_id, fee_item_id, amount_paid, payment_date, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, payment.getHouseholdId());
            stmt.setInt(2, payment.getFeeItem().getId());
            stmt.setDouble(3, payment.getAmountPaid());
            stmt.setDate(4, payment.getPaymentDate() != null ? new java.sql.Date(payment.getPaymentDate().getTime()) : null);
            stmt.setString(5, payment.getStatus());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    payment.setId(generatedKeys.getInt(1));
                }
            }
            return true;
        } finally {
            closeConnection();
        }
    }

    @Override
    public boolean update(Payment payment) throws SQLException {
        String sql = "UPDATE payments SET household_id = ?, fee_item_id = ?, amount_paid = ?, payment_date = ?, status = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, payment.getHouseholdId());
            stmt.setInt(2, payment.getFeeItem().getId());
            stmt.setDouble(3, payment.getAmountPaid());
            stmt.setDate(4, payment.getPaymentDate() != null ? new java.sql.Date(payment.getPaymentDate().getTime()) : null);
            stmt.setString(5, payment.getStatus());
            stmt.setInt(6, payment.getId());
            return stmt.executeUpdate() > 0;
        } finally {
            closeConnection();
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM payments WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } finally {
            closeConnection();
        }
    }

    @Override
    public List<Payment> findByHouseholdId(int householdId) throws SQLException {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT id, household_id, fee_item_id, amount_paid, payment_date, status, created_at, updated_at FROM payments WHERE household_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, householdId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                payments.add(extractPayment(rs));
            }
        } finally {
            closeConnection();
        }
        return payments;
    }

    @Override
    public List<Payment> findByFeeItemId(int feeItemId) throws SQLException {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT id, household_id, fee_item_id, amount_paid, payment_date, status, created_at, updated_at FROM payments WHERE fee_item_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, feeItemId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                payments.add(extractPayment(rs));
            }
        } finally {
            closeConnection();
        }
        return payments;
    }

    @Override
    public double sumTotalPaidByFeeItemId(int feeItemId) throws SQLException {
        String sql = "SELECT SUM(amount_paid) AS total FROM payments WHERE fee_item_id = ? AND status = 'Đã nộp'";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, feeItemId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } finally {
            closeConnection();
        }
        return 0;
    }

    @Override
    public int countUnpaidHouseholdsByFeeItemId(int feeItemId) throws SQLException {
        return 0;
    }
}