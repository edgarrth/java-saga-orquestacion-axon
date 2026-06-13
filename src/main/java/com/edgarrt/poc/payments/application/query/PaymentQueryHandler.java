package com.edgarrt.poc.payments.application.query;

import com.edgarrt.poc.payments.application.projection.*;
import com.edgarrt.poc.payments.domain.query.*;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class PaymentQueryHandler {
    private final PaymentViewRepository repository;
    public PaymentQueryHandler(PaymentViewRepository repository){this.repository = repository;}
    @QueryHandler public PaymentView handle(FindPaymentByIdQuery q){return repository.findById(q.paymentId()).orElseThrow();}
    @QueryHandler public List<PaymentView> handle(ListPaymentsQuery q){return repository.findAll();}
}
