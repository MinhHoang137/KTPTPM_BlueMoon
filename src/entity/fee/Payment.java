package entity.fee;

import java.util.Date;

public class Payment {
    private int id;
    private int householdId;
    private FeeItem feeItem; 
    private double amountPaid;
    private Date paymentDate;
    private String status;
    private Date createdAt;
    private Date updatedAt;

    public Payment() {}

    public Payment(int id, int householdId, double amountPaid, Date paymentDate, String status) {
        this.id = id;
        this.householdId = householdId;
        this.amountPaid = amountPaid;
        this.paymentDate = paymentDate;
        this.status = status;
        this.feeItem = null; 
    }


    public Payment(int id, int householdId, FeeItem feeItem, double amountPaid, Date paymentDate, String status, Date createdAt, Date updatedAt) {
        this.id = id;
        this.householdId = householdId;
        this.feeItem = feeItem;
        this.amountPaid = amountPaid;
        this.paymentDate = paymentDate;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getHouseholdId() { return householdId; }
    public void setHouseholdId(int householdId) { this.householdId = householdId; }

    public FeeItem getFeeItem() { return feeItem; }
    public void setFeeItem(FeeItem feeItem) { this.feeItem = feeItem; }

    public int getFeeItemId() {
        return (feeItem != null) ? feeItem.getId() : 0;
    }

    public double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(double amountPaid) { this.amountPaid = amountPaid; }

    public Date getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Date paymentDate) { this.paymentDate = paymentDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    public void markAsPaid() {
        this.status = "Paid";
        this.paymentDate = new Date();
    }
}