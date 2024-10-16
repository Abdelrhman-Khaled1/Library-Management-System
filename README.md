# Library Management System

This project is a **Library Management System** built using **Spring Boot**. It provides a set of API endpoints for managing books and interacting with the system. The application uses **Spring Security** for authentication and **Spring Data JPA** for database operations.

![maids cc](https://github.com/user-attachments/assets/eff31b15-1e2c-4690-b2c5-dcafe57fdba5)


## Overview
I have implemented **all mandatory tasks**, as well as **optional tasks** such as security, logging with Aspect-Oriented Programming (AOP), caching, and transaction management. Additionally, I have written **comprehensive test cases** to ensure the correctness of the API functionality.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
  - [Authentication](#authentication)
  - [Book Management](#book-management)
- [Testing](#testing)
- [Contact Information](#contact-information)

## Prerequisites

Before running this application, ensure you have the following installed:

- **Java 17** or later
- **Maven 3.6+**
- **MySQL** or any preferred relational database

## Getting Started

1. Clone the repository:

   ```bash
   git clone https://github.com/your-repository-url/library-management-system.git
   cd library-management-system ```

2. Set Up the Database
The application uses MySQL as the database. You need to set up a MySQL database and configure the connection settings in the project.

  Open the project and navigate to src/main/resources/application.yml, and update the database connection details:
```
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/Maids.cc?createDatabaseIfNotExist=true
    username: root
    password: root
 ```

3. Build and Run the Application:
The application will start running on the default port 8080.

## API Documentation

This section provides details about the API endpoints, authentication, and how to interact with various parts of the Library Management System.

### API Endpoints

The base URL for all endpoints is:
``` http://localhost:8080/api/v1 ```



### Authentication

The Library Management System uses **JWT (JSON Web Token)** for authentication. Users must Sign up and obtain a token to access protected endpoints.

#### 1. Register a New User

To register a new user, send a `POST` request to:
http://localhost:8080/api/v1/auth/register

**Request Body Example**:

```
{
    "firstname" : "Abdelrhman",
    "lastname": "Khaled",
    "email" : "kabdo17013@gmail.com",
    "password":"1234"
}
```

**Response Example**:
```
{
  "token": "your.jwt.token.here"
}
```

### You can download all the postman endpoints from here

https://drive.google.com/file/d/1MTzfl7XGVHUxehUTTH15Do4FuDH3xVw4/view?usp=sharing

### Caching
To enhance performance, I have implemented caching using Spring's caching mechanisms. Frequently accessed data, such as book and patron information, is cached to improve response times.

___

### Logging and Aspects
I have implemented logging using Aspect-Oriented Programming (AOP). Key operations, such as book additions, updates, and patron transactions, are logged for performance and auditing purposes.
   
___


### Testing
The project includes comprehensive test cases for validating the API functionality. Unit tests are written using JUnit and Mockito. These tests ensure the correctness of all API endpoints, as well as the caching and security configurations.
___
