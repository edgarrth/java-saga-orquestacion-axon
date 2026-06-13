# Datasets

Esta carpeta contiene datos iniciales para la PoC.

## Archivo

- `init-data.sql`: precarga clientes simulados y reglas antifraude.

## Cómo usarlo

Desde la raíz del proyecto:

```bash
cd infraestructura/docker
docker compose up -d
```

El archivo se monta automáticamente en PostgreSQL cuando el contenedor se crea por primera vez.

## Casos de prueba

- `CUST-001`: cliente válido con saldo suficiente.
- `CUST-002`: cliente de saldo bajo; puedes probar montos pequeños.
- `RISK-001`: cliente marcado como riesgoso; la Saga orquestada ejecuta compensación liberando fondos y cancelando el pago.
