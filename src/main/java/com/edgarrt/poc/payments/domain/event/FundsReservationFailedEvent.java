package com.edgarrt.poc.payments.domain.event;
public record FundsReservationFailedEvent(String paymentId, String customerId, String reason) {}
