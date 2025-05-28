package model.household;

import model.BaseModel;
import entity.resident.Household;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HouseholdModel extends BaseModel {
    private static HouseholdModel instance;

    private HouseholdModel() {
        // Không cần tạo kết nối ở đây, sẽ gọi getConnection() khi cần
    }

    public static HouseholdModel getInstance() {
        if (instance == null) {
            synchronized (HouseholdModel.class) {
                if (instance == null) {
                    instance = new HouseholdModel();
                }
            }
        }
        return instance;
    }

    // Create
    public int insertHousehold(Household household) throws SQLException {
        String sql = "INSERT INTO hokhau (sohokhau, ngaylamhokhau, chuho_id) VALUES (?, ?, ?)";

        Connection conn = getConnection();
        if (conn == null) {
            throw new SQLException("Không thể kết nối đến cơ sở dữ liệu");
        }

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, household.getHouseholdNumber());
            ps.setDate(2, new java.sql.Date(household.getRegistrationDate().getTime()));
            ps.setInt(3, household.getOwnerId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Tạo household thất bại, không có hàng nào bị ảnh hưởng.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int newId = generatedKeys.getInt(1);
                    household.setId(newId);
                    return newId;
                } else {
                    throw new SQLException("Tạo household thất bại, không lấy được ID.");
                }
            }
        } finally {
            closeConnection();
        }
    }

    // update
    public boolean updateHousehold(Household household) throws SQLException {
        String sql = "UPDATE hokhau SET sohokhau = ?, ngaylamhokhau = ?, chuho_id = ? WHERE hokhauid = ?";

        Connection conn = getConnection();
        if (conn == null) {
            throw new SQLException("Không thể kết nối đến cơ sở dữ liệu");
        }

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, household.getHouseholdNumber());
            ps.setDate(2, new java.sql.Date(household.getRegistrationDate().getTime()));
            ps.setInt(3, household.getOwnerId());
            ps.setInt(4, household.getId());

            ps.executeUpdate();
            return true;
        } finally {
            closeConnection();
        }
    }

    // delete
    public boolean deleteHousehold(int householdId) throws SQLException {
        String sql = "DELETE FROM hokhau WHERE hokhauid = ?";

        Connection conn = getConnection();
        if (conn == null) {
            throw new SQLException("Không thể kết nối đến cơ sở dữ liệu");
        }

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, householdId);
            ps.executeUpdate();
            return true;
        } finally {
            closeConnection();
        }
    }

    // read
    public List<Integer> getResidentsByHouseholdId(int householdId) throws SQLException {
        List<Integer> residentIds = new ArrayList<>();
        String sql = "SELECT nhankhauid FROM nhankhau WHERE hokhau_id = ?";

        Connection conn = getConnection();
        if (conn == null) {
            throw new SQLException("Không thể kết nối đến cơ sở dữ liệu");
        }

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, householdId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    residentIds.add(rs.getInt("nhankhauid"));
                }
            }
        } finally {
            closeConnection();
        }
        return residentIds;
    }
    public Household getHouseholdByNumber(String householdNumber) throws SQLException {
        String sql = "SELECT * FROM hokhau WHERE sohokhau = ?";
        Connection conn = getConnection();
        if (conn == null) {
            throw new SQLException("Không thể kết nối đến cơ sở dữ liệu");
        }
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, householdNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractHousehold(rs);
                }
            }
        } finally {
            closeConnection();
        }
        return null;
    }

    public Household getHouseholdById(int householdId) throws SQLException {
        String sql = "SELECT * FROM hokhau WHERE hokhauid = ?";
        Connection conn = getConnection();
        if (conn == null) {
            throw new SQLException("Không thể kết nối đến cơ sở dữ liệu");
        }
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, householdId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractHousehold(rs);
                }
            }
        } finally {
            closeConnection();
        }
        return null;
    }
    public List<Household> getAllHouseholds() throws SQLException {
        List<Household> households = new ArrayList<>();
        String sql = "SELECT * FROM hokhau";

        Connection conn = getConnection();
        if (conn == null) {
            throw new SQLException("Không thể kết nối đến cơ sở dữ liệu");
        }

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Household household = extractHousehold(rs);
                households.add(household);
            }
        } finally {
            closeConnection();
        }
        return households;
    }

    private Household extractHousehold(ResultSet rs) throws SQLException {
        Household household = new Household();
        household.setId(rs.getInt("hokhauid"));
        household.setHouseholdNumber(rs.getString("sohokhau"));
        household.setRegistrationDate(rs.getDate("ngaylamhokhau"));
        household.setOwnerId(rs.getInt("chuho_id"));
        return household;
    }
}
