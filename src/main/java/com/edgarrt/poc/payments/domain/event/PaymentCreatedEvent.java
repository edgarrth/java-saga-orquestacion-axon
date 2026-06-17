package com.edgarrt.poc.payments.domain.event;
import java.math.BigDecimal;
public record PaymentCreatedEvent(String paymentId, String customerId, String merchantId, BigDecimal amount, String currency) {}
