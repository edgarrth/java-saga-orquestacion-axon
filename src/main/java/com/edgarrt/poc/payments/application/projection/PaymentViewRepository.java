package com.edgarrt.poc.payments.application.projection;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PaymentViewRepository extends JpaRepository<PaymentView, String> {}
