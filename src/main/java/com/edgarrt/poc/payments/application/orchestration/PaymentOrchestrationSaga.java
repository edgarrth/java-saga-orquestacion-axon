package com.edgarrt.poc.payments.application.orchestration;

import com.edgarrt.poc.payments.domain.command.*;
import com.edgarrt.poc.payments.domain.event.*;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

@Saga
public class PaymentOrchestrationSaga {
    @Autowired
    private transient CommandGateway commandGateway;

    private String customerId;

    @StartSaga
    @SagaEventHandler(associationProperty = "paymentId")
    public void on(PaymentCreatedEvent event) {
        this.customerId = event.customerId();
        SagaLifecycle.associateWith("customerId", event.customerId());
        commandGateway.send(new ReserveFundsCommand(event.paymentId(), event.customerId(), event.amount()));
    }

    @SagaEventHandler(associationProperty = "paymentId")
    public void on(FundsReservedEvent event) {
        // Simula motor antifraude: IDs que empiezan con RISK rechazan.
        if (event.customerId().startsWith("RISK")) {
            commandGateway.send(new RejectFraudCommand(event.paymentId(), "Customer flagged by simulated fraud rules"));
        } else {
            commandGateway.send(new ApproveFraudCommand(event.paymentId()));
        }
    }

    @SagaEventHandler(associationProperty = "paymentId")
    public void on(FraudApprovedEvent event) {
        commandGateway.send(new CapturePaymentCommand(event.paymentId()));
    }

    @SagaEventHandler(associationProperty = "paymentId")
    public void on(FraudRejectedEvent event) {
        commandGateway.send(new ReleaseFundsCommand(event.paymentId(), customerId, "Compensation after fraud rejection: " + event.reason()));
    }

    @SagaEventHandler(associationProperty = "paymentId")
    public void on(FundsReservationFailedEvent event) {
        commandGateway.send(new CancelPaymentCommand(event.paymentId(), event.reason()));
    }

    @SagaEventHandler(associationProperty = "paymentId")
    public void on(FundsReleasedEvent event) {
        commandGateway.send(new CancelPaymentCommand(event.paymentId(), event.reason()));
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "paymentId")
    public void on(PaymentCapturedEvent event) {}

    @EndSaga
    @SagaEventHandler(associationProperty = "paymentId")
    public void on(PaymentCancelledEvent event) {}
}
