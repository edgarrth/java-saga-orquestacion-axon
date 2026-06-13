package com.edgarrt.poc.payments.domain.event;
public record FundsReleasedEvent(String paymentId, String customerId, String reason) {}
