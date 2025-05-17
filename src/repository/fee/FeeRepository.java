package repository.fee;

import java.util.List;

import entity.fee.FeeItem;

public interface FeeRepository {
    List<FeeItem> findAll();
    FeeItem findById(int id);
    void save(FeeItem feeItem);
    void update(FeeItem feeItem);
    void delete(int id);
}
