package com.edgarrt.poc.payments.application.query;

import com.edgarrt.poc.payments.application.projection.PaymentView;
import com.edgarrt.poc.payments.application.projection.PaymentViewRepository;
import com.edgarrt.poc.payments.domain.query.FindPaymentByIdQuery;
import com.edgarrt.poc.payments.domain.query.ListPaymentsQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PaymentQueryHandler {
    private final PaymentViewRepository repository;

    public PaymentQueryHandler(PaymentViewRepository repository) {
        this.repository = repository;
    }

    @QueryHandler
    public Optional<PaymentView> handle(FindPaymentByIdQuery query) {
        return repository.findById(query.paymentId());
    }

    @QueryHandler
    public List<PaymentView> handle(ListPaymentsQuery query) {
        return repository.findAll();
    }
}
