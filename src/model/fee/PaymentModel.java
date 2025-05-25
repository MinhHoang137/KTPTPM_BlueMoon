package model.fee;

import entity.fee.FeeItem;
import entity.fee.Payment;
import repository.fee.FeeRepository;
import repository.fee.PaymentRepository;

import java.sql.SQLException;
import java.util.List;

public class PaymentModel {
    private PaymentRepository paymentRepository;
    private FeeRepository feeRepository;

    public PaymentModel(PaymentRepository paymentRepository, FeeRepository feeRepository) {
        this.paymentRepository = paymentRepository;
        this.feeRepository = feeRepository;
    }

    public List<Payment> getAllPayments() {
        try {
            return paymentRepository.findAll();
        } catch (SQLException e) {
            System.err.println("Lỗi khi tải tất cả khoản nộp: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Payment getPaymentById(int id) {
        try {
            return paymentRepository.findById(id);
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm khoản nộp theo ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public boolean createPayment(Payment payment) { 
        if (payment.getHouseholdId() <= 0 || payment.getFeeItem() == null || payment.getFeeItem().getId() <= 0 || payment.getAmountPaid() < 0) {
            System.err.println("Thông tin khoản nộp không hợp lệ: ID Hộ khẩu, ID Khoản thu, hoặc số tiền.");
            return false;
        }
        try {
            boolean success = paymentRepository.save(payment);
            if (success) {
                
                updateAllPaymentStatusesForFeeItem(payment.getFeeItem().getId()); 
            }
            return success;
        } catch (SQLException e) {
            System.err.println("Lỗi CSDL khi thêm khoản nộp: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePayment(Payment payment) { 
        if (payment.getId() <= 0 || payment.getHouseholdId() <= 0 || payment.getFeeItem() == null || payment.getFeeItem().getId() <= 0 || payment.getAmountPaid() < 0) {
            System.err.println("ID hoặc thông tin khoản nộp không hợp lệ để cập nhật.");
            return false;
        }
        try {
            boolean success = paymentRepository.update(payment);
            if (success) {
                updateAllPaymentStatusesForFeeItem(payment.getFeeItem().getId());
            }
            return success;
        } catch (SQLException e) {
            System.err.println("Lỗi CSDL khi cập nhật khoản nộp: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePayment(int id) { 
        if (id <= 0) {
            System.err.println("ID khoản nộp không hợp lệ để xóa.");
            return false;
        }
        try {
            Payment paymentToDelete = paymentRepository.findById(id);
            if (paymentToDelete == null) {
                System.err.println("Không tìm thấy khoản nộp để xóa.");
                return false;
            }
            boolean success = paymentRepository.delete(id);
            if (success) {
                updateAllPaymentStatusesForFeeItem(paymentToDelete.getFeeItem().getId());
            }
            return success;
        } catch (SQLException e) {
            System.err.println("Lỗi CSDL khi xóa khoản nộp: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Payment> getPaymentsByHouseholdId(int householdId) {
        try {
            return paymentRepository.findByHouseholdId(householdId);
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm khoản nộp theo ID Hộ khẩu: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<Payment> getPaymentsByFeeItemId(int feeItemId) {
        try {
            return paymentRepository.findByFeeItemId(feeItemId);
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm khoản nộp theo ID Khoản thu: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateAllPaymentStatusesForFeeItem(int feeItemId) { 
        try {
            FeeItem item = feeRepository.findById(feeItemId);
            if (item != null) {
                List<Payment> paymentsForFeeItem = paymentRepository.findByFeeItemId(feeItemId);
                double requiredAmount = item.getAmount();

                for (Payment payment : paymentsForFeeItem) {
                    if (payment.getAmountPaid() >= requiredAmount) {
                        payment.setStatus("Đã nộp");
                    } else if (payment.getAmountPaid() > 0) {
                        payment.setStatus("Thiếu tiền");
                    } else {
                        payment.setStatus("Chưa nộp");
                    }
                    if (!paymentRepository.update(payment)) {
                        System.err.println("Không thể cập nhật trạng thái cho Payment ID: " + payment.getId());
                        return false;
                    }
                }
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi CSDL khi cập nhật trạng thái khoản nộp: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}