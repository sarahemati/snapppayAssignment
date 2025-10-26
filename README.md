#  Wallet Service
**Java 21 • Spring Boot 3.5 • JWT • H2 • Flyway • Maven**

This project is a **wallet** providing authentication, deposits, transfers with **idempotency guarantees**, and transaction tracking with audit logs.
This assignment is developed for Snapppay company.

---

##  Features
- **JWT Authentication**
- **Wallet Operations**
    - Deposit
    - Transfer with **Idempotency** (`(wallet_id, ref)` unique index)
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

## ⚙ Installation & Setup

### 1️⃣ Prerequisites
Make sure you have:
- **Java 21**
- **Maven 3.9+**
- (Optional) **PostgreSQL 15+**

---

### 2️⃣ Clone the Repository
```bash
git clone https://github.com/sarahemati/snapppayAssignment.git
cd wallet
```

---

### 3️⃣ Environment Variables
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

### 4️⃣ Run in Dev Mode (H2)
```bash
mvn clean spring-boot:run -Dspring-boot.run.profiles=dev
```

**Swagger:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  
**H2 Console:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

---

### 5️⃣ Run Tests
```bash
mvn clean verify
```
> Integration tests use **H2** and run automatically via `maven-failsafe-plugin`.

---

##  API Flow

### 1. **Signup**
Create a new user account:
```bash
curl -s -X POST "http://127.0.0.1:8080/api/auth/signup" -H "Content-Type: application/json" -d '{"username":"saraHemmati","password":"saraHemmati"}'
```

---

### 2. **Login**
Obtain JWT token:
```bash
TOKEN=$(curl -s -X POST "http://127.0.0.1:8080/api/auth/login" -H "Content-Type: application/json" -d '{"username":"ali","password":"saraHemmati"}' | jq -r '.accessToken')
```

> You can also authorize in **Swagger** using:  
> `Bearer <token>`

---

### 3. **Deposit**
Deposit funds into the wallet (idempotent operation):

```bash
curl -s -X POST "http://127.0.0.1:8080/api/wallet/deposit" -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" -H "X-Idempotency-Key: 550e8400-e29b-41d4-a716-446655440000" -d '{"amount":1000}'
```

---

### 4. **Transfer**
Transfer funds between users:

```bash
curl -X POST "http://127.0.0.1:8080/api/wallet/transfer" -H "accept: */*" -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" -d '{"toUsername": "Ali", "amount": 2000}'
```

---

### 5. **View Wallet Info**
Retrieve your current balance and ledger:

```bash
curl -s -X GET "http://127.0.0.1:8080/api/wallet/mywallet" -H "Authorization: Bearer $TOKEN"
```

---

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
