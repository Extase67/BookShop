## Bookstore

### Introduction

- **Welcome to the Bookstore**! This project is designed to streamline the management of book inventory and simplify the order handling process. Whether you run a small shop or a large bookstore chain, the system offers an efficient solution for managing categories, books, and searches. It helps you organize sales processes and inventory control as conveniently and transparently as possible.
## Technologies Used
- **Java**: The core programming language used for development.
- **Spring Boot**: For building the backend application.
- **Spring Security**: To handle authentication and authorization.
- **Spring Data JPA**: For database interactions.
- **Swagger**: For API documentation.
- **JUnit & Mockito**: For unit testing.
- **H2 Database**: An in-memory database used for testing.
- **Maven**: For project management and dependency management.
- **Docker**: For containerizing the application.

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
    git clone https://github.com/yourusername/book-management-system.git
    cd book-management-system
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

### Database Setup
The project uses an H2 in-memory database for testing purposes. For a production setup, you can configure a different database in the `application.properties` file.

### Running Tests
To run the tests, use the following command:
```sh
mvn test
```

### Docker Setup
To run the application using Docker, follow these steps:

1. **Build the Docker image**:
    ```sh
    docker build -t book-management-system .
    ```

2. **Run the Docker container**:
    ```sh
    docker run -p 8080:8080 book-management-system
    ```

3. **Access the application**:
    - The application will be running at `http://localhost:8080`.
    - Swagger UI can be accessed at `http://localhost:8080/swagger-ui.html`.

## Challenges Faced
- **Database Integration**: Ensuring seamless integration with the database was challenging. We overcame this by using Spring Data JPA and writing comprehensive tests.
- **Security**: Implementing robust security measures using Spring Security to protect the endpoints.
- **Testing**: Writing extensive unit tests to ensure the reliability of the application.

## Conclusion
This project showcases a full-fledged book management system with a focus on clean code and robust testing. It reflects our dedication to building reliable and maintainable software solutions.

---

Happy coding!
