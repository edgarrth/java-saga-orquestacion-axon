package com.edgarrt.poc.payments.application.projection;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "payment_view")
public class PaymentView {
    @Id
    private String paymentId;
    private String customerId;
    private String merchantId;
    private BigDecimal amount;
    private String currency;
    private String status;
    private String failureReason;
    private Instant createdAt;
    private Instant updatedAt;

    protected PaymentView() {}
    public PaymentView(String paymentId, String customerId, String merchantId, BigDecimal amount, String currency, String status) {
        this.paymentId = paymentId; this.customerId = customerId; this.merchantId = merchantId; this.amount = amount; this.currency = currency; this.status = status; this.createdAt = Instant.now(); this.updatedAt = Instant.now();
    }
    public void updateStatus(String status) { this.status = status; this.updatedAt = Instant.now(); }
    public void fail(String status, String reason) { this.status = status; this.failureReason = reason; this.updatedAt = Instant.now(); }
    public String getPaymentId(){return paymentId;} public String getCustomerId(){return customerId;} public String getMerchantId(){return merchantId;} public BigDecimal getAmount(){return amount;} public String getCurrency(){return currency;} public String getStatus(){return status;} public String getFailureReason(){return failureReason;} public Instant getCreatedAt(){return createdAt;} public Instant getUpdatedAt(){return updatedAt;}
}
