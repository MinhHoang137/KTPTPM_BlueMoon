package repository.temporary;

import entity.temporary.TemporaryRegistration;
import model.BaseModel;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TempRepo extends BaseModel implements ITempRepo {
    private static TempRepo instance;

    private TempRepo() {}

    public static TempRepo getInstance() {
        if (instance == null) {
            instance = new TempRepo();
        }
        return instance;
    }

    @Override
    public boolean add(TemporaryRegistration reg) {
        String sql = "INSERT INTO temporary_registrations (person_id, registration_type, start_date, end_date, address_of_stay, reason_notes) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, reg.getPersonId());
            ps.setString(2, reg.getRegistrationType());
            ps.setDate(3, Date.valueOf(reg.getStartDate()));
            if (reg.getEndDate() != null)
                ps.setDate(4, Date.valueOf(reg.getEndDate()));
            else
                ps.setNull(4, Types.DATE);
            ps.setString(5, reg.getAddressOfStay());
            ps.setString(6, reg.getReasonNotes());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return false;
    }

    @Override
    public boolean update(TemporaryRegistration reg) {
        String sql = "UPDATE temporary_registrations SET person_id = ?, registration_type = ?, start_date = ?, end_date = ?, " +
                "address_of_stay = ?, reason_notes = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, reg.getPersonId());
            ps.setString(2, reg.getRegistrationType());
            ps.setDate(3, Date.valueOf(reg.getStartDate()));
            if (reg.getEndDate() != null)
                ps.setDate(4, Date.valueOf(reg.getEndDate()));
            else
                ps.setNull(4, Types.DATE);
            ps.setString(5, reg.getAddressOfStay());
            ps.setString(6, reg.getReasonNotes());
            ps.setInt(7, reg.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return false;
    }

    @Override
    public boolean delete(TemporaryRegistration reg) {
        String sql = "DELETE FROM temporary_registrations WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, reg.getId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return false;
    }

    @Override
    public List<TemporaryRegistration> findAll() {
        List<TemporaryRegistration> list = new ArrayList<>();
        String sql = "SELECT * FROM temporary_registrations";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TemporaryRegistration reg = extractRegistrationFromResultSet(rs);
                list.add(reg);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return list;
    }

    @Override
    public List<TemporaryRegistration> findByCitizenId(String citizenId) {
        List<TemporaryRegistration> list = new ArrayList<>();
        String sql = """
            SELECT tr.* FROM temporary_registrations tr
            JOIN persons p ON tr.person_id = p.id
            WHERE p.citizen_id = ?
        """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, citizenId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TemporaryRegistration reg = extractRegistrationFromResultSet(rs);
                    list.add(reg);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }

        return list;
    }

    private TemporaryRegistration extractRegistrationFromResultSet(ResultSet rs) throws SQLException {
        TemporaryRegistration reg = new TemporaryRegistration(
                rs.getInt("person_id"),
                rs.getString("registration_type"),
                rs.getDate("start_date").toLocalDate(),
                rs.getDate("end_date") != null ? rs.getDate("end_date").toLocalDate() : null,
                rs.getString("address_of_stay"),
                rs.getString("reason_notes")
        );
        reg.setId(rs.getInt("id"));
        return reg;
    }
}
