package repository.fee;

import entity.fee.Payment;
import java.util.List;

public interface PaymentRepository {
    List<Payment> findAll();
    Payment findById(int id);
    void save(Payment payment);
    void update(Payment payment);
    void delete(int id);

    List<Payment> findByHouseholdId(int householdId);
    List<Payment> findByFeeItemId(int feeItemId);

    List<Integer> findPaidHouseholdsByFeeItemId(int feeItemId);
    List<Integer> findUnpaidHouseholdsByFeeItemId(int feeItemId);

    double sumTotalPaidByFeeItemId(int feeItemId);
    int countUnpaidHouseholdsByFeeItemId(int feeItemId);
}
