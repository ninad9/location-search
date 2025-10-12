# Location Search Application

A Spring Boot web application that lets users search for locations by ZIP code or city name.  
On login, any combination of userId and password is accepted.  
All logged-in users are tracked via session state, and login events are logged.

---

## Features

**Login**  
Any combination of `userId` and `password` is accepted.  
Login state is stored in HTTP session.  
Logs Login events.

- **Search by ZIP or City**
    - If you provide a valid ZIP or City, the app returns the corresponding Location (if exists).
    - Exact-match only (no partial matching).
    - Input is normalized (case-insensitive).

- **Error Handling & Redirects**
    - Unauthenticated access to `/search` or `/result` redirects to login.
    - Blank or invalid input renders an error message on the search page.
    - A custom `/error` mapping redirects fallback errors to root, avoiding a Whitelabel page.

- **Logging**
    - Uses SLF4J to log login, logout, and search operations.
    - Info-level logs show “User logged in: X”, search queries, found vs not found, etc.

- **HTML Views**
    - A `login.html` page for credentials.
    - A `search.html` template to display city, state, country, and the list of ZIP codes from `Location`.

- **Unit & MVC Tests**
    - Controller and service logic have comprehensive test coverage, including positive and negative cases.

- **Dockerfile**
    - Includes a Dockerfile for containerizing the app.

- **Logout (Additional feature)**
Invalidates Login state stored in HTTP session.
Logs Logout event

---

## Project Structure
src/
├─ main/
│ ├─ java/
│ │ ├─ controller/
│ │ │ ├─ LoginController.java
│ │ │ ├─ SearchController.java
│ │ │ └─ ErrorMappingController.java
│ │ ├─ service/
│ │ │ └─ SearchService.java
│ │ ├─ dto/
│ │ │ ├─ LoginRequest.java
│ │ ├─ model/
│ │ │└─ Location.java
│ │ ├─ exception/
│ │ │ └─ GlobalExceptionHandler.java
│ └─ resources/
│ ├─ templates/
│ │ ├─ login.html
│ │ └─ search.html
│ └─ application.properties
└─ test/
└─ java/

---

## Tech Stack
- Java 
- Spring Boot
- Maven
- Docker
---

## Docker Usage
```bash
docker build -t locationsearch-app:latest .
docker run -d -p <port-number>:8080 locationsearch-app:latest
```
