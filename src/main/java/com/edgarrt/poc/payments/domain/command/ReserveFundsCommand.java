package com.edgarrt.poc.payments.domain.command;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import java.math.BigDecimal;
public record ReserveFundsCommand(@TargetAggregateIdentifier String paymentId, String customerId, BigDecimal amount) {}
