package entity.resident;

import java.util.Date;

public class Household {
    private String householdNumber;
    private Date registrationDate;
    private int id;

    public Household() {}

    public Household(String householdNumber, Date registrationDate, int id) {
        this.householdNumber = householdNumber;
        this.registrationDate = registrationDate;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Methods
    public void addResident(Resident resident) {
        // logic to add resident
    }

    public void removeResident(Resident resident) {
        // logic to remove resident
    }

    public void update() {
        // logic to update household
    }
}
