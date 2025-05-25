package model.fee;

import repository.fee.FeeRepository;
import repository.fee.PaymentRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

import entity.fee.FeeItem;
import entity.fee.Payment;

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

    public List<FeeItem> getAllFeeItems() {
        return feeRepository.findAll();
    }

    public FeeItem getFeeItemById(int id) {
        return feeRepository.findById(id);
    }

    public void createFeeItem(FeeItem feeItem) {
        if (validateFeeItem(feeItem)) {
            feeRepository.save(feeItem);
        }
    }

    public void updateFeeItem(FeeItem feeItem) {
        if (validateFeeItem(feeItem)) {
            feeRepository.update(feeItem);
        }
    }

    public void deleteFeeItem(int id) {
        feeRepository.delete(id);
    }

    public boolean validateFeeItem(FeeItem feeItem) {
        return feeItem.getAmount() > 0 && feeItem.getFeeName() != null;
    }

    public double getTotalPaidForFeeItem(int feeItemId) throws Exception{
        if (paymentRepository == null) return 0;
        List<Payment> payments = paymentRepository.findByFeeItemId(feeItemId);
        return payments.stream().mapToDouble(Payment::getAmountPaid).sum();
    }

    public double getTotalExpectedForFeeItem(int feeItemId) {
        FeeItem fee = feeRepository.findById(feeItemId);
        return fee != null ? fee.getAmount() * 10 : 0;
    }

    public double getTotalMissingForFeeItem(int feeItemId) throws Exception {
        return getTotalExpectedForFeeItem(feeItemId) - getTotalPaidForFeeItem(feeItemId);
    }

    public List<Integer> getPaidHouseholdsForFeeItem(int feeItemId) throws Exception{
        if (paymentRepository == null) return List.of();

        List<Payment> payments = paymentRepository.findByFeeItemId(feeItemId);
        Set<Integer> result = new HashSet<>();

        FeeItem fee = feeRepository.findById(feeItemId);
        if (fee == null) return List.of();
        double requiredAmount = fee.getAmount();

        Map<Integer, Double> householdTotalPaid = new HashMap<>();
        for (Payment p : payments) {
            householdTotalPaid.put(p.getHouseholdId(), householdTotalPaid.getOrDefault(p.getHouseholdId(), 0.0) + p.getAmountPaid());
        }

        for (var entry : householdTotalPaid.entrySet()) {
            if (entry.getValue() >= requiredAmount) {
                result.add(entry.getKey());
            }
        }

        return new ArrayList<>(result);
    }

    public List<Integer> getUnpaidHouseholdsForFeeItem(int feeItemId) throws Exception{
        Set<Integer> allHouseholds = new HashSet<>();
        for (int i = 1; i <= 10; i++) allHouseholds.add(i);

        List<Integer> paidHouseholds = getPaidHouseholdsForFeeItem(feeItemId);
        allHouseholds.removeAll(paidHouseholds);

        return new ArrayList<>(allHouseholds);
    }
}