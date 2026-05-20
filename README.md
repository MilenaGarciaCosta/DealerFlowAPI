# DealerFlowAPI

Spring Boot REST API (JWT auth, MySQL) for the DealerFlow mobile app.

## Local run

1. Copy `src/main/resources/application-local.properties.example` → `application-local.properties` and set `jwt.secret`.
2. Start MySQL with database `dealer_flow`.
3. `.\mvnw.cmd spring-boot:run` → `http://localhost:8080`