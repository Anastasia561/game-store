# Game store API

**Game store Management System** is a robust, RESTful backend service for managing a digital game catalog.

---

## Features

### User Roles
- **Visitor:** View game catalog, search games by title, genre name or platform name
- **Admin:** Add new game, update game, delete game

### Security 
 - JWT-based authentication with role-based access control (RBAC)

### Pagination: 
- Efficient data retrieval using Spring Data Pageable

### Validation: 
- Strict input validation using jakarta.validation
  
---

## Technologies Used

- **Backend:** Java 21, Spring Boot, Spring Security, JWT
- **Database:** PostgreSQL  
- **Build Tool:** Maven 
- **Testing:** JUnit, Mockito, Testcontainers
- **Containerization:** Docker
- **Documentation:** Swagger UI / OpenAPI 3

---

## Database schema

<img width="843" height="594" alt="game-store-2026-05-06_10-56" src="https://github.com/user-attachments/assets/55fab752-7c96-49f3-a13d-bcd7f099ec9a" />


**Optimisation:**
To ensure high performance during filtering, the following indexes are applied:
- idx_game_title (B-Tree) for search functionality.
- idx_genre_name and idx_platform_name for fast relational joins.

## Getting started
1. clone repository: https://github.com/Anastasia561/game-store
2. navigate to directory cd game-store
3. run docker-compose up --build -d

## API documentation
Once the application is running, you can access the interactive Swagger documentation at:
http://localhost:8080/swagger-ui/index.html
