package repository.resident;

import entity.resident.Resident;
import model.BaseModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResidentRepository extends BaseModel implements IResidentRepository {

    private static ResidentRepository instance;

    private ResidentRepository() {
    }

    public static ResidentRepository getInstance() {
        if (instance == null) {
            instance = new ResidentRepository();
        }
        return instance;
    }

    // Thêm cư dân mới và cập nhật id cho Resident
    public boolean insertResident(Resident resident) throws SQLException {
        String sql = "INSERT INTO persons(full_name, date_of_birth, gender, ethnicity, religion, citizen_id, occupation, citizen_id_issue_date, citizen_id_issue_place, household_id) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, resident.getFullName());
            stmt.setDate(2, new Date(resident.getBirthDate().getTime()));
            stmt.setString(3, resident.getGender());
            stmt.setString(4, resident.getEthnicity());
            stmt.setString(5, resident.getReligion());
            stmt.setString(6, resident.getIdentityNumber());
            stmt.setString(7, resident.getOccupation());
            stmt.setDate(8, new Date(resident.getIssueDate().getTime()));
            stmt.setString(9, resident.getIssuePlace());
            stmt.setInt(10, resident.getHouseholdId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Insert resident failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    resident.setId(generatedId);
                    System.out.println("Thêm cư dân thành công.");
                    return true;
                } else {
                    throw new SQLException("Insert resident failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return false;
    }

    // Cập nhật cư dân
    public boolean updateResident(Resident resident) {
        String sql = "UPDATE persons SET full_name=?, date_of_birth=?, gender=?, ethnicity=?, religion=?, citizen_id=?, occupation=?, citizen_id_issue_date=?, citizen_id_issue_place=?, household_id=? "
                +
                "WHERE id=?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, resident.getFullName());
            ps.setDate(2, new Date(resident.getBirthDate().getTime()));
            ps.setString(3, resident.getGender());
            ps.setString(4, resident.getEthnicity());
            ps.setString(5, resident.getReligion());
            ps.setString(6, resident.getIdentityNumber());
            ps.setString(7, resident.getOccupation());
            ps.setDate(8, new Date(resident.getIssueDate().getTime()));
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
        String sql = "UPDATE persons SET full_name=?, date_of_birth=?, gender=?, ethnicity=?, religion=?, citizen_id=?, occupation=?, citizen_id_issue_date=?, citizen_id_issue_place=?, household_id=? "
                +
                "WHERE citizen_id = ?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, resident.getFullName());
            ps.setDate(2, new Date(resident.getBirthDate().getTime()));
            ps.setString(3, resident.getGender());
            ps.setString(4, resident.getEthnicity());
            ps.setString(5, resident.getReligion());
            ps.setString(6, resident.getIdentityNumber()); // nếu cho phép thay đổi CCCD
            ps.setString(7, resident.getOccupation());
            ps.setDate(8, new Date(resident.getIssueDate().getTime()));
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
        String sql = "DELETE FROM persons WHERE citizen_id = ?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, resident.getIdentityNumber());
            System.out.println(resident.getIdentityNumber());
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
        String sql = "SELECT * FROM persons";
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
        String sql = "SELECT * FROM persons WHERE citizen_id = ?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cccd);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return extractResident(rs);
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
        String sql = "SELECT * FROM persons WHERE full_name LIKE ?";
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
        } finally {
            closeConnection(); // Đảm bảo đóng kết nối

        }
        return list;
    }

    // Tìm theo ID
    public Resident getResidentById(int id) {
        String sql = "SELECT * FROM persons WHERE id = ?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return extractResident(rs);
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
        setResidentId(resident, rs.getInt("id"));
        resident.setFullName(rs.getString("full_name"));
        resident.setBirthDate(rs.getDate("date_of_birth"));
        resident.setGender(rs.getString("gender"));
        resident.setEthnicity(rs.getString("ethnicity"));
        resident.setReligion(rs.getString("religion"));
        resident.setIdentityNumber(rs.getString("citizen_id"));
        resident.setOccupation(rs.getString("occupation"));
        resident.setIssueDate(rs.getDate("citizen_id_issue_date"));
        resident.setIssuePlace(rs.getString("citizen_id_issue_place"));
        resident.setHouseholdId(rs.getInt("household_id"));
        return resident;
    }

    private void setResidentId(Resident resident, int id) {
        resident.setId(id);
    }
}
