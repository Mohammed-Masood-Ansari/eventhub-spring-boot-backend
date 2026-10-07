# EventHub Spring Boot Backend

EventHub is a **role-based event management REST API** built with Spring
Boot. It provides secure authentication and authorization for **Admin**,
**Organiser**, and **Customer** users.

Organisers can create events, administrators can review and
approve/reject them, and customers can view approved events. The
application uses **JWT-based stateless authentication**, **RSA keys for
JWT signing/verification**, **Spring Security**, **Spring Data JPA**,
and **MySQL**.

> **Project Status:** Core authentication, role-based security, event
> creation, event approval, and approved-event retrieval are
> implemented. The booking/payment workflow is under development.

------------------------------------------------------------------------

## Features

### Authentication

-   User registration
-   Email/password login
-   BCrypt password hashing
-   JWT token generation
-   RSA-based JWT signing and verification
-   Stateless authentication

### Role-Based Authorization

The application supports three roles:

-   `ROLE_ADMIN`
-   `ROLE_ORGANISER`
-   `ROLE_CUSTOMER`

Protected API groups:

  URL Pattern                 Access
  --------------------------- ---------------------
  `/auth/**`                  Public
  `/admin/**`                 Admin only
  `/organiser/**`             Organiser only
  `/customer/**`              Customer only
  Other protected endpoints   Authenticated users

### Organiser

-   Create events
-   Automatically associate an event with the authenticated organiser
-   New events start with `PENDING` verification status

### Admin

-   Review pending events
-   Approve events
-   Reject events
-   Manage event verification status

### Customer

-   View approved events
-   Booking model is available and the complete booking/payment workflow
    is under development

------------------------------------------------------------------------

## Technology Stack

  Technology               Purpose
  ------------------------ ----------------------------------
  Java 21                  Programming language
  Spring Boot              Backend framework
  Spring MVC               REST API development
  Spring Security          Authentication and authorization
  OAuth2 Resource Server   JWT validation
  JWT                      Stateless authentication
  RSA 2048                 JWT signing and verification
  BCrypt                   Password hashing
  Spring Data JPA          Data access
  Hibernate                ORM
  MySQL                    Relational database
  MapStruct                DTO/entity mapping
  Lombok                   Boilerplate reduction
  Maven                    Build and dependency management
  Springdoc OpenAPI        API documentation

------------------------------------------------------------------------

## Architecture

``` text
                 Client / Frontend / Postman
                           |
                           | HTTP + Bearer JWT
                           v
                +-----------------------+
                |    Spring Security    |
                |  SecurityFilterChain  |
                +-----------+-----------+
                            |
                 JWT Authentication
                  Role Authorization
                            |
                            v
                +-----------------------+
                |      Controller       |
                +-----------+-----------+
                            |
                            v
                +-----------------------+
                |        Service        |
                |    Business Logic     |
                +-----------+-----------+
                            |
                            v
                +-----------------------+
                |      Repository       |
                |   Spring Data JPA     |
                +-----------+-----------+
                            |
                            v
                +-----------------------+
                |         MySQL         |
                +-----------------------+
```

------------------------------------------------------------------------

## Main Modules

``` text
EventHub
|
+-- Authentication Module
|   +-- Registration
|   +-- Login
|   +-- JWT generation
|
+-- Security Module
|   +-- Spring Security
|   +-- JWT validation
|   +-- RSA signing/verification
|   +-- Role-based authorization
|
+-- Organiser Module
|   +-- Event creation
|
+-- Admin Module
|   +-- View pending events
|   +-- Approve events
|   +-- Reject events
|
+-- Customer Module
|   +-- View approved events
|
+-- Booking Module
    +-- Booking entity/model
    +-- Booking/payment workflow under development
```

------------------------------------------------------------------------

## ER Diagram

``` text
+----------------+
|      ROLE      |
+----------------+
| PK id          |
| name           |
+-------+--------+
        |
        | 1
        |
        | N
+-------v--------+
|      USER      |
+----------------+
| PK id          |
| name           |
| email UNIQUE   |
| password       |
| phone UNIQUE   |
| createdAt      |
| FK role_id     |
+-------+--------+
        |
        | organiser creates
        | 1 : N
        v
+----------------+
|     EVENT      |
+----------------+
| PK id          |
| name           |
| description    |
| location       |
| eventDateTime  |
| createdAt      |
| ticketPrice    |
| availableTickets|
| status         |
| FK organiser_id|
+-------+--------+
        |
        | 1 : N
        v
+-------------------+
|      BOOKING      |
+-------------------+
| PK bookingId      |
| quantity          |
| totalPrice        |
| paymentStatus     |
| paymentDateTime   |
| eventDateTime     |
| bookingDateTime   |
| FK event_id       |
| FK user_id        |
+-------------------+
```

### Main Relationships

-   One **Role** can be assigned to many users.
-   One **Organiser/User** can create many events.
-   Each **Event** belongs to one organiser.
-   One event can eventually have many bookings.
-   A customer can eventually have multiple bookings.

------------------------------------------------------------------------

## Authentication Flow

``` text
POST /auth/login
       |
       v
AuthenticationManager
       |
       v
UserDetailsService
       |
       v
UserRepository.findByEmail()
       |
       v
Password Verification
       |
       v
Authenticated User
       |
       v
JWT Generator
       |
       v
JWT returned to client
```

The JWT contains the authenticated user's identity and authorities, for
example:

``` json
{
  "iss": "event-hub",
  "sub": "organiser@example.com",
  "roles": [
    "ROLE_ORGANISER"
  ]
}
```

The client sends the token with protected requests:

``` http
Authorization: Bearer <JWT>
```

------------------------------------------------------------------------

## JWT Authorization Flow

``` text
Bearer JWT
    |
    v
Spring Security Filter Chain
    |
    v
JwtDecoder
    |
    | RSA Public Key
    v
JWT Signature Validation
    |
    v
JwtAuthenticationConverter
    |
    | reads "roles"
    v
Granted Authorities
    |
    v
SecurityContext
    |
    v
Authentication
```

The logged-in user's email can be obtained using:

``` java
authentication.getName();
```

Authorities can be obtained using:

``` java
authentication.getAuthorities();
```

Example:

``` text
Username: organiser@example.com
Authority: ROLE_ORGANISER
```

------------------------------------------------------------------------

## Event Lifecycle

An organiser cannot directly publish an event.

``` text
Organiser creates event
         |
         v
      PENDING
         |
         v
    Admin Review
       /     \
      /       \
     v         v
 APPROVED   REJECTED
     |
     v
Visible to customers
```

The event verification states are:

``` text
PENDING
APPROVED
REJECTED
```

------------------------------------------------------------------------

## Example: Create Event

An authenticated organiser sends:

``` http
POST /organiser/registerEvent
Authorization: Bearer <organiser-jwt>
Content-Type: application/json
```

Example request:

``` json
{
  "name": "Java Developer Conference 2026",
  "description": "Conference for Java and Spring Boot developers",
  "location": "Bengaluru",
  "eventDateTime": "2026-12-20T10:00:00",
  "ticketPrice": 999.0,
  "availableTickets": 500
}
```

The client does not need to provide the organiser ID. The backend
identifies the organiser from the authenticated JWT:

``` java
String email = authentication.getName();
```

The user is fetched from the database and associated with the event.

New events are stored with:

``` text
status = PENDING
```

------------------------------------------------------------------------

## Example: View Approved Events

A customer can access approved events through the customer API.

``` http
GET /customer/getAllApprovedEvents
Authorization: Bearer <customer-jwt>
```

The backend retrieves events whose verification status is:

``` text
APPROVED
```

Pending or rejected events are not returned by this endpoint.

------------------------------------------------------------------------

## Security Configuration

The application uses stateless authentication:

``` java
session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
```

Role-based URL protection follows this structure:

``` java
.requestMatchers("/auth/**").permitAll()
.requestMatchers("/admin/**").hasRole("ADMIN")
.requestMatchers("/organiser/**").hasRole("ORGANISER")
.requestMatchers("/customer/**").hasRole("CUSTOMER")
.anyRequest().authenticated();
```

The JWT `roles` claim is converted into Spring Security authorities.

------------------------------------------------------------------------

## 401 vs 403

### 401 Unauthorized

Authentication failed.

Common reasons:

-   JWT is missing
-   JWT is expired
-   JWT is malformed
-   Invalid JWT signature
-   Bearer token is not supplied correctly

### 403 Forbidden

Authentication succeeded, but the authenticated user does not have the
required authority.

Example:

``` text
ROLE_CUSTOMER
       |
       | POST /organiser/registerEvent
       v
403 Forbidden
```

------------------------------------------------------------------------

## Password Security

Passwords are not stored in plain text.

The application uses:

``` java
BCryptPasswordEncoder
```

Registration flow:

``` text
Raw Password
     |
     v
BCryptPasswordEncoder
     |
     v
Hashed Password
     |
     v
Database
```

Never log or expose raw passwords.

------------------------------------------------------------------------

## DTO Mapping

MapStruct is used to convert request DTOs to entities.

Example concept:

``` text
UserRegisterRequestDTO
          |
          | MapStruct
          v
         User
```

Server-managed properties such as IDs, timestamps, and roles can be
excluded from client-controlled mapping where appropriate.

------------------------------------------------------------------------

## Project Structure

``` text
src/main/java/com/flowtech/eventhub_spring_boot_backend
|
+-- controller
|   +-- authentication endpoints
|   +-- admin endpoints
|   +-- organiser endpoints
|   +-- customer endpoints
|
+-- service
|   +-- authentication/business logic
|   +-- event business logic
|
+-- repository
|   +-- UserRepository
|   +-- RoleRepository
|   +-- EventRepository
|   +-- BookingRepository
|
+-- entity
|   +-- User
|   +-- Role
|   +-- Event
|   +-- Booking
|
+-- dto
|   +-- registration/login DTOs
|
+-- mapper
|   +-- MapStruct mappers
|
+-- security
|   +-- SecurityFilterChain configuration
|   +-- Custom UserDetailsService
|   +-- JWT generator
|   +-- JwtEncoder / JwtDecoder
|
+-- enums
    +-- event/payment status enums
```

------------------------------------------------------------------------

## Database Setup

Create a MySQL database for the application.

Example:

``` sql
CREATE DATABASE eventhub_db;
```

Configure the database connection in `application.properties` or through
environment variables.

Example development configuration:

``` properties
spring.datasource.url=jdbc:mysql://localhost:3306/eventhub_db
spring.datasource.username=root
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

> Do not commit real database passwords, API keys, private keys, or
> other secrets to Git.

------------------------------------------------------------------------

## Running the Application

### Prerequisites

Install:

-   Java 21
-   Maven
-   MySQL
-   Git
-   Postman or another API client

### Clone

``` bash
git clone <your-repository-url>
cd eventhub-spring-boot-backend
```

### Configure Database

Create the MySQL database and configure your datasource
properties/environment variables.

### Run with Maven

On Windows:

``` bash
mvnw.cmd spring-boot:run
```

On macOS/Linux:

``` bash
./mvnw spring-boot:run
```

Or run the Spring Boot main class from your IDE.

------------------------------------------------------------------------

## API Testing Flow

A recommended testing sequence is:

``` text
1. Register user
        |
2. Login
        |
3. Receive JWT
        |
4. Add JWT as Bearer Token
        |
5. Call role-protected API
        |
6. Organiser creates event
        |
7. Admin reviews event
        |
8. Admin approves/rejects event
        |
9. Customer views approved events
```

------------------------------------------------------------------------

## Current Development Status

### Implemented

-   User registration
-   User login
-   BCrypt password hashing
-   JWT generation
-   RSA-based JWT validation
-   Stateless Spring Security
-   Role-based authorization
-   Organiser event creation
-   Event verification states
-   Admin event approval/rejection workflow
-   Customer retrieval of approved events
-   JPA entity relationships
-   DTO mapping with MapStruct

### In Progress / Planned

-   Complete event booking workflow
-   Ticket inventory handling
-   Payment workflow
-   Booking history
-   Email notifications
-   Better validation and exception handling
-   Pagination and filtering
-   Persistent/externally managed RSA keys
-   Production security hardening
-   Automated unit/integration tests

------------------------------------------------------------------------

## Production Improvements

Before a production deployment, the project can be enhanced with:

-   Persistent RSA keys or an external identity provider
-   Refresh-token strategy where required
-   Request validation using Jakarta Bean Validation
-   Custom exception hierarchy and standardized error responses
-   Transactional ticket booking
-   Optimistic/pessimistic locking for ticket inventory
-   Idempotent payment processing
-   Pagination and event search/filtering
-   Rate limiting
-   Structured logging
-   Docker deployment
-   CI/CD
-   Unit and integration tests
-   Database migrations using Flyway or Liquibase

------------------------------------------------------------------------

## Key Learning Outcomes

This project demonstrates practical understanding of:

-   REST API architecture
-   Spring Boot layered architecture
-   Authentication vs authorization
-   Spring Security
-   JWT authentication
-   RSA cryptography
-   Role-based access control
-   JPA/Hibernate relationships
-   MySQL persistence
-   DTO design
-   MapStruct
-   Password security
-   Event approval workflows
-   Secure association of resources with authenticated users

------------------------------------------------------------------------

## Author

**Mohammad Masood Ansari**

Java / Spring Boot Developer

------------------------------------------------------------------------

## License

This project is intended for learning, portfolio, and demonstration
purposes. Add an appropriate open-source license if the repository will
be distributed publicly.
