package repository.household;

import entity.resident.Household;
import entity.resident.Resident;
import model.BaseModel;
import repository.resident.ResidentRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HouseholdRepository extends BaseModel implements  IHouseholdRepository {
    private static HouseholdRepository instance;

    private HouseholdRepository() {
        super();
    }

    public static HouseholdRepository getInstance() {
        if (instance == null) {
            instance = new HouseholdRepository();
        }
        return instance;
    }

    private void extractHouseholdFromResultSet(ResultSet rs, Household household) throws SQLException {
        household.setId(rs.getInt("id"));
        household.setHouseholdNumber(rs.getString("household_registration_number"));
        household.setRegistrationDate(rs.getDate("registration_date"));
        household.setOwnerId(rs.getInt("head_of_household_id"));
        Resident owner = ResidentRepository.getInstance().getResidentById(household.getOwnerId());
        household.setOwner(owner);
        household.setAddressNumber(rs.getString("address_number"));
        ArrayList <Resident> residents = ResidentRepository.getInstance()
                .getResidentByHouseholdId(household.getId());
        household.setMembers(residents);
    }
    // read
    public Household getHouseholdById(int householdId) throws SQLException {
        String sql = "SELECT * FROM households WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, householdId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Household household = new Household();
                    extractHouseholdFromResultSet(rs, household);
                    return household;
                }
            }
        }
        return null;
    }

    public ArrayList<Household> getAllHouseholds() throws SQLException {
        ArrayList<Household> households = new ArrayList<>();
        String sql = "SELECT * FROM households";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Household household = new Household();
                extractHouseholdFromResultSet(rs, household);
                households.add(household);
            }
        }
        return households;
    }
    public Household getHouseholdByNumber(String householdNumber) throws SQLException {
        String sql = "SELECT * FROM households WHERE household_registration_number = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, householdNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Household household = new Household();
                    extractHouseholdFromResultSet(rs, household);
                    return household;
                }
            }
        }
        return null;
    }
    public Household getHouseholdByAddressNumber(String addressNumber) throws SQLException {
        String sql = "SELECT * FROM households WHERE address_number = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, addressNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Household household = new Household();
                    extractHouseholdFromResultSet(rs, household);
                    return household;
                }
            }
        }
        return null;
    }
    //Create
    public int insertHousehold(Household household) throws SQLException {
        if (getHouseholdByAddressNumber(household.getAddressNumber()) != null) {
            System.out.println("Hộ gia đình với địa chỉ này đã tồn tại.");
            return -1; // Trả về -1 nếu địa chỉ đã tồn tại
        }
        String sql = "INSERT INTO households (household_registration_number, registration_date, head_of_household_id, address_number) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, household.getHouseholdNumber());
            ps.setDate(2, new java.sql.Date(household.getRegistrationDate().getTime()));
            ps.setInt(3, household.getOwnerId());
            ps.setString(4, household.getAddressNumber());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        }
        return -1; // Trả về -1 nếu không thành công
    }
    // Update
    public boolean updateHousehold(Household household) throws SQLException {
        String sql = "UPDATE households SET household_registration_number = ?, registration_date = ?, head_of_household_id = ?, address_number = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, household.getHouseholdNumber());
            ps.setDate(2, new java.sql.Date(household.getRegistrationDate().getTime()));
            int ownerId = household.getOwner() != null ? household.getOwner().getId() : 0;
            ps.setInt(3, ownerId);
            ps.setString(4, household.getAddressNumber());
            ps.setInt(5, household.getId());
            return ps.executeUpdate() > 0;
        }
    }
    // Delete
    public boolean deleteHousehold(Household household) throws SQLException {
        String setNullOwnerSql = "UPDATE households SET head_of_household_id = NULL WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(setNullOwnerSql)) {
            ps.setInt(1, household.getId());
            ps.executeUpdate(); // Cập nhật để đặt head_of_household_id thành NULL
        } catch (SQLException e) {
            System.out.println("Lỗi khi cập nhật head_of_household_id thành NULL: " + e.getMessage());
            return false; // Nếu không cập nhật được, trả về false
        }
        for (Resident resident : household.getMembers()) {
            if (!ResidentRepository.getInstance().deleteResident(resident)) {
                System.out.println("Không thể xóa cư dân: " + resident.getFullName() + " trong hộ gia đình: " + household.getHouseholdNumber());
                return false; // Nếu không xóa được cư dân, trả về false
            }
        }
        String sql = "DELETE FROM households WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, household.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
