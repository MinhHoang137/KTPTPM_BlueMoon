package repository.temporary;

import entity.temporary.TemporaryRegistration;
import java.util.List;

public interface ITempRepo {
    boolean add(TemporaryRegistration reg);
    boolean update(TemporaryRegistration reg);
    boolean delete(TemporaryRegistration reg);
    List<TemporaryRegistration> findAll();
    List<TemporaryRegistration> findByCitizenId(String citizenId);
}
