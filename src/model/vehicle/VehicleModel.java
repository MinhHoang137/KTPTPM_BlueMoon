package model.vehicle;

import entity.vehicle.Vehicle;
import repository.vehicle.VehicleRepository;
import repository.vehicle.VehicleRepositoryImpl;

import java.sql.SQLException;
import java.util.List;

public class VehicleModel {
    private VehicleRepository vehicleRepository;

    public VehicleModel() {
        this.vehicleRepository = new VehicleRepositoryImpl();
    }

    public VehicleModel(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<Vehicle> getAllVehicles() {
        try {
            return vehicleRepository.findAll();
        } catch (SQLException e) {
            System.err.println("Lỗi khi tải tất cả xe: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Vehicle getVehicleById(int id) {
        try {
            if (id <= 0) {
                System.err.println("ID xe không hợp lệ.");
                return null;
            }
            return vehicleRepository.findById(id);
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm xe theo ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Vehicle getVehicleByLicensePlate(String licensePlate) {
        try {
            if (licensePlate == null || licensePlate.isEmpty()) {
                System.err.println("Biển số xe không hợp lệ.");
                return null;
            }
            return vehicleRepository.findByLicensePlate(licensePlate);
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm xe theo biển số: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public boolean addVehicle(Vehicle vehicle) {
        if (vehicle.getLicensePlate() == null || vehicle.getLicensePlate().isEmpty() ||
            vehicle.getBrand() == null || vehicle.getBrand().isEmpty() ||
            vehicle.getModel() == null || vehicle.getModel().isEmpty()) {
            System.err.println("Thông tin xe không hợp lệ: Thiếu các trường bắt buộc.");
            return false;
        }
        try {
            if (vehicleRepository.findByLicensePlate(vehicle.getLicensePlate()) != null) {
                System.err.println("Biển số xe đã tồn tại.");
                return false;
            }
            return vehicleRepository.save(vehicle);
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm xe: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateVehicle(Vehicle vehicle) {
        if (vehicle.getId() <= 0 || vehicle.getLicensePlate() == null || vehicle.getLicensePlate().isEmpty()) {
            System.err.println("ID xe hoặc Biển số không hợp lệ để cập nhật.");
            return false;
        }
        try {
            return vehicleRepository.update(vehicle);
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật xe: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteVehicle(int id) {
        if (id <= 0) {
            System.err.println("ID xe không hợp lệ để xóa.");
            return false;
        }
        try {
            return vehicleRepository.delete(id);
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa xe: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Vehicle> getVehiclesByOwnerPersonId(int ownerPersonId) {
        try {
            if (ownerPersonId <= 0) {
                System.err.println("ID chủ sở hữu không hợp lệ.");
                return null;
            }
            return vehicleRepository.findByOwnerPersonId(ownerPersonId);
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm xe theo ID chủ sở hữu: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}