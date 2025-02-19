## Bookstore

### Introduction

- **Welcome to the Bookstore**! This project is designed to streamline the management of book inventory and simplify the order handling process. Whether you run a small shop or a large bookstore chain, the system offers an efficient solution for managing categories, books, and searches. It helps you organize sales processes and inventory control as conveniently and transparently as possible.
## Technologies Used
-  **Java 17**: The core programming language used for development.
-  **Spring Boot 3.2.0**: For building the backend application.
-  **Spring Security 6.2.0**: To handle authentication and authorization.
-  **Spring Data JPA 3.2.0**: For database interactions.
-  **SpringDoc OpenAPI 2.3.0**: For API documentation (Swagger UI).
-  **JUnit 5.10.0 & Mockito 5.7.0**: For unit testing.
-  **H2 Database 2.2.224**: An in-memory database used for testing.
-  **Maven 3.9.5**: For project management and dependency management.
-  **MySQL 8.0**: Production database.
-  **Docker 24.0.7**: For containerizing the application.

## Key Functionalities
### BookController
- **Get all books**: Retrieve a list of all books with pagination and sorting.
- **Get book by ID**: Fetch details of a specific book by its ID.
- **Create a new book**: Add a new book to the collection.
- **Update book by ID**: Modify details of an existing book.
- **Delete book by ID**: Remove a book from the collection.
- **Search books**: Search for books based on various parameters.

### CategoryController
- **Get all categories**: Retrieve a list of all categories.
- **Get category by ID**: Fetch details of a specific category by its ID.
- **Create a new category**: Add a new category to the collection.
- **Update category by ID**: Modify details of an existing category.
- **Delete category by ID**: Remove a category from the collection.

## Setup and Usage
### Prerequisites
- Java 17 or higher
- Maven 3.6.3 or higher
- Docker
- An IDE like IntelliJ IDEA

### Steps to Run the Project
1. **Clone the repository**:
    ```sh
    git clone https://github.com/Extase67/BookShop.git
    cd BookShop
    ```

2. **Build the project**:
    ```sh
    mvn clean install
    ```

3. **Run the application**:
    ```sh
    mvn spring-boot:run
    ```

4. **Access the application**:
    - The application will be running at `http://localhost:8080`.
    - Swagger UI can be accessed at `http://localhost:8080/swagger-ui.html`.

### Entity Relationship
![Entity Relationships.png](Entity%20Relationships.png)

### Database Setup
The project uses an H2 in-memory database for testing purposes. For a production setup, you can configure a different database in the `application.properties` file.

### Running Tests
To run the tests, use the following command:
```sh
mvn test
```

### Docker Setup
To run the application using Docker Compose, follow these steps:

1. **Create an `.env` file in the root directory**:
    ```properties
    MYSQL_ROOT_PASSWORD=root
    MYSQL_DATABASE=bookstore
    MYSQL_USER=user
    MYSQL_PASSWORD=password
    SPRING_PROFILES_ACTIVE=prod
    JWT_SECRET=your_jwt_secret_key
    JWT_EXPIRATION=86400000
    ```

2. **Create a `docker-compose.yml` file**:
    ```yaml
    version: '3.8'
    services:
      mysql:
        image: mysql:8.0
        env_file: .env
        ports:
          - "3306:3306"
        volumes:
          - mysql_data:/var/lib/mysql
        healthcheck:
          test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
          timeout: 5s
          retries: 10

      app:
        build: .
        env_file: .env
        ports:
          - "8080:8080"
        depends_on:
          mysql:
            condition: service_healthy

    volumes:
      mysql_data:
    ```

3. **Run the application**:
    ```sh
    docker-compose up -d
    ```

4. **Access the application**:
    - The application will be running at `http://localhost:8080`
    - Swagger UI can be accessed at `http://localhost:8080/swagger-ui.html`

5. **Stop the application**:
    ```sh
    docker-compose down
    ```
## Challenges Faced
- **Database Integration**: Ensuring seamless integration with the database was challenging. We overcame this by using Spring Data JPA and writing comprehensive tests.
- **Security**: Implementing robust security measures using Spring Security to protect the endpoints.
- **Testing**: Writing extensive unit tests to ensure the reliability of the application.

## Conclusion
This project showcases a full-fledged book management system with a focus on clean code and robust testing. It reflects our dedication to building reliable and maintainable software solutions.

---

Happy coding!
