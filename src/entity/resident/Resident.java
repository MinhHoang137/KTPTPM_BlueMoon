package entity.resident;

import model.resident.ResidentModel;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Resident {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

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

    // Constructor trống
    public Resident() {}

    // Constructor đầy đủ, KHÔNG tự động thêm vào DB
    public Resident(String fullName, Date birthDate, String gender, String ethnicity, String religion,
                    String identityNumber, String occupation, Date issueDate, String issuePlace, int householdId) throws Exception {
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.ethnicity = ethnicity;
        this.religion = religion;
        this.identityNumber = identityNumber;
        this.occupation = occupation;
        this.issueDate = issueDate;
        this.issuePlace = issuePlace;
        this.householdId = householdId;

        ResidentModel model = ResidentModel.getInstance();

        Resident existing = model.getResidentByCCCD(identityNumber);
        if (existing != null) {
            // CCCD đã tồn tại → lấy ID từ DB
            this.id = existing.getId();
        } else {
            // CCCD chưa có → thêm vào CSDL và lấy ID mới
            model.insertResident(this); // Gọi insertResident, bên trong sẽ setId(this)
        }
    }


    // Constructor đơn giản với tên và số hộ khẩu
    public Resident(String fullName, int householdNumber) {
        this.fullName = fullName;
        this.householdId = householdNumber;
    }

    // Getter & Setter
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) {
        this.fullName = fullName;
        //updateToDatabase();
    }

    public Date getBirthDate() { return birthDate; }
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
        //updateToDatabase();
    }

    public String getGender() { return gender; }
    public void setGender(String gender) {
        this.gender = gender;
        //updateToDatabase();
    }

    public String getEthnicity() { return ethnicity; }
    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
        //updateToDatabase();
    }

    public String getReligion() { return religion; }
    public void setReligion(String religion) {
        this.religion = religion;
        //updateToDatabase();
    }

    public String getIdentityNumber() { return identityNumber; }
    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
        //updateToDatabase();
    }

    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) {
        this.occupation = occupation;
        //updateToDatabase();
    }

    public Date getIssueDate() { return issueDate; }
    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
        //updateToDatabase();
    }

    public String getIssuePlace() { return issuePlace; }
    public void setIssuePlace(String issuePlace) {
        this.issuePlace = issuePlace;
        //updateToDatabase();
    }

    public int getId() { return id; }
    public void setId(int id) {
        this.id = id;
    }

    public int getHouseholdId() { return householdId; }
    public void setHouseholdId(int householdId) {
        this.householdId = householdId;
        //updateToDatabase();
    }

    public boolean insertToDatabase() throws SQLException {
        return ResidentModel.getInstance().insertResident(this);
    }

    public boolean updateToDatabase() {
        // Chỉ cập nhật nếu id đã được thiết lập (tức là tồn tại trong DB)
        if (id > 0) {
            return ResidentModel.getInstance().updateResident(this);
        }
        return false;
    }

    public boolean delete() {
        if (id > 0) {
            return ResidentModel.getInstance().deleteResident(this);
        }
        return false;
    }
    private int getIdFromDatabase(String identityNumber) {
        // Giả sử bạn có một phương thức trong ResidentModel để lấy ID từ CCCD
        ResidentModel model = ResidentModel.getInstance();
        Resident resident = model.getResidentByCCCD(identityNumber);
        return (resident != null) ? resident.getId() : -1;
    }

    public String getBirthDateString() {
        return (birthDate != null) ? sdf.format(birthDate) : "";
    }

    public String getIssueDateString() {
        return (issueDate != null) ? sdf.format(issueDate) : "";
    }

}
