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
    public boolean addHousehold(String householdNumber, int ownerId, Date registrationDate) {
        if (householdNumber == null || householdNumber.isBlank() || ownerId <= 0) {
            System.out.println("Số hộ hoặc ID chủ hộ không hợp lệ. Không thể thêm hộ gia đình.");
            return false;
        }
        try {
            Household household = new Household(householdNumber, registrationDate, ownerId);
            //System.out.println("Thêm hộ gia đình thành công.");
            return HouseholdModel.getInstance().insertHousehold(household) != 0;
        } catch (Exception e) {
            System.out.println("Lỗi khi thêm hộ gia đình: " + e.getMessage());
        }
        return false;
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

    //update
    public boolean updateHouseholdByNumber(String householdNumber, int ownerId, Date registrationDate) throws SQLException {
        if (householdNumber == null || householdNumber.isBlank() || ownerId <= 0) {
            System.out.println("Số hộ hoặc ID chủ hộ không hợp lệ. Không thể cập nhật hộ gia đình.");
            return false;
        }
        try {
            Household household = new Household(householdNumber, registrationDate, ownerId);
            return HouseholdModel.getInstance().updateHousehold(household);
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật hộ gia đình: " + e.getMessage());
        }
        return false;
    }

    //Delete
    public boolean deleteHouseholdByNumber(String householdNumber) throws SQLException {
        if (householdNumber == null || householdNumber.isBlank()) {
            System.out.println("Số hộ không hợp lệ. Không thể xóa hộ gia đình.");
            return false;
        }
        try {
            Household household = HouseholdModel.getInstance().getHouseholdByNumber(householdNumber);
            if (household != null) {
                return HouseholdModel.getInstance().deleteHousehold(household.getId());
            } else {
                System.out.println("Hộ gia đình không tồn tại.");
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi xóa hộ gia đình: " + e.getMessage());
        }
        return false;
    }
}
