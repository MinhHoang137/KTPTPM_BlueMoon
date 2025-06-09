package entity.temporary;

import java.time.LocalDate;

public class TemporaryRegistration {
    private int id;
    private int personId;
    private  String citizenId; // Mã số công dân, có thể dùng để tra cứu
    private String registrationType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String addressOfStay;
    private String reasonNotes;

    // Constructor không bao gồm id
    public TemporaryRegistration(int personId, String registrationType, LocalDate startDate, LocalDate endDate,
                                 String addressOfStay, String reasonNotes) {
        this.personId = personId;
        this.registrationType = registrationType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.addressOfStay = addressOfStay;
        this.reasonNotes = reasonNotes;
    }

    public TemporaryRegistration(int personId, String citizenId, String registrationType,
                                 LocalDate startDate, LocalDate endDate, String addressOfStay, String reasonNotes) {
        this.personId = personId;
        this.citizenId = citizenId;
        this.registrationType = registrationType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.addressOfStay = addressOfStay;
        this.reasonNotes = reasonNotes;
    }

    // Getter và Setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }
    public String getCitizenId() {
        return citizenId;
    }
    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public String getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(String registrationType) {
        this.registrationType = registrationType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getAddressOfStay() {
        return addressOfStay;
    }

    public void setAddressOfStay(String addressOfStay) {
        this.addressOfStay = addressOfStay;
    }

    public String getReasonNotes() {
        return reasonNotes;
    }

    public void setReasonNotes(String reasonNotes) {
        this.reasonNotes = reasonNotes;
    }

}
