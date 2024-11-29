# Eureka Example

This is a **Spring Boot** project demonstrating service discovery using Netflix Eureka and Docker Compose. The project includes a Eureka Server and two client services (`service-client1` and `service-client2`), all containerized with Docker.

## Features
- **Eureka Server**: A service registry to manage service discovery.
- **Service-Client1**: A client application that registers itself with Eureka Server and exposes a simple endpoint.
- **Service-Client2**: Another client application that also registers with Eureka Server and demonstrates inter-service communication by calling an endpoint in `service-client1`.

## Prerequisites
Before running this project, ensure you have the following installed:
- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)
- [Maven](https://maven.apache.org/) (only for building the project locally, optional)

---

## Project Structure

```
eureka-example/
├── docker-compose.yml       # Docker Compose configuration file
├── eureka-server/           # Eureka Server module
│   ├── Dockerfile           # Dockerfile for Eureka Server
│   ├── pom.xml              # Maven configuration for Eureka Server
│   └── src/                 # Source code and resources for Eureka Server
├── service-client1/         # Service Client 1 module
│   ├── Dockerfile           # Dockerfile for Service Client 1
│   ├── pom.xml              # Maven configuration for Service Client 1
│   └── src/                 # Source code and resources for Service Client 1
└── service-client2/         # Service Client 2 module
    ├── Dockerfile           # Dockerfile for Service Client 2
    ├── pom.xml              # Maven configuration for Service Client 2
    └── src/                 # Source code and resources for Service Client 2
```

---

## How to Run

### 1. Build the Project
If you want to build the project locally (optional):
```bash
mvn clean package
```

### 2. Start the Services
Run the following command to start all services using Docker Compose:
```bash
docker compose up -d
```

This will:
- Start the Eureka Server on `http://localhost:8761`
- Start `service-client1` on `http://localhost:8081`
- Start `service-client2` on `http://localhost:8082`

### 3. Verify Eureka Server
Visit `http://localhost:8761` to view the Eureka Server dashboard. You should see `service-client1` and `service-client2` registered.

### 4. Test the Services

- **Service-Client1**:  
  Open `http://localhost:8081/hello` in your browser. You should see:
  ```
  Hello from Service Client 1
  ```

- **Service-Client2**:  
  Open `http://localhost:8082/call-client1` in your browser. This endpoint calls `service-client1` via Eureka and should return:
  ```
  Hello from Service Client 1
  ```

---

## Configuration Details

### **Docker Compose**
The `docker-compose.yml` file defines the three services:
- `eureka-server`: Acts as the service registry.
- `service-client1`: Registers with Eureka and exposes a `/hello` endpoint.
- `service-client2`: Registers with Eureka and demonstrates service-to-service communication.

Key configuration:
```yaml
services:
  eureka-server:
    build:
      context: ./eureka-server
    ports:
      - "8761:8761"
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8761/actuator/health"]
      interval: 10s
      timeout: 5s
      retries: 5

  service-client1:
    build:
      context: ./service-client1
    ports:
      - "8081:8081"
    depends_on:
      eureka-server:
        condition: service_healthy

  service-client2:
    build:
      context: ./service-client2
    ports:
      - "8082:8082"
    depends_on:
      eureka-server:
        condition: service_healthy
```

### **Application Configuration**

#### **Eureka Server (`eureka-server/src/main/resources/application.yml`):**
```yaml
server:
  port: 8761

spring:
  application:
    name: eureka-server

eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
```

#### **Service-Client1 (`service-client1/src/main/resources/application.yml`):**
```yaml
server:
  port: 8081

spring:
  application:
    name: service-client1

eureka:
  client:
    service-url:
      defaultZone: http://eureka-server:8761/eureka/
```

#### **Service-Client2 (`service-client2/src/main/resources/application.yml`):**
```yaml
server:
  port: 8082

spring:
  application:
    name: service-client2

eureka:
  client:
    service-url:
      defaultZone: http://eureka-server:8761/eureka/
```

---

## Key Components

### **Eureka Server**
- Acts as the central registry for service discovery.
- Starts on `http://localhost:8761`.

### **Service-Client1**
- Exposes a simple `/hello` endpoint.
- Registers itself with Eureka Server.

### **Service-Client2**
- Calls `service-client1`'s `/hello` endpoint via Eureka using a `RestTemplate`.

---

## Troubleshooting

### **1. Services Not Registered**
- Ensure the Eureka Server is running and accessible at `http://localhost:8761`.
- Check `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` in the client configurations.

### **2. Services Fail to Start**
- Ensure Docker and Docker Compose are installed and running.
- Check logs for errors:
  ```bash
  docker logs <container_name>
  ```

### **3. Containers Fail to Communicate**
- Ensure all services are on the same network:
  ```bash
  docker network inspect eureka-example_eureka-network
  ```

---

## Clean Up
To stop all services and remove containers:
```bash
docker compose down
```

---

## Future Enhancements
- Add more client services to demonstrate scaling.
- Integrate Spring Cloud Gateway for API routing.
- Use distributed tracing tools like Zipkin or Sleuth for service monitoring.


