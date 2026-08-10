#!/usr/bin/env sh
set -eu

POSTGRES_CONTAINER="axon-orchestration-postgres"
KAFKA_CONTAINER="axon-orchestration-kafka"

echo "[1/4] Verificando contenedores..."
docker inspect "$POSTGRES_CONTAINER" >/dev/null
docker inspect "$KAFKA_CONTAINER" >/dev/null

echo "[2/4] Verificando PostgreSQL y base payments..."
docker exec "$POSTGRES_CONTAINER" pg_isready -U payments -d payments >/dev/null
docker exec "$POSTGRES_CONTAINER" psql -U payments -d payments -v ON_ERROR_STOP=1 -tAc \
  "SELECT current_database();" | grep -q '^payments$'

echo "[3/4] Verificando tablas creadas por Flyway (si la aplicación ya se ejecutó)..."
TABLE_COUNT="$(docker exec "$POSTGRES_CONTAINER" psql -U payments -d payments -tAc \
  "SELECT count(*) FROM information_schema.tables WHERE table_schema='public' AND table_name IN ('payment_view','customer_funds','fraud_rules');" | tr -d '[:space:]')"
if [ "$TABLE_COUNT" = "3" ]; then
  echo "      OK: tablas de negocio presentes."
else
  echo "      INFO: la base existe; las tablas se crearán al iniciar Spring Boot/Flyway."
fi

echo "[4/4] Verificando Kafka..."
docker exec "$KAFKA_CONTAINER" /opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --list >/dev/null

echo "OK: infraestructura lista."
