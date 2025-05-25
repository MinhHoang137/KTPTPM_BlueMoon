package model.fee;

import repository.fee.FeeRepository;
import repository.fee.PaymentRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import entity.fee.FeeItem;
import entity.fee.Payment;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Set;

public class FeeModel {
    private FeeRepository feeRepository;
    private PaymentRepository paymentRepository;

    public FeeModel(FeeRepository feeRepository) {
        this.feeRepository = feeRepository;
    }

    public FeeModel(FeeRepository feeRepository, PaymentRepository paymentRepository) {
        this.feeRepository = feeRepository;
        this.paymentRepository = paymentRepository;
    }

    public List<FeeItem> getAllFees() {
        return feeRepository.findAll();
    }

    public FeeItem getFeeById(int id) {
        return feeRepository.findById(id);
    }

    public void createFee(FeeItem feeItem) {
        if (validateFee(feeItem)) {
            feeRepository.save(feeItem);
        }
    }

    public void updateFee(FeeItem feeItem) {
        if (validateFee(feeItem)) {
            feeRepository.update(feeItem);
        }
    }

    public void deleteFee(int id) {
        feeRepository.delete(id);
    }

    public boolean validateFee(FeeItem feeItem) {
        return feeItem.getSoTien() > 0 && feeItem.getTenKhoanThu() != null;
    }

    public double getTotalPaidForFee(int feeItemId) {
        if (paymentRepository == null) return 0;
        List<Payment> payments = paymentRepository.findByFeeItemId(feeItemId);
        return payments.stream().mapToDouble(Payment::getSoTienNop).sum();
    }

    public double getTotalExpectedForFee(int feeItemId) {
        FeeItem fee = feeRepository.findById(feeItemId);
        return fee != null ? fee.getSoTien() * 10 : 0; // Giả định có 10 hộ
    }

    public double getTotalMissingForFee(int feeItemId) {
        return getTotalExpectedForFee(feeItemId) - getTotalPaidForFee(feeItemId);
    }

    public List<Integer> getPaidHouseholdsForFee(int feeItemId) {
        if (paymentRepository == null) return List.of();

        List<Payment> payments = paymentRepository.findByFeeItemId(feeItemId);
        Set<Integer> result = new HashSet<>();

        FeeItem fee = feeRepository.findById(feeItemId);
        if (fee == null) return List.of();
        double soTienCanThu = fee.getSoTien();

        // Tính tổng mỗi hộ đã nộp bao nhiêu
        Map<Integer, Double> tongTienHo = new HashMap<>();
        for (Payment p : payments) {
            tongTienHo.put(p.getIdHousehold(), tongTienHo.getOrDefault(p.getIdHousehold(), 0.0) + p.getSoTienNop());
        }

        for (var entry : tongTienHo.entrySet()) {
            if (entry.getValue() >= soTienCanThu) {
                result.add(entry.getKey());
            }
        }

        return new ArrayList<>(result);
    }

    public List<Integer> getUnpaidHouseholdsForFee(int feeItemId) {
        Set<Integer> allHouseholds = new HashSet<>();
        for (int i = 1; i <= 10; i++) allHouseholds.add(i);

        List<Integer> paid = getPaidHouseholdsForFee(feeItemId);
        allHouseholds.removeAll(paid);

        return new ArrayList<>(allHouseholds);
    }
}  
