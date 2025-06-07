package repository.temporary;

import entity.temporary.TemporaryRegistration;
import java.util.List;

public interface ITempRepo {
    boolean add(TemporaryRegistration reg) throws Exception;
    boolean update(TemporaryRegistration reg);
    boolean delete(TemporaryRegistration reg);
    List<TemporaryRegistration> findAll();
    List<TemporaryRegistration> findByCitizenId(String citizenId);
    TemporaryRegistration findById(int id);
}
