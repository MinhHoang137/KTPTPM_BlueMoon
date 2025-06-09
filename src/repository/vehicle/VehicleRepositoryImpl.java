package repository.vehicle;

import entity.vehicle.Vehicle;
import model.BaseModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VehicleRepositoryImpl extends BaseModel implements VehicleRepository {

    public VehicleRepositoryImpl() {
        super();
    }

    private Vehicle extractVehicle(ResultSet rs) throws SQLException {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(rs.getInt("id"));
        vehicle.setOwnerPersonId(rs.getInt("owner_person_id"));
        vehicle.setBrand(rs.getString("brand"));
        vehicle.setModel(rs.getString("model"));
        vehicle.setLicensePlate(rs.getString("license_plate"));
        vehicle.setColor(rs.getString("color"));
        vehicle.setImagePath(rs.getString("image_path"));
        vehicle.setCreatedAt(rs.getTimestamp("created_at"));
        vehicle.setUpdatedAt(rs.getTimestamp("updated_at"));
        return vehicle;
    }

    @Override
    public Vehicle findById(int id) throws SQLException {
        String sql = "SELECT * FROM vehicles WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractVehicle(rs);
            }
        } finally {
            closeConnection();
        }
        return null;
    }

    @Override
    public Vehicle findByLicensePlate(String licensePlate) throws SQLException {
        String sql = "SELECT * FROM vehicles WHERE license_plate = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, licensePlate);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractVehicle(rs);
            }
        } finally {
            closeConnection();
        }
        return null;
    }

    @Override
    public List<Vehicle> findAll() throws SQLException {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                vehicles.add(extractVehicle(rs));
            }
        } finally {
            closeConnection();
        }
        return vehicles;
    }

    @Override
    public boolean save(Vehicle vehicle) throws SQLException {
        String sql = "INSERT INTO vehicles (owner_person_id, brand, model, license_plate, color, image_path) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, vehicle.getOwnerPersonId());
            stmt.setString(2, vehicle.getBrand());
            stmt.setString(3, vehicle.getModel());
            stmt.setString(4, vehicle.getLicensePlate());
            stmt.setString(5, vehicle.getColor());
            stmt.setString(6, vehicle.getImagePath());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    vehicle.setId(generatedKeys.getInt(1));
                }
            }
            return true;
        } finally {
            closeConnection();
        }
    }

    @Override
    public boolean update(Vehicle vehicle) throws SQLException {
        String sql = "UPDATE vehicles SET owner_person_id = ?, brand = ?, model = ?, license_plate = ?, color = ?, image_path = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vehicle.getOwnerPersonId());
            stmt.setString(2, vehicle.getBrand());
            stmt.setString(3, vehicle.getModel());
            stmt.setString(4, vehicle.getLicensePlate());
            stmt.setString(5, vehicle.getColor());
            stmt.setString(6, vehicle.getImagePath());
            stmt.setInt(7, vehicle.getId());
            return stmt.executeUpdate() > 0;
        } finally {
            closeConnection();
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM vehicles WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } finally {
            closeConnection();
        }
    }
    
    @Override
    public List<Vehicle> findByOwnerPersonId(int ownerPersonId) throws SQLException {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles WHERE owner_person_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ownerPersonId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                vehicles.add(extractVehicle(rs));
            }
        } finally {
            closeConnection();
        }
        return vehicles;
    }
}