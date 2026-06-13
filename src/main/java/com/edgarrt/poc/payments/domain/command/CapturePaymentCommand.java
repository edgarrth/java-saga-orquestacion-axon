package com.edgarrt.poc.payments.domain.command;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
public record CapturePaymentCommand(@TargetAggregateIdentifier String paymentId) {}
