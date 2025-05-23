package entity.resident;

import model.household.HouseholdModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.sql.SQLException;

public class Household {
    private int id;
    private String householdNumber;
    private Date registrationDate;
    private int ownerId;        // ID của chủ hộ (chuho_id trong CSDL)
    private String ownerName;   // Có thể để để hiển thị, không dùng để lưu DB trực tiếp

    private final HouseholdModel model = HouseholdModel.getInstance();

    public Household() {}

    /**
     * Constructor để tạo mới Household và lưu vào DB luôn
     * @param householdNumber
     * @param registrationDate
     * @param ownerId
     * @throws SQLException nếu có lỗi khi thao tác DB
     */
    public Household(String householdNumber, Date registrationDate, int ownerId) throws SQLException {
        this.householdNumber = householdNumber;
        this.registrationDate = registrationDate;
        this.ownerId = ownerId;

        Household existing = model.getHouseholdByNumber(householdNumber);
        if (existing != null) {
            // Số hộ khẩu đã tồn tại → lấy ID từ DB
            this.id = existing.getId();
        } else {
            // Số hộ khẩu chưa có → thêm vào CSDL và lấy ID mới
            model.insertHousehold(this); // Gọi insertHousehold, bên trong sẽ setId(this)
        }
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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) throws SQLException {
        this.ownerName = ownerName;

    }

    // --- Các method thao tác DB ---
    /**
     * Cập nhật dữ liệu household trong database
     */
    public boolean update() throws SQLException {
        if (this.id == 0) {
            throw new SQLException("Household chưa được lưu trong database, không thể cập nhật");
        }
        return model.updateHousehold(this);
    }

    /**
     * Xóa household khỏi database
     */
    public boolean delete() throws SQLException {
        if (this.id == 0) {
            throw new SQLException("Household chưa được lưu trong database, không thể xóa");
        }
        return model.deleteHousehold(this.id);
    }

    /**
     * Lấy danh sách id các cư dân thuộc household này
     * @return danh sách id cư dân (nhankhauid)
     * @throws SQLException
     */
    public List<Integer> getResidentIds() throws SQLException {
        if (this.id == 0) {
            throw new SQLException("Household chưa được lưu trong database, không thể lấy danh sách cư dân");
        }
        return model.getResidentsByHouseholdId(this.id);
    }
}

