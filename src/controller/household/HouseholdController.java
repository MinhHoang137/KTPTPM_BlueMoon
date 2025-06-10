/**
 * HouseholdController.java
 * This class is responsible for managing household data.
 * It provides methods to add, remove, and retrieve household information.
 * It uses a singleton pattern to ensure that only one instance of the controller exists.
 * Created by Minh Hoang.
 */

package controller.household;

import entity.resident.Household;
import model.household.HouseholdModel;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class HouseholdController {
    private static  HouseholdController instance;

    private HouseholdController(){}
    public static HouseholdController getInstance() {
        if (instance == null) {
            instance = new HouseholdController();
        }
        return instance;
    }

    //Create
    public int addHousehold(Household household) throws SQLException {
        return HouseholdModel.getInstance().insertHousehold(household);
    }

    //Read
    public List<Household> getAllHouseholds() throws SQLException {
        return HouseholdModel.getInstance().getAllHouseholds();
    }
    public Household getHouseholdByNumber(String householdNumber) throws SQLException {
        return HouseholdModel.getInstance().getHouseholdByNumber(householdNumber);
    }
    public Household getHouseholdById(int id) throws SQLException {
        return HouseholdModel.getInstance().getHouseholdById(id);
    }
    public Household getHouseholdByAddress(String addressNumber) throws SQLException {
        return HouseholdModel.getInstance().getHouseholdByAddress(addressNumber);
    }
    public Household getHouseholdsByAddress(String address) {
        try {
            return HouseholdModel.getInstance().getHouseholdByAddress(address);
        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy hộ gia đình theo địa chỉ: " + e.getMessage());
            return null;
        }
    }
    //update
    public boolean updateHousehold(Household household) throws SQLException {
        if (household == null || household.getHouseholdNumber() == null || household.getHouseholdNumber().isBlank()) {
            System.out.println("Thông tin hộ gia đình không hợp lệ. Không thể cập nhật.");
            return false;
        }
        try {
            return HouseholdModel.getInstance().updateHousehold(household);
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật hộ gia đình: " + e.getMessage());
            return false;
        }
    }

    //Delete
    public boolean deleteHousehold(Household household) throws SQLException {
        if (household == null || household.getId() <= 0) {
            System.out.println("Thông tin hộ gia đình không hợp lệ. Không thể xóa.");
            return false;
        }
        try {
            return HouseholdModel.getInstance().deleteHousehold(household);
        } catch (Exception e) {
            System.out.println("Lỗi khi xóa hộ gia đình: " + e.getMessage());
            return false;
        }
    }


}
