package model.resident;

import entity.resident.Resident;
import model.BaseModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResidentModel extends BaseModel {

    private static ResidentModel instance;

    private ResidentModel() {
    }

    public static ResidentModel getInstance() {
        if (instance == null) {
            instance = new ResidentModel();
        }
        return instance;
    }

    // Thêm cư dân mới và cập nhật id cho Resident
    public boolean insertResident(Resident resident) throws SQLException {
        String sql = "INSERT INTO nhankhau(hoten, ngaysinh, gioitinh, dantoc, tongiao, cccd, nghenghiep, ngaycap, noicap, hokhauid) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, resident.getFullName());
            stmt.setDate(2, new java.sql.Date(resident.getBirthDate().getTime()));
            stmt.setString(3, resident.getGender());
            stmt.setString(4, resident.getEthnicity());
            stmt.setString(5, resident.getReligion());
            stmt.setString(6, resident.getIdentityNumber());
            stmt.setString(7, resident.getOccupation());
            stmt.setDate(8, new java.sql.Date(resident.getIssueDate().getTime()));
            stmt.setString(9, resident.getIssuePlace());
            stmt.setInt(10, resident.getHouseholdId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Insert resident failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    resident.setId(generatedId); // ⚠️ Quan trọng
                    return true; // Thêm thành công
                } else {
                    throw new SQLException("Insert resident failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // hoặc log tùy ý
        }
        finally {
            closeConnection(); // Đảm bảo đóng kết nối
        }
        return false;
    }


    // Cập nhật cư dân
    public boolean updateResident(Resident resident) {
        String sql = "UPDATE nhankhau SET hoten=?, ngaysinh=?, gioitinh=?, dantoc=?, tongiao=?, cccd=?, nghenghiep=?, ngaycap=?, noicap=?, hokhauid=? " +
                "WHERE nhankhauid=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, resident.getFullName());
            ps.setDate(2, new java.sql.Date(resident.getBirthDate().getTime()));
            ps.setString(3, resident.getGender());
            ps.setString(4, resident.getEthnicity());
            ps.setString(5, resident.getReligion());
            ps.setString(6, resident.getIdentityNumber());
            ps.setString(7, resident.getOccupation());
            ps.setDate(8, new java.sql.Date(resident.getIssueDate().getTime()));
            ps.setString(9, resident.getIssuePlace());
            ps.setInt(10, resident.getHouseholdId());
            ps.setInt(11, resident.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(); // Đảm bảo đóng kết nối
        }
        return false;
    }
    public boolean updateResidentByCCCD(Resident resident) {
        String sql = "UPDATE nhankhau SET hoten=?, ngaysinh=?, gioitinh=?, dantoc=?, tongiao=?, cccd=?, nghenghiep=?, ngaycap=?, noicap=?, hokhauid=? " +
                "WHERE cccd = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, resident.getFullName());
            ps.setDate(2, new java.sql.Date(resident.getBirthDate().getTime()));
            ps.setString(3, resident.getGender());
            ps.setString(4, resident.getEthnicity());
            ps.setString(5, resident.getReligion());
            ps.setString(6, resident.getIdentityNumber()); // nếu cho phép thay đổi CCCD
            ps.setString(7, resident.getOccupation());
            ps.setDate(8, new java.sql.Date(resident.getIssueDate().getTime()));
            ps.setString(9, resident.getIssuePlace());
            ps.setInt(10, resident.getHouseholdId());
            ps.setString(11, resident.getIdentityNumber()); // WHERE cccd = ?

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return false;
    }


    // Xóa cư dân
    public boolean deleteResident(Resident resident) {
        String sql = "DELETE FROM nhankhau WHERE nhankhauid = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, resident.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.getErrorCode();
        } finally {
            closeConnection(); // Đảm bảo đóng kết nối
        }
        return false;
    }

    // Lấy tất cả cư dân
    public List<Resident> getAllResidents() {
        List<Resident> list = new ArrayList<>();
        String sql = "SELECT * FROM nhankhau";
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(extractResident(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(); // Đảm bảo đóng kết nối
        }
        return list;
    }

    // Tìm theo CCCD
    public Resident getResidentByCCCD(String cccd) {
        String sql = "SELECT * FROM nhankhau WHERE cccd = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cccd);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return extractResident(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(); // Đảm bảo đóng kết nối
        }
        return null;
    }

    // Tìm theo tên
    public List<Resident> getResidentsByName(String name) {
        List<Resident> list = new ArrayList<>();
        String sql = "SELECT * FROM nhankhau WHERE hoten LIKE ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractResident(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeConnection(); // Đảm bảo đóng kết nối

        }
        return list;
    }

    // Tìm theo ID
    public Resident getResidentById(int id) {
        String sql = "SELECT * FROM nhankhau WHERE nhankhauid = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return extractResident(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(); // Đảm bảo đóng kết nối
        }
        return null;
    }

    // Tạo Resident từ ResultSet
    private Resident extractResident(ResultSet rs) throws SQLException {
        Resident resident = new Resident();
        setResidentId(resident, rs.getInt("nhankhauid"));
        resident.setFullName(rs.getString("hoten"));
        resident.setBirthDate(rs.getDate("ngaysinh"));
        resident.setGender(rs.getString("gioitinh"));
        resident.setEthnicity(rs.getString("dantoc"));
        resident.setReligion(rs.getString("tongiao"));
        resident.setIdentityNumber(rs.getString("cccd"));
        resident.setOccupation(rs.getString("nghenghiep"));
        resident.setIssueDate(rs.getDate("ngaycap"));
        resident.setIssuePlace(rs.getString("noicap"));
        resident.setHouseholdId(rs.getInt("hokhauid"));
        return resident;
    }

    private void setResidentId(Resident resident, int id) {
        resident.setId(id);
    }
}
