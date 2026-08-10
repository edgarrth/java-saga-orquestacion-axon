package com.edgarrt.poc.payments.infrastructure.kafka;

import com.edgarrt.poc.payments.domain.event.*;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component("paymentKafkaPublisher")
public class PaymentKafkaPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    public PaymentKafkaPublisher(KafkaTemplate<String, Object> kafkaTemplate) { this.kafkaTemplate = kafkaTemplate; }
    @EventHandler public void on(PaymentCreatedEvent e) { publish(e.paymentId(), e); }
    @EventHandler public void on(FundsReservedEvent e) { publish(e.paymentId(), e); }
    @EventHandler public void on(FraudApprovedEvent e) { publish(e.paymentId(), e); }
    @EventHandler public void on(FundsReservationFailedEvent e) { publish(e.paymentId(), e); }
    @EventHandler public void on(FraudRejectedEvent e) { publish(e.paymentId(), e); }
    @EventHandler public void on(FundsReleasedEvent e) { publish(e.paymentId(), e); }
    @EventHandler public void on(PaymentCapturedEvent e) { publish(e.paymentId(), e); }
    @EventHandler public void on(PaymentCancelledEvent e) { publish(e.paymentId(), e); }
    private void publish(String key, Object event) { kafkaTemplate.send("payment-events", key, event); }
}
