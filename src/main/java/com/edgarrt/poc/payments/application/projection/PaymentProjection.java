package com.edgarrt.poc.payments.application.projection;

import com.edgarrt.poc.payments.domain.event.*;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component("paymentProjection")
public class PaymentProjection {
    private final PaymentViewRepository repository;
    public PaymentProjection(PaymentViewRepository repository) { this.repository = repository; }
    @EventHandler public void on(PaymentCreatedEvent e) { repository.save(new PaymentView(e.paymentId(), e.customerId(), e.merchantId(), e.amount(), e.currency(), "CREATED")); }
    @EventHandler public void on(FundsReservedEvent e) { update(e.paymentId(), "FUNDS_RESERVED"); }
    @EventHandler public void on(FraudApprovedEvent e) { update(e.paymentId(), "FRAUD_APPROVED"); }
    @EventHandler public void on(PaymentCapturedEvent e) { update(e.paymentId(), "CAPTURED"); }
    @EventHandler public void on(FundsReservationFailedEvent e) { fail(e.paymentId(), "RESERVATION_FAILED", e.reason()); }
    @EventHandler public void on(FraudRejectedEvent e) { fail(e.paymentId(), "FRAUD_REJECTED", e.reason()); }
    @EventHandler public void on(FundsReleasedEvent e) { fail(e.paymentId(), "FUNDS_RELEASED", e.reason()); }
    @EventHandler public void on(PaymentCancelledEvent e) { fail(e.paymentId(), "CANCELLED", e.reason()); }
    private void update(String id, String status) { var v = repository.findById(id).orElseThrow(); v.updateStatus(status); repository.save(v); }
    private void fail(String id, String status, String reason) { var v = repository.findById(id).orElseThrow(); v.fail(status, reason); repository.save(v); }
}
