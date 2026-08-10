# Datasets

Esta carpeta conserva el dataset de referencia de la PoC.

## Carga automática

Los datos ya **no se ejecutan como script de inicialización de PostgreSQL**. Hacerlo así provocaba un error en una base nueva porque el script intentaba insertar filas antes de que Flyway creara las tablas.

La carga automática correcta se realiza al iniciar la aplicación mediante:

```text
src/main/resources/db/migration/V1__payment_read_model.sql
src/main/resources/db/migration/V2__demo_data.sql
```

`init-data.sql` queda únicamente como copia legible del dataset de ejemplo.

## Casos de prueba

- `CUST-001`: cliente usado para el flujo aprobado.
- `CUST-002`: cliente de referencia con saldo bajo.
- `RISK-001`: identificador que activa la regla antifraude simulada y la compensación de la Saga.

> Nota: la PoC simula la reserva de fondos y fraude dentro del dominio; las tablas del dataset sirven para representar cómo podrían persistirse esos datos en una integración posterior.
