package model.temporary;

import entity.temporary.TemporaryRegistration;
import repository.temporary.ITempRepo;
import repository.temporary.TempRepo;

import java.util.List;

public class TempModel {
    private static TempModel instance;
    private final ITempRepo repo;

    private TempModel() {
        this.repo = TempRepo.getInstance(); // Singleton Repo
    }

    public static TempModel getInstance() {
        if (instance == null) {
            instance = new TempModel();
        }
        return instance;
    }

    public boolean add(TemporaryRegistration reg) {
        return repo.add(reg);
    }

    public boolean update(TemporaryRegistration reg) {
        return repo.update(reg);
    }

    public boolean delete(TemporaryRegistration reg) {
        return repo.delete(reg);
    }

    public List<TemporaryRegistration> findAll() {
        return repo.findAll();
    }

    public List<TemporaryRegistration> findByCitizenId(String citizenId) {
        return repo.findByCitizenId(citizenId);
    }
}
