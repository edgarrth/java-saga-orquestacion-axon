INSERT INTO customer_funds(customer_id, available_balance, reserved_balance)
VALUES ('CUST-001', 10000.00, 0.00),
       ('CUST-002', 500.00, 0.00),
       ('RISK-001', 10000.00, 0.00)
ON CONFLICT (customer_id) DO NOTHING;

INSERT INTO fraud_rules(rule_id, max_amount, enabled)
VALUES ('DEFAULT_MAX_AMOUNT', 3000.00, TRUE)
ON CONFLICT (rule_id) DO NOTHING;
