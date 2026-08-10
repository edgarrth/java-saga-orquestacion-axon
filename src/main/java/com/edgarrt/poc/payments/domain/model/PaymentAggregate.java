package com.edgarrt.poc.payments.domain.model;

import com.edgarrt.poc.payments.domain.command.*;
import com.edgarrt.poc.payments.domain.event.*;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class PaymentAggregate {
    @AggregateIdentifier
    private String paymentId;
    private String customerId;
    private BigDecimal amount;
    private PaymentStatus status;

    protected PaymentAggregate() {}

    @CommandHandler
    public PaymentAggregate(CreatePaymentCommand command) {
        if (command.amount().compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Amount must be positive");
        if (!"PEN".equals(command.currency()) && !"USD".equals(command.currency())) throw new IllegalArgumentException("Currency must be PEN or USD");
        apply(new PaymentCreatedEvent(command.paymentId(), command.customerId(), command.merchantId(), command.amount(), command.currency()));
    }

    @CommandHandler
    public void handle(ReserveFundsCommand command) {
        requireStatus(PaymentStatus.CREATED);
        if (command.amount().compareTo(new BigDecimal("5000")) > 0) {
            apply(new FundsReservationFailedEvent(command.paymentId(), command.customerId(), "Insufficient simulated balance for large payment"));
        } else {
            apply(new FundsReservedEvent(command.paymentId(), command.customerId()));
        }
    }

    @CommandHandler
    public void handle(ApproveFraudCommand command) {
        requireStatus(PaymentStatus.FUNDS_RESERVED);
        apply(new FraudApprovedEvent(command.paymentId()));
    }

    @CommandHandler
    public void handle(RejectFraudCommand command) {
        requireStatus(PaymentStatus.FUNDS_RESERVED);
        apply(new FraudRejectedEvent(command.paymentId(), command.reason()));
    }

    @CommandHandler
    public void handle(CapturePaymentCommand command) {
        requireStatus(PaymentStatus.FRAUD_APPROVED);
        apply(new PaymentCapturedEvent(command.paymentId()));
    }

    @CommandHandler
    public void handle(ReleaseFundsCommand command) {
        if (status != PaymentStatus.FUNDS_RESERVED && status != PaymentStatus.FRAUD_REJECTED) {
            throw new IllegalStateException("Funds can only be released after reservation/fraud rejection");
        }
        apply(new FundsReleasedEvent(command.paymentId(), command.customerId(), command.reason()));
    }

    @CommandHandler
    public void handle(CancelPaymentCommand command) {
        if (status == PaymentStatus.CAPTURED) throw new IllegalStateException("Captured payment cannot be cancelled; use refund flow");
        apply(new PaymentCancelledEvent(command.paymentId(), command.reason()));
    }

    @EventSourcingHandler
    public void on(PaymentCreatedEvent event) { this.paymentId = event.paymentId(); this.customerId = event.customerId(); this.amount = event.amount(); this.status = PaymentStatus.CREATED; }
    @EventSourcingHandler
    public void on(FundsReservedEvent event) { this.status = PaymentStatus.FUNDS_RESERVED; }
    @EventSourcingHandler
    public void on(FundsReservationFailedEvent event) { this.status = PaymentStatus.RESERVATION_FAILED; }
    @EventSourcingHandler
    public void on(FraudApprovedEvent event) { this.status = PaymentStatus.FRAUD_APPROVED; }
    @EventSourcingHandler
    public void on(FraudRejectedEvent event) { this.status = PaymentStatus.FRAUD_REJECTED; }
    @EventSourcingHandler
    public void on(FundsReleasedEvent event) { this.status = PaymentStatus.FUNDS_RELEASED; }
    @EventSourcingHandler
    public void on(PaymentCapturedEvent event) { this.status = PaymentStatus.CAPTURED; }
    @EventSourcingHandler
    public void on(PaymentCancelledEvent event) { this.status = PaymentStatus.CANCELLED; }

    private void requireStatus(PaymentStatus expected) {
        if (status != expected) throw new IllegalStateException("Expected status " + expected + " but was " + status);
    }
}
