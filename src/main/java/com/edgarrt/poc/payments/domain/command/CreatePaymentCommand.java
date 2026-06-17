package com.edgarrt.poc.payments.domain.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import java.math.BigDecimal;

public record CreatePaymentCommand(@TargetAggregateIdentifier String paymentId,
                                   String customerId,
                                   String merchantId,
                                   BigDecimal amount,
                                   String currency) {}
