package repository.household;

import entity.resident.Household;
import model.BaseModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HouseholdRepository extends BaseModel implements IHouseholdRepository {
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

    // Create
    public int insertHousehold(Household household) throws SQLException {
        String sql = "INSERT INTO households (household_registration_number, registration_date, head_of_household_id) VALUES (?, ?, ?)";

        Connection conn = getConnection();
        if (conn == null)
            throw new SQLException("Cannot connect to the database");

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, household.getHouseholdNumber());
            ps.setDate(2, new Date(household.getRegistrationDate().getTime()));
            ps.setInt(3, household.getOwnerId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0)
                throw new SQLException("Creating household failed, no rows affected.");

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int newId = generatedKeys.getInt(1);
                    household.setId(newId);
                    return newId;
                } else {
                    throw new SQLException("Creating household failed, no ID obtained.");
                }
            }
        } finally {
            closeConnection();
        }
    }

    // Update
    public boolean updateHousehold(Household household) throws SQLException {
        String sql = "UPDATE households SET household_registration_number = ?, registration_date = ?, head_of_household_id = ? WHERE id = ?";

        Connection conn = getConnection();
        if (conn == null)
            throw new SQLException("Cannot connect to the database");

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, household.getHouseholdNumber());
            ps.setDate(2, new Date(household.getRegistrationDate().getTime()));
            ps.setInt(3, household.getOwnerId());
            ps.setInt(4, household.getId());

            ps.executeUpdate();
            return true;
        } finally {
            closeConnection();
        }
    }

    // Delete
    public boolean deleteHousehold(int householdId) throws SQLException {
        String sql = "DELETE FROM households WHERE id = ?";

        Connection conn = getConnection();
        if (conn == null)
            throw new SQLException("Cannot connect to the database");

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, householdId);
            ps.executeUpdate();
            return true;
        } finally {
            closeConnection();
        }
    }

    // Get all households
    public List<Household> getAllHouseholds() throws SQLException {
        List<Household> households = new ArrayList<>();
        String sql = "SELECT * FROM households";

        Connection conn = getConnection();
        if (conn == null)
            throw new SQLException("Cannot connect to the database");

        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                households.add(extractHousehold(rs));
            }
        } finally {
            closeConnection();
        }
        return households;
    }

    // Get by household registration number
    public Household getHouseholdByNumber(String householdNumber) throws SQLException {
        String sql = "SELECT * FROM households WHERE household_registration_number = ?";

        Connection conn = getConnection();
        if (conn == null)
            throw new SQLException("Cannot connect to the database");

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

    // Get by household ID
    public Household getHouseholdById(int householdId) throws SQLException {
        String sql = "SELECT * FROM households WHERE id = ?";

        Connection conn = getConnection();
        if (conn == null)
            throw new SQLException("Cannot connect to the database");

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

    // Get resident IDs by household ID
    public List<Integer> getResidentsByHouseholdId(int householdId) throws SQLException {
        List<Integer> residentIds = new ArrayList<>();
        String sql = "SELECT id FROM persons WHERE household_id = ?";

        Connection conn = getConnection();
        if (conn == null)
            throw new SQLException("Cannot connect to the database");

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, householdId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    residentIds.add(rs.getInt("id"));
                }
            }
        } finally {
            closeConnection();
        }
        return residentIds;
    }

    // Helper: convert ResultSet -> Household
    private Household extractHousehold(ResultSet rs) throws SQLException {
        Household household = new Household();
        household.setId(rs.getInt("id"));
        household.setHouseholdNumber(rs.getString("household_registration_number"));
        household.setRegistrationDate(rs.getDate("registration_date"));
        household.setOwnerId(rs.getInt("head_of_household_id"));
        return household;
    }
}
