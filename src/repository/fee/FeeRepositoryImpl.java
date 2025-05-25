package repository.fee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import entity.fee.FeeItem;
import model.BaseModel;

public class FeeRepositoryImpl extends BaseModel implements FeeRepository {



    @Override
    public List<FeeItem> findAll() {
        List<FeeItem> feeItems = new ArrayList<>();
        String sql = "SELECT id, fee_name, amount, description, start_date, end_date, status, created_at, updated_at FROM fee_items";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                FeeItem item = new FeeItem();
                item.setId(rs.getInt("id"));
                item.setFeeName(rs.getString("fee_name"));
                item.setAmount(rs.getDouble("amount"));
                item.setDescription(rs.getString("description"));
                item.setStartDate(rs.getDate("start_date"));
                item.setEndDate(rs.getDate("end_date"));
                item.setStatus(rs.getString("status"));
                item.setCreatedAt(rs.getTimestamp("created_at"));
                item.setUpdatedAt(rs.getTimestamp("updated_at"));
                feeItems.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return feeItems;
    }

    @Override
    public FeeItem findById(int id) {
        String sql = "SELECT id, fee_name, amount, description, start_date, end_date, status, created_at, updated_at FROM fee_items WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                FeeItem item = new FeeItem();
                item.setId(rs.getInt("id"));
                item.setFeeName(rs.getString("fee_name"));
                item.setAmount(rs.getDouble("amount"));
                item.setDescription(rs.getString("description"));
                item.setStartDate(rs.getDate("start_date"));
                item.setEndDate(rs.getDate("end_date"));
                item.setStatus(rs.getString("status"));
                item.setCreatedAt(rs.getTimestamp("created_at"));
                item.setUpdatedAt(rs.getTimestamp("updated_at"));
                return item;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void save(FeeItem feeItem) {
        String sql = "INSERT INTO fee_items (fee_name, amount, description, start_date, end_date, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) { 

            stmt.setString(1, feeItem.getFeeName());
            stmt.setDouble(2, feeItem.getAmount());
            stmt.setString(3, feeItem.getDescription());
            stmt.setDate(4, feeItem.getStartDate() != null ? new java.sql.Date(feeItem.getStartDate().getTime()) : null);
            stmt.setDate(5, feeItem.getEndDate() != null ? new java.sql.Date(feeItem.getEndDate().getTime()) : null);
            stmt.setString(6, feeItem.getStatus());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                feeItem.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(FeeItem feeItem) {
        String sql = "UPDATE fee_items SET fee_name = ?, amount = ?, description = ?, start_date = ?, end_date = ?, status = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, feeItem.getFeeName());
            stmt.setDouble(2, feeItem.getAmount());
            stmt.setString(3, feeItem.getDescription());
            stmt.setDate(4, feeItem.getStartDate() != null ? new java.sql.Date(feeItem.getStartDate().getTime()) : null);
            stmt.setDate(5, feeItem.getEndDate() != null ? new java.sql.Date(feeItem.getEndDate().getTime()) : null);
            stmt.setString(6, feeItem.getStatus());
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