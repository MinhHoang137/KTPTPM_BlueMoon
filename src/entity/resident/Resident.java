package entity.resident;

import java.util.Date;

public class Resident {
    private String fullName;
    private Date birthDate;
    private String gender;
    private String ethnicity;
    private String religion;
    private String identityNumber;
    private String occupation;
    private Date issueDate;
    private String issuePlace;
    private int id;
    private int householdId;

    public Resident() {}

    public Resident(String fullName, Date birthDate, String gender, String ethnicity, String religion,
                    String identityNumber, String occupation, Date issueDate, String issuePlace,
                    int id, int householdId) {
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.ethnicity = ethnicity;
        this.religion = religion;
        this.identityNumber = identityNumber;
        this.occupation = occupation;
        this.issueDate = issueDate;
        this.issuePlace = issuePlace;
        this.id = id;
        this.householdId = householdId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getIssuePlace() {
        return issuePlace;
    }

    public void setIssuePlace(String issuePlace) {
        this.issuePlace = issuePlace;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHouseholdId() {
        return householdId;
    }

    public void setHouseholdId(int householdId) {
        this.householdId = householdId;
    }
}
