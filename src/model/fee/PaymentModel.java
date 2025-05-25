package model.fee;

import entity.fee.FeeItem;
import entity.fee.Payment;
import repository.fee.FeeRepository;
import repository.fee.PaymentRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentModel {
    private PaymentRepository paymentRepository;
    private FeeRepository feeRepository;

    public PaymentModel(PaymentRepository paymentRepository, FeeRepository feeRepository) {
        this.paymentRepository = paymentRepository;
        this.feeRepository = feeRepository;
    }

    public List<Payment> getAllPayment() {
        return paymentRepository.findAll();
    }

    public List<Payment> getPaymentById(int id) {
        return List.of(paymentRepository.findById(id));
    }

    public void createPayment(Payment payment) {
        paymentRepository.save(payment);
        updateAllPaymentStatusForFee(payment.getFeeItem().getId());
    }

    public void updatePayment(Payment payment) {
        paymentRepository.update(payment);
        updateAllPaymentStatusForFee(payment.getFeeItem().getId());
    }

    public boolean validatePayment(Payment payment) {
        return payment.getSoTienNop() > 0 && payment.getFeeItem() != null;
    }

    public void deletePayment(int id) {
        Payment payment = paymentRepository.findById(id);
        int feeId = payment.getFeeItem() != null ? payment.getFeeItem().getId() : -1;
        paymentRepository.delete(id);
        if (feeId != -1) {
            updateAllPaymentStatusForFee(feeId);
        }
    }

    public List<Integer> getPaidHouseholdsForFee(int feeItemId) {
        return paymentRepository.findPaidHouseholdsByFeeItemId(feeItemId);
    }

    public void validatePaymentStatusForFeeItem(int feeItemId, double soTienThu) {
        List<Payment> all = paymentRepository.findByFeeItemId(feeItemId);

        Map<Integer, Double> householdToAmount = new HashMap<>();
        for (Payment p : all) {
            int ho = p.getIdHousehold();
            householdToAmount.put(ho, householdToAmount.getOrDefault(ho, 0.0) + p.getSoTienNop());
        }

        for (Payment p : all) {
            int ho = p.getIdHousehold();
            double tongNop = householdToAmount.get(ho);
            String trangThai = (tongNop >= soTienThu) ? "Đã nộp" : "Thiếu";
            p.setTrangThai(trangThai);

            if (p.getNgayNop() == null) {
                p.setNgayNop(new Date());
            }

            if (p.getFeeItem() == null || p.getFeeItem().getSoTien() == 0) {
                FeeItem fullInfo = feeRepository.findById(feeItemId);
                p.setFeeItem(fullInfo);
            }

            paymentRepository.update(p);
        }
    }

    public void updateAllPaymentStatusForFee(int feeItemId) {
        FeeItem item = feeRepository.findById(feeItemId);
        if (item != null) {
            validatePaymentStatusForFeeItem(feeItemId, item.getSoTien());
        }
    }
}
