# 🛠️ Local Setup Guide – E-commerce Backend

This guide will help you get the e-commerce backend running locally with **PostgreSQL** and **Keycloak**.

---

## ✅ Prerequisites

- Java 17+
- Docker & Docker Compose
- Git

---

## 1. 📦 Setup PostgreSQL Database

You can run PostgreSQL locally using Docker:

```bash
docker run --name ecommerce-db \
  -e POSTGRES_DB=ecommerce \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=1002 \
  -p 5432:5432 \
  -d postgres:17
```


---

## 🚀 1. Start Keycloak with Docker

You can start Keycloak directly using the Docker CLI:

```bash
docker run --name ecommerce-keycloak \
  -p 8080:8080 \
  -e KEYCLOAK_ADMIN=admin \
  -e KEYCLOAK_ADMIN_PASSWORD=admin \
  -v $(pwd)/keycloak:/opt/keycloak/data/import \
  quay.io/keycloak/keycloak:26.0.6 \
  start-dev --import-realm
```

