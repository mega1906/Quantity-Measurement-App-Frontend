# Quantity Measurement Application

This is a Spring Boot REST API that handles different quantity measurement operations such as unit conversion, arithmetic calculations, and comparisons across multiple measurement types like length, weight, volume, and temperature.

The application also maintains a complete history of all operations for audit and debugging purposes.

## Features

- Convert quantities between compatible units  
- Perform arithmetic operations (add, subtract, multiply, divide) using a single endpoint  
- Compare two quantities and determine if one is greater, lesser, or equal  
- Special handling for temperature conversions (Celsius and Fahrenheit)  
- Store detailed operation history including results, errors, and timestamps  
- Retrieve history by operation type or measurement type  
- Stateless REST APIs with CORS enabled  

## Available APIs

- Quantity operations  
  - `/api/v1/quantities/convert`  
  - `/api/v1/quantities/arithmetic`  
  - `/api/v1/quantities/compare`

- Units  
  - `/units/type/{measurementType}`  
  - `/units`

- Conversions  
  - `/conversions/all`  
  - `/conversions/from/{fromUnit}/to/{toUnit}`

- History  
  - `/history`  
  - `/history/operation/{operation}`  
  - `/history/type/{measurementType}`  
  - `/history` (DELETE)

- Temperature  
  - `/temperature/convert`  
  - `/temperature/scales`  

## Tech Stack

- Java 
- Spring Boot  
- Spring Security (stateless, CORS enabled)  
- Spring Data JPA  
- H2 (in‑memory database for development) 
- MySQL
- Maven  

## Testing

The project includes automated tests written using **JUnit** and **Mockito**.  
These tests cover service logic, edge cases, error handling, and controller behavior to ensure the API works as expected.

## Running Tests

To run all tests:
```bash
mvn test
```

## Running the Application

Start the application using:
```bash
mvn spring-boot:run
```

## Access URLs

Swagger UI:
```bash
http://localhost:8081/swagger-ui/index.html
```

H2 Console:
```bash
http://localhost:8081/h2-console
```

## Notes

This project follows a clean separation between controllers, services, repositories, DTOs, and entities.  
Security settings are configured for development and testing and should be reviewed before production use.