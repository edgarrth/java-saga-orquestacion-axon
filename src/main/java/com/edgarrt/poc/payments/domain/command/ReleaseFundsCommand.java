package com.edgarrt.poc.payments.domain.command;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
public record ReleaseFundsCommand(@TargetAggregateIdentifier String paymentId, String customerId, String reason) {}
