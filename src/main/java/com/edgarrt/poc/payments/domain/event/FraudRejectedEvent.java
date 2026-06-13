package com.edgarrt.poc.payments.domain.event;
public record FraudRejectedEvent(String paymentId, String reason) {}
