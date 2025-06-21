# 🕒 Employee Check-In/Out System – Microservices Architecture with JWT & Kafka

A full-stack microservices application that manages employee check-ins and check-outs, secured through robust JWT-based authentication and routed via Spring Cloud Gateway. Built using Spring Boot, Angular, Kafka, and Docker, the system is designed for scalability, modularity, and secure asynchronous processing.

---

##  Architecture Overview

![Diagram_employee](https://github.com/user-attachments/assets/d2ba3d69-b3bd-4cdc-b2d6-dd44ffa8e0eb)


**Key Highlights:**
- Spring Boot backend with RESTful APIs
- Angular frontend with reactive UI
- Stateless JWT authentication across services
- Kafka-based messaging for decoupled event flow
- Docker Compose orchestration for unified local and cloud deployments

---

##  Tech Stack

| Component          | Technology                                |
|--------------------|--------------------------------------------|
| **Frontend**        | Angular, RxJS, BehaviorSubject             |
| **Backend**         | Spring Boot, Spring Web, Spring Security   |
| **Auth Gateway**    | Spring Cloud Gateway + JWT Filter          |
| **Event Streaming** | Apache Kafka, Spring Kafka                 |
| **Database**        | MySQL (employee, check-in/out) + MongoDB (logs/events) |
| **Infrastructure**  | Docker, Docker Compose                     |

---

##  Features

-  Secure login and role-based access with JWT
-  Real-time employee check-in/out with timestamped logs
-  Kafka-based decoupled architecture for asynchronous processing
-  API gateway (Spring Cloud Gateway) enforcing centralized security
-  Modular microservices for clear domain separation
-  Fully containerized for easy deployment

---

##  System Structure

###  Spring Cloud Gateway
- Acts as the single entry point for all backend services
- Validates JWT tokens
- Routes requests to appropriate microservices

###  JWT Authentication
- Stateless auth via signed JWT tokens
- Roles and access rights encoded in token payload
- Auth headers validated by Gateway before request forwarding

###  Kafka
- Used as async message bus between services
- Example: check-in events published from Attendance Service to be logged/stored

###  Microservices

####  **Attendance Service**
- Exposes APIs for `/checkin`, `/checkout`
- Timestamps each event and publishes to Kafka
- Stores activity in MySQL

####  **Employee Service**
- Handles employee creation, login, and role assignment
- Issues JWT tokens on successful login

####  **Admin Dashboard**
- Provides employee attendance history
- Aggregates event streams for UI

---

##  Persistence Layer

- **MySQL**: Stores employee profiles and check-in/out history
- **MongoDB**: Stores logs, event payloads, and audit trails

---

##  Containerization

- Entire system orchestrated using Docker Compose
- Each service defined as its own container (frontend, backend, Kafka, DBs)
- Dev/prod environments supported via `.env` files

```bash
# Start all services
docker-compose up --build
