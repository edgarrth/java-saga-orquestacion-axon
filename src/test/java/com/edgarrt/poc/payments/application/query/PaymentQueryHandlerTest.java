package com.edgarrt.poc.payments.application.query;

import com.edgarrt.poc.payments.application.projection.PaymentView;
import com.edgarrt.poc.payments.application.projection.PaymentViewRepository;
import com.edgarrt.poc.payments.domain.query.FindPaymentByIdQuery;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PaymentQueryHandlerTest {

    @Test
    void shouldFindPaymentById() {
        PaymentViewRepository repository = mock(PaymentViewRepository.class);
        PaymentQueryHandler handler = new PaymentQueryHandler(repository);
        PaymentView payment = new PaymentView(
                "58181961-295e-41f7-a1aa-2ad088c460ff",
                "CUST-001",
                "MERCH-001",
                new BigDecimal("120.50"),
                "PEN",
                "CAPTURED");

        when(repository.findById(payment.getPaymentId())).thenReturn(Optional.of(payment));

        Optional<PaymentView> result = handler.handle(new FindPaymentByIdQuery(payment.getPaymentId()));

        assertThat(result).isPresent();
        assertThat(result.orElseThrow().getPaymentId()).isEqualTo(payment.getPaymentId());
    }

    @Test
    void shouldReturnEmptyWhenPaymentDoesNotExist() {
        PaymentViewRepository repository = mock(PaymentViewRepository.class);
        PaymentQueryHandler handler = new PaymentQueryHandler(repository);
        when(repository.findById("missing")).thenReturn(Optional.empty());

        Optional<PaymentView> result = handler.handle(new FindPaymentByIdQuery("missing"));

        assertThat(result).isEmpty();
    }
}
