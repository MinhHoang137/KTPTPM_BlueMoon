package repository.vehicle;

import entity.vehicle.Vehicle;
import java.sql.SQLException; // Import SQLException
import java.util.List;

public interface VehicleRepository {
    Vehicle findById(int id) throws SQLException;
    Vehicle findByLicensePlate(String licensePlate) throws SQLException;
    List<Vehicle> findAll() throws SQLException;
    boolean save(Vehicle vehicle) throws SQLException;
    boolean update(Vehicle vehicle) throws SQLException;
    boolean delete(int id) throws SQLException;
    List<Vehicle> findByOwnerPersonId(int ownerPersonId) throws SQLException;
}