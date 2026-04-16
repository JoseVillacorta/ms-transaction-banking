
# Banking System Transaction Microservice

**Microservicio para el registro de movimientos y estados de cuenta.**

Este servicio se encarga de auditar y registrar cada movimiento financiero realizado en el banco (transferencias, depósitos, retiros), permitiendo a los usuarios consultar su historial de transacciones.

## Descripción del Proyecto

El **Transaction Microservice** funciona de manera asíncrona para registrar lo que sucede en otros servicios, manteniendo un historial inmutable de las operaciones.

### Qué hace esta aplicación
- **Registro de Transferencias:** Almacena los detalles de origen, destino y monto de cada movimiento.
- **Estados de Cuenta:** Proporciona un historial detallado de movimientos por cada cuenta bancaria.
- **Trazabilidad:** Define estados para las transacciones (PENDING, COMPLETED, FAILED).
- **Integración:** (Futuro) Previsto para integrarse con Kafka para recibir eventos de otros servicios.

### Tecnologías utilizadas
- **Java 21**
- **Spring Boot 3.x / WebFlux**
- **Spring Data R2DBC (PostgreSQL)**.
- **Lombok**
- **Eureka & Config Client**.

---

## Cómo instalar y ejecutar el proyecto

### Requisitos previos
1. Toda la infraestructura base (Config, Registry) activa.
2. Base de datos **PostgreSQL** (db_transaction) activa.

### Pasos para ejecución local (Gradle)
1. Navega a la carpeta: `cd ms-transaction`
2. Ejecuta:
   ```bash
   ./gradlew bootRun
   ```

### Pasos para ejecución local (Gradle)
1. Navega a la carpeta: `cd ms-transaction`
2. Ejecuta:
   ```bash
   ./gradlew bootRun
   ```

### Pasos para ejecución con Docker
```bash
docker-compose up -d ms-transaction
```

---

## Cómo utilizar el proyecto

### Endpoints (v1)
- **Registrar Transferencia:** `POST /api/v1/transactions`
- **Consultar Movimientos:** `GET /api/v1/transactions/statement/{accountId}`


