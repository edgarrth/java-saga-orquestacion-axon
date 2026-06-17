package com.edgarrt.poc.payments.domain.event;
public record PaymentCancelledEvent(String paymentId, String reason) {}
