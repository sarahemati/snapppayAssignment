# Wallet Service (Java 21 • Spring Boot 3.5 • JWT • H2 • Flyway)

کیف‌ پول ساده با قابلیت‌های:
- ثبت‌نام/لاگین با **JWT**
- **واریز** و **انتقال** با تضمین **Idempotency** (ایندکس یونیک `(wallet_id, ref)`)
- **Ledger** 
- **Audit Log**
- **Swagger**  http://localhost:8080/swagger-ui.html
- **Flyway** برای اسکیما
- **H2** (dev) و **Postgres** (prod-ready) H2 Console: http://localhost:8080/h2-console

## Run (dev)
mvn clean spring-boot:run -Dspring-boot.run.profiles=dev


# Flow

## 1) signup (ثبت نام )
curl -s -X POST "http://127.0.0.1:8080/api/auth/signup" \
-H "Content-Type: application/json" \
-d '{"username":"saraHemmati","password":"saraHemmati"}'

## 2) login (توکن بگیر)
در Swagger روی Authorize بزن: Bearer<token>

TOKEN=$(curl -s -X POST "http://127.0.0.1:8080/api/auth/login" \
-H "Content-Type: application/json" \
-d '{"username":"ali","password":"saraHemmati"}' | jq -r '.accessToken')

## 3) deposit (شارژ کیف پول)
Header اختیاری: X-Idempotency-Key: <uuid>

curl -s -X POST "http://127.0.0.1:8080/api/wallet/deposit" \
-H "Authorization: Bearer $TOKEN" \
-H "Content-Type: application/json" \
-H "X-Idempotency-Key: 550e8400-e29b-41d4-a716-446655440000" \
-d '{"amount":1000}'

## 4) Transfer (انتاقال)
curl -X 'POST' \
'http://localhost:8080/api/wallet/transfer' \
-H 'accept: */*' \
-H 'Authorization: Bearer  $TOKEN \
-H 'Content-Type: application/json' \
-d '{
"toUsername": "Ali",
"amount": 2000
}'

## 5) me (مشاهده موجودی و گردش)
curl -s -X GET "http://127.0.0.1:8080/api/wallet/me" \
-H "Authorization: Bearer $TOKEN"


```bash
