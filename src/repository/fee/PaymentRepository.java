package repository.fee;

import entity.fee.Payment;
import java.sql.SQLException;
import java.util.List;

public interface PaymentRepository {
    Payment findById(int id) throws SQLException;
    List<Payment> findAll() throws SQLException;
    boolean save(Payment payment) throws SQLException;
    boolean update(Payment payment) throws SQLException;
    boolean delete(int id) throws SQLException;
    List<Payment> findByHouseholdId(int householdId) throws SQLException;
    List<Payment> findByFeeItemId(int feeItemId) throws SQLException;
    double sumTotalPaidByFeeItemId(int feeItemId) throws SQLException;
    int countUnpaidHouseholdsByFeeItemId(int feeItemId) throws SQLException;
}