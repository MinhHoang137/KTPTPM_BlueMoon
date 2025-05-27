package controller.temporary;

import entity.temporary.TemporaryRegistration;
import model.temporary.TempModel;

import java.util.List;

public class TempController {
    private static TempController instance;
    private final TempModel model;

    private TempController() {
        this.model = TempModel.getInstance();
    }

    public static TempController getInstance() {
        if (instance == null) {
            instance = new TempController();
        }
        return instance;
    }

    public boolean add(TemporaryRegistration reg) {
        return model.add(reg);
    }

    public boolean update(TemporaryRegistration reg) {
        return model.update(reg);
    }

    public boolean delete(TemporaryRegistration reg) {
        return model.delete(reg);
    }

    public List<TemporaryRegistration> findAll() {
        return model.findAll();
    }

    public List<TemporaryRegistration> findByCitizenId(String citizenId) {
        return model.findByCitizenId(citizenId);
    }
}
