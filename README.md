# Class Booking System

A backend service for an online learning platform where teachers create class offerings and parents can browse and book them. Built to handle multiple timezones, conflict detection, and concurrent bookings.

## Tech Stack

- Java 17
- Spring Boot 3.5.14
- MySQL
- Spring Data JPA
- Maven

## Database Tables

- `courses` — the actual course (e.g. Python Coding)
- `offerings` — a batch of a course (e.g. Saturday Batch)
- `sessions` — individual class times within an offering (stored in UTC)
- `bookings` — tracks which parent booked which offering

## Environment Variables

Set these in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/class_booking?useSSL=false&serverTimezone=UTC
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.jdbc.time_zone=UTC
server.port=8081
```

## Steps to Run Locally

1. Make sure Java 17 and MySQL are installed
2. Create the database:
```sql
CREATE DATABASE class_booking;
```
3. Update `application.properties` with your MySQL username and password
4. Run the app:
```bash
mvn spring-boot:run
```
5. App starts at `http://localhost:8081`

## API Documentation

### Teacher APIs

#### Create Offering
```
POST /api/teacher/offerings
```
```json
{
    "name": "Saturday Batch",
    "teacherId": 1,
    "teacherTimezone": "Asia/Kolkata",
    "courseId": 1
}
```

#### Add Session
```
POST /api/teacher/sessions
```
```json
{
    "offeringId": 1,
    "startTime": "2026-06-07T18:00:00+05:30",
    "endTime": "2026-06-07T19:00:00+05:30"
}
```

#### Get Teacher Offerings
```
GET /api/teacher/offerings/{teacherId}
```

### Parent APIs

#### Get Available Offerings
```
GET /api/parent/offerings?timezone=America/New_York
```

#### Book Offering
```
POST /api/parent/bookings
```
```json
{
    "parentId": 1,
    "parentTimezone": "America/New_York",
    "offeringId": 1
}
```

#### Get Bookings
```
GET /api/parent/bookings/{parentId}
```

## Timezone Handling

Teachers create sessions in their own timezone. The app converts all times to UTC before saving to the database. When a parent views offerings, session times are automatically converted to their local timezone. This way everyone sees the correct time regardless of where they are.

## Conflict Detection

Before confirming a booking, the app checks if any session in the new offering overlaps with any session the parent has already booked. If there is an overlap, the booking is rejected with a clear error message. The overlap check used is:

```
newSession.start < bookedSession.end 
AND bookedSession.start < newSession.end
```

## Concurrency Handling

The booking method uses `@Transactional` to ensure that if two parents try to book the same offering at the exact same time, the data stays consistent and no invalid bookings go through.

## Assumptions Made

- Authentication is kept simple — teacherId and parentId are passed directly in the request body
- A course must exist in the database before creating an offering
- Sessions are added one by one after creating an offering
- A parent cannot book the same offering twice
- All session times sent by the teacher include their timezone offset