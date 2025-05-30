package repository.resident;

import entity.resident.Resident;

import java.sql.SQLException;
import java.util.List;

public interface IResidentRepository {

    boolean insertResident(Resident resident) throws SQLException;

    boolean updateResident(Resident resident);

    boolean updateResidentByCCCD(Resident resident);

    boolean deleteResident(Resident resident);

    List<Resident> getAllResidents();

    Resident getResidentByCCCD(String cccd);

    List<Resident> getResidentsByName(String name);

    Resident getResidentById(int id);
}
