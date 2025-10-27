#  Wallet Project
**Java 21 • Spring Boot 3.5 • JWT • Postgres • H2 • Flyway • Maven**

This project is a **wallet** providing authentication, deposits, set limit usage for transaction amount, transfers with **idempotency guarantees**, and transaction tracking with audit logs.
This assignment is developed for Snapppay company.

---

##  Features
- **JWT Authentication**
- **Wallet Operations**
    - Balance 
    - Deposit
    - Transfer with **Idempotency** (`(wallet_id, ref)` unique index)
    - set daily limit and single transaction limit for user
- **Ledger Entries**
- **Audit Logs**
- **Flyway** for schema versioning
- **H2 (dev)** and **PostgreSQL (prod-ready)**
    - H2 Console: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

##  Architecture Overview
The service follows a layered architecture:
- `domain` – entities & business rules
- `application` – services
- `infra` – repositories, filters, and utilities
- `api` – REST controllers
- `security` – JWT, filters, and configuration

---

##  Installation & Setup

### 1. Prerequisites
Make sure you have:
- **Java 21**
- **Maven 3.9+**
- (Optional) **PostgreSQL 15+**

---

### 2. Clone the Repository 
```bash
git clone https://github.com/sarahemati/snapppayAssignment.git
cd wallet
```

---

### 3. Environment Variables
For production or local dev, define the following environment variables:

```bash
APP_JWT_SECRET=YOUR_LONG_SECRET_KEY_AT_LEAST_32_BYTES
APP_JWT_EXP=7200
```

Or edit in `application.properties`:
```properties
app.jwt.secret=${APP_JWT_SECRET:default-test-secret-32-bytes-minimum}
app.jwt.exp=${APP_JWT_EXP:7200}
```

---

### 4. Run in Dev Mode (H2)
```bash
mvn clean spring-boot:run -Dspring-boot.run.profiles=dev
```

**Swagger:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  
**H2 Console:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

---

### 5. Run Tests
```bash
mvn clean verify
```
> Integration tests use **H2** and run automatically via `maven-failsafe-plugin`.

---
## Header Explanation
Header which provides it Purpose
X-Request-Id	Server or Gateway Used for tracing a single HTTP request across logs. New value for every request.
X-Idempotency-Key	Client app (mobile/web)	Prevents duplicate effects for the same operation (e.g., repeated transfers). Same key reused on retries.
# Behavior

If X-Request-Id is missing, backend auto-generates a UUID and echoes it in the response header.

If X-Idempotency-Key is missing, the transfer will still execute but retries may duplicate the operation.

For production, the client SDK (not human user) should always generate a unique X-Idempotency-Ke

##  API Flow

### 1. **Signup**
Create a new user account:
```bash
curl -s -X POST "http://127.0.0.1:8080/api/auth/signup" -H "Content-Type: application/json" -d '{"username":"saraHemmati","password":"saraHemmati"}'
```
Expected: 
> 200 response code
---

### 2. **Login**
Use your credentials to receive access token.
Obtain JWT token:
```bash
TOKEN=$(curl -s -X POST "http://127.0.0.1:8080/api/auth/login" -H "Content-Type: application/json" -d '{"username":"ali","password":"saraHemmati"}' | jq -r '.accessToken')
```
Expected:
> accessToken returned in JSON — copy or store it in Swagger Authorize dialog.
> You can authorize in **Swagger** using:  
> `Bearer <token>`

---

### 3. **Deposit**
Deposit funds into the wallet (idempotent operation):
Optional header: X-Idempotency-Key for duplicate protection.

```bash
curl -s -X POST "http://127.0.0.1:8080/api/wallet/deposit" -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" -H "X-Idempotency-Key: 550e8400-e29b-41d4-a716-446655440000" -d '{"amount":1000}'
```
Expected:
Wallet balance increased, a ledger record created.
If same Idempotency Key is used again → response repeats, no new transaction.
---

### 4. **Transfer**
Transfer funds between users:
Move funds from your wallet to another user.

```bash
curl -X POST "http://127.0.0.1:8080/api/wallet/transfer" -H "accept: */*" -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" -d '{"toUsername": "Ali", "amount": 2000}'
```
Expected:
Ledger entries created for both users (debit & credit).
If same X-Idempotency-Key retried → returns same result, no double transfer.
---

### 5. **View Wallet Info**
Retrieve your current balance and ledger:

```bash
curl -s -X GET "http://127.0.0.1:8080/api/wallet/mywallet" 
```
Expected:
JSON with current balance + recent ledger operations.
---

### 6. **Account Limit Config (Admin/Test)**
Example for creating or updating account limits. single transaction limit and Daily limit should be set.

curl -X POST "http://localhost:8080/accountLimitConfigs" \ -H "Authorization: Bearer $TOKEN"
---
Expected:
Daily and per-transaction limits persisted for this user.
Server enforces limit when withdrawing or transferring.

##  Useful Endpoints
| Endpoint | Description |
|-----------|--------------|
| `/api/auth/signup` | Register new user |
| `/api/auth/login` | Login and get JWT |
| `/api/wallet/deposit` | Deposit funds |
| `/api/wallet/transfer` | Transfer between users |
| `/api/wallet/me` | Get wallet and transaction history |
| `/swagger-ui.html` | API documentation |
| `/h2-console` | H2 in-memory DB console |

---
## Test Idempotency Scenario

Send deposit with a unique key → processed.

Send the same request again with same key → no duplicate transaction.

Change the key → processed as a new transaction.

Expected:
Only one ledger record per unique Idempotency Key.

## Logging

All logs include:

requestId=<uuid> | idemKey=<uuid or -> | user=<username>


Example:

2025-10-25 20:26:11 INFO  RequestContextLoggingFilter |
requestId=644cc184... idemKey=550e8400... user=saraHemmati |
POST /wallet/transfer -> 200 (45 ms)


Log files are stored in:

/opt/app/logs/wallet-app.log

(created automatically by Logback if folder exists)

## Architecture Notes

@Transactional(propagation = REQUIRES_NEW) used for audit/ledger logs to ensure they persist even if main transaction fails.

Idempotency enforced at DB level via unique constraint (wallet_id, ref) to avoid race conditions.

Request context logging uses MDC for tracing across async threads.

##  Database Migration
Flyway migration scripts are located in:
```
src/main/resources/db/migration
```
- **V1__init.sql** — creates schema

---

##  License
This project is licensed under the MIT License.  
Feel free to use, modify, and distribute with attribution.
