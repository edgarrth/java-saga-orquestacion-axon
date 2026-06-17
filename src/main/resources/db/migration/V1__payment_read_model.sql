CREATE TABLE IF NOT EXISTS payment_view (
    payment_id VARCHAR(80) PRIMARY KEY,
    customer_id VARCHAR(80) NOT NULL,
    merchant_id VARCHAR(80) NOT NULL,
    amount NUMERIC(19,2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    status VARCHAR(40) NOT NULL,
    failure_reason VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS customer_funds (
    customer_id VARCHAR(80) PRIMARY KEY,
    available_balance NUMERIC(19,2) NOT NULL,
    reserved_balance NUMERIC(19,2) NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS fraud_rules (
    rule_id VARCHAR(80) PRIMARY KEY,
    max_amount NUMERIC(19,2) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE
);
