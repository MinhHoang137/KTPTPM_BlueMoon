package repository.household;

import entity.resident.Household;
import entity.resident.Resident;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface IHouseholdRepository {

    int insertHousehold(Household household) throws SQLException;

    boolean updateHousehold(Household household) throws SQLException;


    List<Household> getAllHouseholds() throws SQLException;
    Household getHouseholdByAddressNumber(String addressNumber) throws SQLException;

    Household getHouseholdByNumber(String householdNumber) throws SQLException;

    Household getHouseholdById(int householdId) throws SQLException;


    boolean deleteHousehold(Household household) throws SQLException;
}
