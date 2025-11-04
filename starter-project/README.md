OHS Digital Health API
==================
This repository contains the Digital Health API, which provides endpoints for accessing and managing digital health data.
It provides a RESTful API for managing patients, encounters, and observations in a healthcare setting and built with Spring Boot, and includes validation, error handling, and automated tests for service and controller layers.

Getting Started
---------------
### Prerequisites

* Java 17+
* Maven 3.8+

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/Muta-Jonathan/ohs-backend-developer-exercise.git
    cd ohs-backend-developer-exercise
    ```
2. Build the project using Maven:
   ```bash
   mvn clean install
   ```
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```
4. The application starts by default at -> http://localhost:8080

API Endpoints
-----------------
### Patient Endpoints
- `POST /api/patient`: Create a new patient.

Example request body:
```json
{
  "identifier": "P124556",
  "givenName": "erf",
  "familyName": "ty",
  "birthDate": "1985-07-12",
  "gender": "F"
}
```
- `GET /api/patient/{id}`: Retrieve patient details by ID.

Example response body:
```json
{
  "id": 1,
  "uuid": "3000c52d-5eb7-4158-8b55-e2a315e16d5a",
  "identifier": "P123456",
  "givenName": "John",
  "familyName": "Doe",
  "birthDate": "1985-07-12T00:00:00.000+00:00",
  "gender": "M",
  "encounters": [],
  "observations": []
}
  ```

- `PUT /api/patient/{id}`: Update patient information.
- `DELETE /api/patient/{id}`: Delete a patient by ID.
- `GET /api/patient/all`: List all patients.

Example response body:
```json
[
    {
        "id": 1,
        "uuid": "3000c52d-5eb7-4158-8b55-e2a315e16d5a",
        "identifier": "P123456",
        "givenName": "John",
        "familyName": "Doe",
        "birthDate": "1985-07-12T00:00:00.000+00:00",
        "gender": "M",
        "encounters": [],
        "observations": []
    },
    {
        "id": 2,
        "uuid": "7b75bb38-8220-4140-8432-817dd3c01620",
        "identifier": "P124556",
        "givenName": "erf",
        "familyName": "ty",
        "birthDate": "1985-07-12T00:00:00.000+00:00",
        "gender": "F",
        "encounters": [],
        "observations": []
    }
]
```
- `GET /api/patient?family=&given=&identifier=&birthDate=`: Search patients by optional query parameters: family name, given name, identifier, and birth date.

### Encounter Endpoints
- `POST /api/encounters`: Create a new encounter.

Example request body:
```json
{
  "patientId": 1,
  "encounterDate": "2023-10-01T10:00:00.000+00:00",
  "type": "Outpatient"
}
```
- `GET /api/encounters/{id}`: Retrieve encounter details by ID.

### Observation Endpoints
- `POST /api/observations`: Create a new observation.
- `GET /api/observations/{id}`: Retrieve observation details by ID.

Testing
-------
To run the automated tests for the service and controller layers, use the following command:
```bash
mvn test
```

Technologies Used
-----------------
- Java 17
- Spring Boot
- Maven
- JUnit 5
- Mockito
- H2 Database (for testing)

API Documentation
-----------------
Swagger/OpenAPI is enabled for easy API exploration.

After starting the app, open:
-> http://localhost:8080/swagger-ui.html

or
-> http://localhost:8080/v3/api-docs
(JSON format)

Design Choices and Trade-offs
-----------------
While building this API, I made a few design decisions to keep things clean, maintainable, and easy to test.
I focused on readability and quick iteration

* Used Date instead of LocalDateTime
I went with Date mainly for simplicity and better compatibility.

* Used annotation-based validation
I added validation rules directly on model fields using annotations like @NotBlank, @NotNull. It keeps validation logic close to the data model and avoids cluttering controller code with manual checks.

* Consistent error responses
All validation and API errors return a clear JSON structure with an error type and message. This makes it easier for clients to understand what went wrong instead of dealing with generic stack traces.

* Swagger/OpenAPI for documentation
Instead of writing manual API docs, I integrated Swagger so I can automatically generate and view them in the browser. It’s faster to maintain and helps during testing and debugging.

* Simple relationships between Patient, Encounter, and Observation
I kept the relationships lightweight — no complex cascading or deep bidirectional mapping. It’s easier to reason about, especially when testing or posting nested data.
