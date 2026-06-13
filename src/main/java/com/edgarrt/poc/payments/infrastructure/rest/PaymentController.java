package com.edgarrt.poc.payments.infrastructure.rest;

import com.edgarrt.poc.payments.application.projection.PaymentView;
import com.edgarrt.poc.payments.domain.command.CreatePaymentCommand;
import com.edgarrt.poc.payments.domain.query.FindPaymentByIdQuery;
import com.edgarrt.poc.payments.domain.query.ListPaymentsQuery;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/payments/v1/payments")
public class PaymentController {
    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;
    public PaymentController(CommandGateway commandGateway, QueryGateway queryGateway){this.commandGateway=commandGateway;this.queryGateway=queryGateway;}

    @PostMapping
    public CompletableFuture<CreatePaymentResponse> create(@Valid @RequestBody CreatePaymentRequest request) {
        String id = UUID.randomUUID().toString();
        return commandGateway.send(new CreatePaymentCommand(id, request.customerId(), request.merchantId(), request.amount(), request.currency()))
                .thenApply(r -> new CreatePaymentResponse(id, "PAYMENT_ORCHESTRATION_STARTED"));
    }

    @GetMapping("/{paymentId}")
    public CompletableFuture<PaymentView> find(@PathVariable String paymentId) {
        return queryGateway.query(new FindPaymentByIdQuery(paymentId), ResponseTypes.instanceOf(PaymentView.class));
    }

    @GetMapping
    public CompletableFuture<List<PaymentView>> list() {
        return queryGateway.query(new ListPaymentsQuery(), ResponseTypes.multipleInstancesOf(PaymentView.class));
    }

    public record CreatePaymentRequest(@NotBlank String customerId, @NotBlank String merchantId, @DecimalMin("0.01") BigDecimal amount, @Pattern(regexp="PEN|USD") String currency) {}
    public record CreatePaymentResponse(String paymentId, String status) {}
}
