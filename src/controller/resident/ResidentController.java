package controller.resident;

import entity.resident.Resident;
import model.resident.ResidentModel;
import repository.resident.ResidentRepository;

import java.util.Date;
import java.util.List;

public class ResidentController {
    private static ResidentController instance;

    private ResidentController() {
    }

    public static ResidentController getInstance() {
        if (instance == null) {
            instance = new ResidentController();
        }
        return instance;
    }

    // CREATE
    public boolean addResident(String name, String cccd, String gender, Date birthDate,
            String ethnicity, String religion, String occupation,
            Date issueDate, String issuePlace, int householdId) throws Exception {
        if (name == null || name.isBlank() || cccd == null || cccd.isBlank()) {
            System.out.println("Tên hoặc CCCD không hợp lệ. Không thể thêm cư dân.");
            return false;
        }
        if (ResidentRepository.getInstance().getResidentByCCCD(cccd) != null) {
            System.out.println("Cư dân với CCCD này đã tồn tại.");
            return false;
        }
        try {
            Resident res = new Resident(name, birthDate, gender, ethnicity, religion, cccd,
                    occupation, issueDate, issuePlace, householdId);
            return ResidentModel.getInstance().addResident(res);
        } catch (Exception e) {
            System.out.println("Lỗi khi thêm cư dân: " + e.getMessage());
        }
        return false;
    }

    public Resident getResidentByCccd(String cccd) {
        return ResidentModel.getInstance().getResidentByCCCD(cccd);
    }

    public List<Resident> searchResidentsByName(String name) {
        return ResidentModel.getInstance().getResidentsByName(name);
    }

    public List<Resident> getAllResidents() {
        return ResidentRepository.getInstance().getAllResidents();
    }

    // UPDATE
    public boolean updateResident(String cccd, String name, String gender, Date birthDate,
            String ethnicity, String religion, String occupation,
            Date issueDate, String issuePlace, int householdId) {

        Resident resident = ResidentRepository.getInstance().getResidentByCCCD(cccd);
        if (resident == null) {
            System.out.println("Không tìm thấy cư dân có CCCD: " + cccd);
            return false;
        }

        if (name != null && !name.isBlank())
            resident.setFullName(name);
        if (gender != null && !gender.isBlank())
            resident.setGender(gender);
        if (birthDate != null)
            resident.setBirthDate(birthDate);
        if (ethnicity != null && !ethnicity.isBlank())
            resident.setEthnicity(ethnicity);
        if (religion != null && !religion.isBlank())
            resident.setReligion(religion);
        if (occupation != null && !occupation.isBlank())
            resident.setOccupation(occupation);
        if (issueDate != null)
            resident.setIssueDate(issueDate);
        if (issuePlace != null && !issuePlace.isBlank())
            resident.setIssuePlace(issuePlace);
        if (householdId > 0)
            resident.setHouseholdId(householdId);
        return ResidentModel.getInstance().updateResident(resident);
    }

    public boolean removeResidentByCccd(String cccd) {
        Resident resident = ResidentRepository.getInstance().getResidentByCCCD(cccd);
        if (resident == null) {
            System.out.println("Không tìm thấy cư dân với CCCD: " + cccd);
            return false;
        }

        boolean success = ResidentModel.getInstance().deleteResident(resident);
        if (success) {
            System.out.println("Xóa cư dân thành công.");
        } else {
            System.out.println("Xóa cư dân thất bại.");
        }
        return success;
    }
}