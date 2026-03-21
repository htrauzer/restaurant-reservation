# Restaurant Reservation System

A smart, full-stack restaurant reservation and table management system built with Spring Boot and modern web technologies. Features intelligent table recommendations, real-time availability checking, admin controls, and Docker support.

## 🌟 Features

- **Smart Reservation System**: Book tables with intelligent recommendations based on party size and preferences
- **Table Management**: Visual table layout editor with drag-and-drop positioning
- **Real-time Availability**: Check table availability for specific dates and times
- **Admin Dashboard**: Manage tables, view reservations, and update table positions
- **Automatic Table Release**: Scheduled task for automatic table release after reservation time
- **Zone Management**: Organize tables into zones (e.g., VIP, Standard, Bar seating)
- **Table Features**: Support for table amenities (outdoor, window view, etc.)
- **Comprehensive Testing**: Unit tests for core business logic (ReservationService, MealService, RecommendationService)

## 🛠️ Tech Stack

- **Backend**: Spring Boot 3.5.11 with Java 21
- **Frontend**: Thymeleaf, HTML5, CSS3, JavaScript
- **Database**: PostgreSQL (with H2 for development)
- **ORM**: Spring Data JPA with Hibernate
- **Build Tool**: Maven
- **Containerization**: Docker & Docker Compose
- **Testing**: JUnit 5, Mockito

## 📋 Prerequisites

- Java 21 or higher
- Maven 3.9.6 or higher
- PostgreSQL 15 (if running without Docker)
- Docker & Docker Compose (for containerized deployment)

## 🚀 Getting Started

### Docker Deployment

1. **Build and run with Docker Compose**:
   ```bash
   docker-compose up --build
   ```

2. **Access the application**:
   - Frontend: `http://localhost:8080`
   - Database: `postgresql://localhost:5432/restaurant_db`
   - Credentials: `user = admin` / `password = 1234`

3. **Stop the services**:
   ```bash
   docker-compose down
   ```

### Local Development (H2 Database)

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd restaurant-reservation
   ```

2. **Build the project**:
   ```bash
   mvn clean package
   ```

3. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**:
   - Open your browser and navigate to `http://localhost:8080`



## 📁 Project Structure

```
src/
├── main/
│   ├── java/ee/cgi/practice/reservation/
│   │   ├── RestaurantReservationApplication.java    # Main application entry point
│   │   ├── config/
│   │   │   └── RestTemplateConfig.java              # HTTP client configuration
│   │   ├── controller/
│   │   │   ├── AdminController.java                 # Admin API endpoints
│   │   │   ├── ReservationController.java           # Reservation API endpoints
│   │   │   ├── RestaurantController.java            # Restaurant data endpoints
│   │   │   ├── TableController.java                 # Table management endpoints
│   │   │   └── ViewController.java                  # Web page views
│   │   ├── dto/                                     # Data Transfer Objects
│   │   ├── model/
│   │   │   ├── Reservation.java                     # Reservation entity
│   │   │   ├── RestaurantTable.java                 # Table entity
│   │   │   ├── TableFeature.java                    # Table amenities enum
│   │   │   └── Zone.java                            # Seating zone enum
│   │   ├── repository/                              # Spring Data JPA repositories
│   │   └── service/
│   │       ├── BookingService.java                  # Core booking logic
│   │       ├── MealService.java                     # Meal-related operations
│   │       ├── RecommendationService.java           # Table recommendations
│   │       ├── ReservationService.java              # Reservation management
│   │       └── OccupancyGenerator.java              # Occupancy simulation
│   └── resources/
│       ├── application.properties                   # Application configuration
│       ├── import.sql                               # Database initialization script
│       ├── static/
│       │   ├── css/
│       │   │   ├── admin.css                        # Admin dashboard styles
│       │   │   ├── base.css                         # Global styles
│       │   │   ├── components.css                   # Component styles
│       │   │   ├── layout.css                       # Layout styles
│       │   │   └── modals.css                       # Modal dialog styles
│       │   └── js/
│       │       ├── admin-logic.js                   # Admin functionality
│       │       ├── api.js                           # API client
│       │       ├── main.js                          # Main app logic
│       │       ├── map.js                           # Table layout map
│       │       └── ui-handlers.js                   # UI event handlers
│       └── templates/
│           ├── admin.html                           # Admin dashboard
│           └── index.html                           # Main reservation page
└── test/
    └── java/ee/cgi/practice/reservation/
        ├── MealServiceTest.java
        ├── RecommendationServiceTest.java
        └── ReservationServiceTest.java
```

## 🔌 API Endpoints

### Reservations
- `GET /api/reservations/find-available` - Find available tables
- `POST /api/reservations` - Create new reservation
- `GET /api/reservations/{id}` - Get reservation details

### Admin
- `PATCH /api/admin/tables/{id}/position` - Update table position on the floor plan

### Restaurant Data
- `GET /api/restaurants` - Get restaurant information
- `GET /api/tables` - Get all tables
- `GET /api/zones` - Get seating zones

### Views
- `GET /` - Main reservation page
- `GET /admin` - Admin dashboard

## ⚙️ Configuration

### Database Configuration (PostgreSQL)

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://db:5432/restaurant_db
spring.datasource.username=user
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
```

### Application Features

- **Scheduling Enabled**: Automatic background tasks for table management
- **Thymeleaf Templating**: Server-side HTML rendering
- **RESTful API**: JSON-based API for frontend communication
- **CORS Enabled**: Cross-origin requests supported

## 🧪 Testing

Run all tests:

```bash
mvn test
```

Run specific test class:

```bash
mvn test -Dtest=ReservationServiceTest
```

## Time spend on the project 50 hours

## The Role of AI in the Workflow
I treated AI as a senior pair programmer during this project. Instead of letting it "write the app," I used it to bounce ideas off of and to help bridge the gap between my logic and the final implementation.

> Architectural Decisions: I designed the core logic for the scoring and reservation systems, then used AI to compare different implementation patterns. This helped me settle on a point-based recommendation system that is both flexible and easy to test.

> Debugging & Refactoring: When shifting from a monolithic structure to a modular one, I used AI to quickly catch syntax errors and "boilerplate" issues that come with moving code between files. This allowed me to focus on the big-picture architecture rather than hunting for missing semicolons.

> Best Practices: I used it as a real-time documentation tool to explore the most efficient ways to handle Java Streams and Date/Time comparisons, ensuring the code is not just working, but written to modern standards.


## PROJECT ISSUE LOG AND RESOLUTION SUMMARY

### 1. Architectural Evolution: From Monolith to Modular Design

#### The Problem
In the early stages of development, the entire backend logic and all frontend features were compressed into only four files. This made the codebase "heavy" and difficult to navigate. As the project grew, finding specific functions became time-consuming, and the risk of creating bugs while editing unrelated code increased significantly.

#### The Cause
A lack of initial strict separation of concerns. All responsibilities—from database access to UI rendering—were bundled together for speed of initial setup.

#### The Solution
Following industry best practices, the project was refactored into a modular structure. I split the large files into specialized services and controllers. For example, reservation-specific logic was moved into its own Service layer, while a dedicated Controller was created for table management. This made the code cleaner, more readable, and easier to scale.

### 2. Stability Management: Handling Variable and Dependency Breaks

#### The Problem
Throughout the development process, adding new features or renaming variables frequently caused other parts of the system to break. Functions would stop working because they were still looking for variables or methods that no longer existed or had been changed.

#### The Cause
High coupling between different parts of the code. When a core variable name was updated in one file, the dependent files were not automatically synchronized.

#### The Solution
To maintain project stability, I implemented a strict manual audit process. Whenever a shared variable or function signature was modified, I immediately performed a "trace-and-update" check across all connected files. This ensured that every change was reflected globally, preventing "undefined" errors and keeping the application functional during rapid development phases.

### 3. API Connectivity: Resolving the 404 Not Found Error

#### The Problem
The administrative dashboard failed to load the restaurant floor plan, showing a 404 Not Found error when trying to access the table data at the /api/tables endpoint.

#### The Cause
There was no dedicated REST endpoint on the backend designed to return a full list of tables. The existing controllers were only set up to handle specific reservation tasks, not general table data retrieval.

#### The Solution
I developed a new TableController class. This controller specifically manages table-related data and includes a GET method that pulls the complete list of tables from the database. This established the necessary communication bridge between the database and the visual map.

### 4. Rendering Logic: Fixing the JS ReferenceError

#### The Problem
The browser console frequently reported a ReferenceError stating that the map could not be found, causing the interactive features of the admin panel to fail.

#### The Source
The JavaScript code was attempting to manipulate the map element before the HTML document had finished loading it. Essentially, the code was asking for an object that didn't exist yet in the browser's memory.

#### The Solution
I restructured the initialization logic within the JavaScript files. By reordering the code so that the document.getElementById calls are executed at the very beginning of the initialization function, I ensured the DOM elements are correctly identified before any logic or event listeners are applied to them.

### 5. Business Logic: Preventing Booking Conflicts

#### The Problem
The system initially allowed "double bookings," where two different customers could successfully reserve the exact same table for the same time slot.

#### The Cause
The original recommendation logic only checked if a table existed, but it did not sufficiently compare the start and end times of new requests against existing entries in the database.

#### The Solution
I upgraded the RecommendationService to include a robust time-overlap filter. The system now cross-references the requested time slot with all existing reservations. If any overlap is detected, the table is automatically excluded from the available list, ensuring 100% booking accuracy.

### 6. UI Accuracy: Correcting Table Placement (0,0 Issue)

#### The Problem
Upon loading the admin page, all restaurant tables appeared "smashed" together in the top-left corner of the screen at coordinates (0,0), making the map unusable.

#### The Cause
This was caused by two factors: first, the initial SQL data used very small coordinate values (under 90px), which looked like a single point on a large map. Second, the JavaScript code was missing the "px" unit suffix when applying styles, causing the browser to ignore the position values entirely.

#### The Solution
I performed a three-part fix:
First, I updated the data.sql file with realistic coordinates (100px and above).
Second, I corrected the JavaScript logic to ensure every coordinate is followed by the "px" unit.
Third, I implemented an "Auto-Grid" algorithm that automatically calculates a neat position for any table that has missing or zero coordinates in the database.




