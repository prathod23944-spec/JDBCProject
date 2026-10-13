# Student Management System — Java JDBC & MySQL

A console-based Student Management System built using Java, JDBC, MySQL, Maven, and HikariCP.

## Features

* Add a new student
* View students with pagination
* Search students by name
* Update student details
* Delete students with confirmation
* Input validation and exception handling
* JDBC database connectivity
* HikariCP connection pooling
* JUnit tests for model and database operations

## Technologies Used

* Java
* JDBC
* MySQL
* Maven
* HikariCP
* JUnit 5
* IntelliJ IDEA
* Git and GitHub

## Database Setup

1. Start your MySQL server.

2. Create the database:

   ```sql
   CREATE DATABASE college;
   ```

3. Select the database:

   ```sql
   USE college;
   ```

4. Create the students table:

   ```sql
   CREATE TABLE students (
       id INT PRIMARY KEY,
       name VARCHAR(50),
       age INT,
       course VARCHAR(50)
   );
   ```

## Configuration

Configure these environment variables before running the application:

* `DB_URL` — JDBC connection URL (optional; defaults to `jdbc:mysql://localhost:3306/college`)
* `DB_USER` — MySQL username (optional; defaults to `root`)
* `DB_PASSWORD` — MySQL password (required)

Never commit real database passwords or other secrets to GitHub.

## Run the Project

1. Install a compatible JDK and MySQL.
2. Open the project in IntelliJ IDEA as a Maven project.
3. Ensure Maven dependencies are loaded.
4. Set the required environment variables in the run configuration.
5. Run `StudentManagementSystem.java`.

## Run Tests

Run the tests from IntelliJ IDEA, or execute:

```bash
mvn test
```

Database integration tests require a running MySQL server, the `college` database, the `students` table, and valid database environment variables.

## Project Structure

```text
JDBCProject/
├── src/
│   ├── Main/Java/
│   │   ├── DBConnection.java
│   │   ├── Student.java
│   │   ├── StudentDAO.java
│   │   ├── StudentService.java
│   │   └── StudentManagementSystem.java
│   └── Test/java/
│       ├── StudentTest.java
│       └── StudentDAOIntegrationTest.java
├── pom.xml
├── .gitignore
└── README.md
```

## Author

Developed as a Java JDBC and MySQL learning project.
