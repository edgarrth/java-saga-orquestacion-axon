package com.edgarrt.poc.payments.domain.event;
public record FundsReservedEvent(String paymentId, String customerId) {}
