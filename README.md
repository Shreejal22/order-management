# Order Management Service (Spring Boot + PostgreSQL)

A lightweight REST API to manage orders, built with **Java 21**, **Spring Boot 3.5.9**, **Spring Data JPA**, and **PostgreSQL**.

## Features
- Create, retrieve, update, and cancel orders
- Filter orders by status (`GET /api/orders?status=CREATED`)
- Bean Validation on incoming requests
- Consistent error responses using `ProblemDetail`
- Unit tests with JUnit 5 + Mockito

## Prerequisites
- Java 21
- Maven 3.9+
- PostgreSQL 14+

## Database Setup
1. Start PostgreSQL.
2. Create the database and user:
   ```sql
   CREATE USER order_user WITH PASSWORD 'order_pass';
   CREATE DATABASE orderdb OWNER order_user;


## 🐳 Running with Docker (Recommended)
No need to install PostgreSQL locally. Just run:
```bash
docker-compose up --build