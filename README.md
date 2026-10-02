# SriLanka Railways - Train Route Management Module

**Academic Project**: SE2030 Software Engineering Group Project  
**System Component**: Train Route Management (Individual Module)  
**Branding**: SriLanka Railways - Web-Based Train Scheduling & Booking System  

---

## 📌 Executive Summary
The **Train Route Management** module is the foundational core of the **SriLanka Railways** system. It provides high-performance administration for railway stations, ordered train routes, origin/destination derivation, and station stop sequences.

---

## 🛠 Tech Stack
- **Backend Framework**: Java 17 / Spring Boot 3.2.3
- **Persistence Layer**: Spring Data JPA / Hibernate
- **Database**: MySQL (`train_booking_db`) / In-Memory H2 for automated testing
- **Validation**: Jakarta Bean Validation (`@NotBlank`, `@Size`, `@NotEmpty`)
- **Frontend**: Vanilla HTML5, CSS3, JavaScript (Fetch API, Glassmorphism, Micro-animations)
- **Testing**: JUnit 5, Mockito, Spring Boot Test / MockMvc

---

## 🗄 Database Schema Design

### 1. `stations` Table
- `id` (BIGINT, Primary Key, Auto Increment)
- `code` (VARCHAR(20), UNIQUE, NOT NULL) - e.g., `FOT`, `KDT`
- `name` (VARCHAR(100), NOT NULL) - e.g., `Colombo Fort`
- `location` (VARCHAR(150), NOT NULL) - e.g., `Colombo District`
- `active` (BOOLEAN, DEFAULT true)
- `created_at`, `updated_at` (TIMESTAMP)

### 2. `routes` Table
- `id` (BIGINT, Primary Key, Auto Increment)
- `route_code` (VARCHAR(50), UNIQUE, NOT NULL) - e.g., `R001`
- `route_name` (VARCHAR(150), NOT NULL) - e.g., `Colombo Fort - Kandy Main Line`
- `active` (BOOLEAN, DEFAULT true)
- `created_at`, `updated_at` (TIMESTAMP)

### 3. `route_stations` (Junction Table)
- `id` (BIGINT, Primary Key, Auto Increment)
- `route_id` (BIGINT, Foreign Key -> `routes.id`, NOT NULL)
- `station_id` (BIGINT, Foreign Key -> `stations.id`, NOT NULL)
- `stop_order` (INT, NOT NULL)
- `is_origin` (BOOLEAN, NOT NULL)
- `is_destination` (BOOLEAN, NOT NULL)
- **Unique Constraints**: `uk_route_station(route_id, station_id)`, `uk_route_stop_order(route_id, stop_order)`

---

## 📡 REST API Documentation

### Station Endpoints
| Method | Endpoint | Description | Status Code |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/stations` | Create a new railway station | `201 CREATED` |
| `GET` | `/api/stations` | Get all stations (Optional `?active=true`) | `200 OK` |
| `GET` | `/api/stations/{id}` | Get station details by ID | `200 OK` |
| `PUT` | `/api/stations/{id}` | Update station details | `200 OK` |
| `DELETE` | `/api/stations/{id}` | Soft deactivate station | `200 OK` |

### Route Endpoints
| Method | Endpoint | Description | Status Code |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/routes` | Create a new train route with station sequence | `201 CREATED` |
| `GET` | `/api/routes` | Get all routes (Optional `?active=true`) | `200 OK` |
| `GET` | `/api/routes/active` | Get active routes only | `200 OK` |
| `GET` | `/api/routes/{id}` | Get route details by ID | `200 OK` |
| `GET` | `/api/routes/{id}/stations` | Get ordered stations breakdown for a route | `200 OK` |
| `PUT` | `/api/routes/{id}` | Update route and re-order station sequence | `200 OK` |
| `PATCH` | `/api/routes/{id}/status?active=bool` | Toggle active status of a route | `200 OK` |
| `DELETE` | `/api/routes/{id}` | Soft deactivate route | `200 OK` |

---

## ⚡ Business Rules & Validation Highlights
1. **Station Code Uniqueness**: Enforced case-insensitively (`FOT`, `KDT`).
2. **Minimum Station Rule**: A train route must contain at least 2 stations (`size >= 2`).
3. **No Duplicate Stops**: A single route payload cannot contain repeated station IDs.
4. **Origin & Destination Derivation**: 
   - 1st station in sequence (`stopOrder = 1`) -> `is_origin = true`
   - Last station in sequence (`stopOrder = N`) -> `is_destination = true`
5. **Transactional Integrity**: Route updates clear and recreate station associations atomically within `@Transactional` boundaries.

---

## 🚀 Running the Application

### 1. Database Configuration
Ensure MySQL server is running on `localhost:3306` with database `train_booking_db`.
(Spring Boot will automatically seed sample Sri Lankan stations and routes on first boot).

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/train_booking_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
```

### 2. Run via Maven
Execute from terminal:
```bash
mvn spring-boot:run
```

### 3. Access Admin Web Portal
Open browser at: `http://localhost:8080`

### 4. Import Postman Collection
Import `SriLanka_Railways_Train_Route_Management_Postman_Collection.json` into Postman to test all endpoints directly.

---

## 🧪 Running Automated Tests
Run unit and controller tests:
```bash
mvn test
```
All test suites pass 100% cleanly.
