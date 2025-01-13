# To-do API with Spark

Welcome to the **To-do API with Spark** repository! This project is a lightweight backend API for managing a to-do list application, built using modern tools and frameworks like Spark Java, Sql2o, and JUnit. 

This project was created as part of Unit 6 of my Java Web Development Techdegree at Treehouse.

## Features

- **RESTful API:** Implements CRUD operations (Create, Read, Update, Delete) adhering to RESTful design principles.
- **Lightweight Architecture:** Uses Spark Java for efficient request handling and routing.
- **Database Integration:** Leverages Sql2o for database interactions and H2 as an in-memory database for testing and development.
- **Error Handling:** Robust exception handling ensures meaningful feedback for clients.
- **Testing:** Includes unit and functional tests to validate the API's reliability and scalability.

## Technology Stack

- **Spark Java:** Framework for building RESTful web services.
- **Sql2o:** Lightweight library for database interaction.
- **H2 Database:** Fast, in-memory database for development and testing.
- **Gson:** For JSON serialization and deserialization.
- **JUnit:** Framework for unit and functional testing.

## Prerequisites

Before running the application, ensure you have the following installed:

- Java 11 or higher
- Gradle

## Getting Started

### Clone the Repository

```
git clone https://github.com/apatino16/todoAPIWithSpark.git
cd todoAPIWithSpark
```

### Build the Project

Use Gradle to build the project and download dependencies:

```
gradle build
```

### Run the Application

Start the application using Gradle:

```
gradle run
```

By default, the API will run on `http://localhost:4567`.

### Access the API

You can interact with the API using tools like Postman or cURL. Below are the available endpoints:

#### Endpoints

1. **GET /api/v1/todos**
   - Fetch all to-do items.
   - Response: JSON array of to-dos.

   Example:
   ```
   curl -X GET http://localhost:4567/api/v1/todos
   ```

2. **POST /api/v1/todos**
   - Create a new to-do item.
   - Request Body:
     ```
     {
       "name": "Sample Task",
       "isCompleted": false
     }
     ```

   Example:
   ```
   curl -X POST http://localhost:4567/api/v1/todos \
        -H "Content-Type: application/json" \
        -d '{"name":"Sample Task","isCompleted":false}'
   ```

3. **PUT /api/v1/todos/{id}**
   - Update an existing to-do item.
   - Request Body:
     ```
     {
       "name": "Updated Task",
       "isCompleted": true
     }
     ```

   Example:
   ```
   curl -X PUT http://localhost:4567/api/v1/todos/1 \
        -H "Content-Type: application/json" \
        -d '{"name":"Updated Task","isCompleted":true}'
   ```

4. **DELETE /api/v1/todos/{id}**
   - Delete a to-do item by ID.

   Example:
   ```
   curl -X DELETE http://localhost:4567/api/v1/todos/1
   ```

## Project Structure

```
.
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── com.teamtreehouse.techdegrees
│   │   │   │   ├── Api.java
│   │   │   │   ├── model
│   │   │   │   │   └── Todo.java
│   │   │   │   ├── dao
│   │   │   │   │   ├── TodoDao.java
│   │   │   │   │   └── Sql2oTodoDao.java
│   │   ├── resources
│   │   │   └── init.sql
│   ├── test
│       ├── java
│       │   ├── com.teamtreehouse.techdegrees.api
│       │   │   └── ApiFunctionalTest.java
│       │   ├── com.teamtreehouse.techdegrees.dao
│       │   │   ├── Sql2oTodoDaoTest.java
│       │   ├── com.teamtreehouse.techdegrees.model
│       │   │   └── TodoTest.java
```

## Testing

### Run Tests

Execute the unit and functional tests using Gradle:

```
gradle test
```

### Test Coverage

- **Unit Tests:** Validate the core functionality of the To-do model and DAO layer.
- **Functional Tests:** Verify API endpoints respond correctly to HTTP requests and interact as expected with the database.

## Future Enhancements

- Add support for advanced query parameters (e.g., filtering and sorting).
- Implement user authentication and authorization.
