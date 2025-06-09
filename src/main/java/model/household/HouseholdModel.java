package model.household;

import entity.resident.Household;
import repository.household.HouseholdRepository;
import repository.household.IHouseholdRepository;

import java.sql.SQLException;
import java.util.List;

public class HouseholdModel {

    private static HouseholdModel instance;
    private final IHouseholdRepository householdRepository;

    private HouseholdModel() {
        this.householdRepository = HouseholdRepository.getInstance();
    }

    public static HouseholdModel getInstance() {
        if (instance == null) {
            synchronized (HouseholdModel.class) {
                if (instance == null) {
                    instance = new HouseholdModel();
                }
            }
        }
        return instance;
    }

    // Create
    public int insertHousehold(Household household) throws SQLException {
        return householdRepository.insertHousehold(household);
    }

    // Update
    public boolean updateHousehold(Household household) throws SQLException {
        return householdRepository.updateHousehold(household);
    }

    // Delete
    public boolean deleteHousehold(int householdId) throws SQLException {
        return householdRepository.deleteHousehold(householdId);
    }

    // Get all households
    public List<Household> getAllHouseholds() throws SQLException {
        return householdRepository.getAllHouseholds();
    }

    // Get by household registration number
    public Household getHouseholdByNumber(String householdNumber) throws SQLException {
        return householdRepository.getHouseholdByNumber(householdNumber);
    }

    // Get by household ID
    public Household getHouseholdById(int householdId) throws SQLException {
        return householdRepository.getHouseholdById(householdId);
    }

    // Get resident IDs by household ID
    public List<Integer> getResidentsByHouseholdId(int householdId) throws SQLException {
        return householdRepository.getResidentsByHouseholdId(householdId);
    }
}
