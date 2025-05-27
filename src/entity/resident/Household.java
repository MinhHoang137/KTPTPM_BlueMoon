package entity.resident;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Household {
    private int id;
    private String householdNumber;
    private Date registrationDate;
    private int ownerId;        // ID của chủ hộ
    private String ownerName;   // Dùng để hiển thị, không lưu DB

    public Household() {}

    public Household(String householdNumber, Date registrationDate, int ownerId) {
        this.householdNumber = householdNumber;
        this.registrationDate = registrationDate;
        this.ownerId = ownerId;
    }

    // --- Getters/Setters ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHouseholdNumber() {
        return householdNumber;
    }

    public void setHouseholdNumber(String householdNumber) {
        this.householdNumber = householdNumber;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getFormattedRegistrationDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(registrationDate);
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
