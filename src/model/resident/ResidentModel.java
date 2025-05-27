package model.resident;

import entity.resident.Resident;
import repository.resident.IResidentRepository;
import repository.resident.ResidentRepository;

import java.sql.SQLException;
import java.util.List;

public class ResidentModel {

    private static ResidentModel instance;
    private final IResidentRepository residentRepository;

    private ResidentModel() {
        this.residentRepository = ResidentRepository.getInstance();
    }

    public static ResidentModel getInstance() {
        if (instance == null) {
            instance = new ResidentModel();
        }
        return instance;
    }

    public boolean addResident(Resident resident) {
        try {
            return residentRepository.insertResident(resident);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateResident(Resident resident) {
        return residentRepository.updateResident(resident);
    }

    public boolean updateResidentByCCCD(Resident resident) {
        return residentRepository.updateResidentByCCCD(resident);
    }

    public boolean deleteResident(Resident resident) {
        return residentRepository.deleteResident(resident);
    }

    public List<Resident> getAllResidents() {
        return residentRepository.getAllResidents();
    }

    public Resident getResidentByCCCD(String cccd) {
        return residentRepository.getResidentByCCCD(cccd);
    }

    public List<Resident> getResidentsByName(String name) {
        return residentRepository.getResidentsByName(name);
    }

    public Resident getResidentById(int id) {
        return residentRepository.getResidentById(id);
    }
}
