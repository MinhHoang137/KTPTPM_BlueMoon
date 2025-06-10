package entity.resident;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.sql.SQLException;

public class Household {
    private int id;
    private String householdNumber;
    private Date registrationDate;
    private int ownerId;
    private Resident owner;
    private String addressNumber;
    private ArrayList<Resident> members = new ArrayList<>();

    public Household() {}

    /**
     * Constructor để tạo mới Household và lưu vào DB luôn
     * @param householdNumber
     * @param registrationDate
     * @param ownerId
     * @throws SQLException nếu có lỗi khi thao tác DB
     */
    public Household(String householdNumber, Date registrationDate, int ownerId)  {
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

    public void setHouseholdNumber(String householdNumber) throws SQLException {
        this.householdNumber = householdNumber;

    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) throws SQLException {
        this.registrationDate = registrationDate;

    }
    public String getFormattedRegistrationDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(registrationDate);
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) throws SQLException {
        this.ownerId = ownerId;

    }
    public Resident getOwner() {
        return owner;
    }
    public void setOwner(Resident owner) {
        this.owner = owner;
    }
    public String getAddressNumber() {
        return addressNumber;
    }
    public void setAddressNumber(String addressNumber) {
        this.addressNumber = addressNumber;
    }
    public ArrayList<Resident> getMembers() {
        return members;
    }
    public void setMembers(ArrayList<Resident> members) {
        this.members = members;
    }
}

