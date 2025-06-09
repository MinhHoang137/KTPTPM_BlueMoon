package repository.household;

import entity.resident.Household;

import java.sql.SQLException;
import java.util.List;

public interface IHouseholdRepository {

    int insertHousehold(Household household) throws SQLException;

    boolean updateHousehold(Household household) throws SQLException;

    boolean deleteHousehold(int householdId) throws SQLException;

    List<Household> getAllHouseholds() throws SQLException;

    Household getHouseholdByNumber(String householdNumber) throws SQLException;

    Household getHouseholdById(int householdId) throws SQLException;

    List<Integer> getResidentsByHouseholdId(int householdId) throws SQLException;
}
