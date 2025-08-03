# Letras Vivas Book API

A RESTful API for managing books in the "Letras Vivas" editorial catalog. This project is built with Spring Boot, PostgreSQL, and follows best practices for modular architecture.

## 🚀 Features

- **CRUD Operations**: Create, Read, Update, and Delete books
- **Search Functionality**: Search books by title
- **Validation**: Input validation using Jakarta Bean Validation
- **Documentation**: Swagger/OpenAPI documentation
- **Error Handling**: Comprehensive error handling with proper HTTP status codes
- **Modular Architecture**: Clean separation of concerns with layered architecture

## 🏗️ Architecture

The project follows a modular architecture with clear separation of concerns:

```
src/main/java/com/udb/letrasvivas/bookapi/
├── book/
│   ├── controller/     # REST controllers
│   ├── service/        # Business logic
│   ├── repository/     # Data access layer
│   ├── model/          # Entity classes
│   ├── dto/            # Data Transfer Objects
│   └── exception/      # Exception handling
└── LetrasVivasBookApiApplication.java
```

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- Docker and Docker Compose
- PostgreSQL (via Docker)

## 🛠️ Installation & Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd LetrasVivasBookAPI
```

### 2. Configure Environment Variables

**Option A: Using the setup script (Recommended)**

```bash
./setup-env.sh
```

**Option B: Manual setup**

Copy the environment template and configure your variables:

```bash
cp env.template .env
```

Edit the `.env` file with your preferred configuration:

```bash
# Database Configuration
DB_HOST=localhost
DB_PORT=5433
DB_NAME=letras_db
DB_USER=postgres
DB_PASSWORD=postgres

# Application Configuration
APP_PORT=8081
APP_NAME=letras-vivas-api

# JPA Configuration
JPA_DDL_AUTO=update
JPA_SHOW_SQL=true
JPA_DIALECT=org.hibernate.dialect.PostgreSQLDialect

# Logging Configuration
LOG_LEVEL=DEBUG
```

### 3. Start PostgreSQL Database

```bash
docker-compose up -d
```

This will start a PostgreSQL container with the configuration from your `.env` file:
- **Database**: `letras_db` (or your DB_NAME value)
- **Username**: `postgres` (or your DB_USER value)
- **Password**: `postgres` (or your DB_PASSWORD value)
- **Port**: `5433` (or your DB_PORT value)

### 4. Run the Application

```bash
./mvnw spring-boot:run
```

The application will start on port `8081`.

## 📚 API Endpoints

### Base URL: `http://localhost:8081/api/books`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/books` | Get all books |
| GET | `/api/books/{id}` | Get book by ID |
| GET | `/api/books/search?title={title}` | Search books by title |
| POST | `/api/books` | Create a new book |
| PUT | `/api/books/{id}` | Update an existing book |
| DELETE | `/api/books/{id}` | Delete a book |

### Example Request Body (POST/PUT)

```json
{
  "title": "The Great Gatsby",
  "author": "F. Scott Fitzgerald",
  "publicationYear": 1925
}
```

## 📖 API Documentation

### Swagger UI
Once the application is running, you can access the Swagger UI documentation at:

**http://localhost:8081/swagger-ui.html**

### Postman Collection
Import the provided Postman collection to test all endpoints:

1. **Download**: `LetrasVivasBookAPI.postman_collection.json`
2. **Import**: In Postman, go to File → Import → Upload Files
3. **Select**: Choose the JSON file
4. **Configure**: The collection uses the variable `{{base_url}}` set to `http://localhost:8081`

**Collection includes:**
- ✅ All CRUD operations
- ✅ Search functionality
- ✅ Validation tests
- ✅ Error handling tests
- ✅ Sample data for testing

## 🧪 Testing the API

### Using curl

1. **Get all books**:
   ```bash
   curl -X GET http://localhost:8081/api/books
   ```

2. **Create a new book**:
   ```bash
   curl -X POST http://localhost:8081/api/books \
     -H "Content-Type: application/json" \
     -d '{
       "title": "Don Quixote",
       "author": "Miguel de Cervantes",
       "publicationYear": 1605
     }'
   ```

3. **Search books by title**:
   ```bash
   curl -X GET "http://localhost:8081/api/books/search?title=Quixote"
   ```

4. **Delete a book**:
   ```bash
   curl -X DELETE http://localhost:8081/api/books/1
   ```

## 🛡️ Validation Rules

- **Title**: Required, cannot be blank
- **Author**: Required, cannot be blank
- **Publication Year**: Required, must be 1000 or greater

## 🔧 Configuration

The application uses environment variables for configuration. Copy `env.template` to `.env` and customize as needed:

### Environment Variables

| Variable | Description | Default Value |
|----------|-------------|---------------|
| `DB_HOST` | Database host | `localhost` |
| `DB_PORT` | Database port | `5433` |
| `DB_NAME` | Database name | `letras_db` |
| `DB_USER` | Database username | `postgres` |
| `DB_PASSWORD` | Database password | `postgres` |
| `APP_PORT` | Application port | `8081` |
| `APP_NAME` | Application name | `letras-vivas-api` |
| `JPA_DDL_AUTO` | JPA DDL strategy | `update` |
| `JPA_SHOW_SQL` | Show SQL queries | `true` |
| `LOG_LEVEL` | Logging level | `DEBUG` |

### Configuration Files

- **Environment**: `.env` (copy from `env.template`)
- **Application**: `src/main/resources/application.properties`
- **Docker**: `docker-compose.yml`

## 🏃‍♂️ Development

### Running Tests

```bash
./mvnw test
```

### Building the Application

```bash
./mvnw clean package
```

### Running with Maven

```bash
./mvnw spring-boot:run
```

## 🔧 Troubleshooting

### Database Connection Issues

If you encounter database connection errors:

1. **Check if Docker is running**:
   ```bash
   docker --version
   docker ps
   ```

2. **Start the database**:
   ```bash
   docker-compose up -d
   ```

3. **Verify database is running**:
   ```bash
   docker-compose ps
   ```

4. **Check environment variables**:
   ```bash
   cat .env
   ```

5. **Restart the application**:
   ```bash
   ./mvnw spring-boot:run
   ```

### Common Issues

- **Port already in use**: Change `APP_PORT` in `.env` file
- **Database connection failed**: Ensure PostgreSQL container is running
- **Permission denied**: Make sure `setup-env.sh` is executable (`chmod +x setup-env.sh`)

## 📦 Dependencies

- **Spring Boot 3.5.4**: Core framework
- **Spring Data JPA**: Data access layer
- **PostgreSQL**: Database
- **Jakarta Bean Validation**: Input validation
- **Lombok**: Reduce boilerplate code
- **SpringDoc OpenAPI**: API documentation
- **Spring Boot DevTools**: Development utilities

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📄 License

This project is created for educational purposes as part of the POO course at Universidad Don Bosco.

## 👥 Team

- [Your Name] - [Your Student ID]
- [Team Member 2] - [Student ID]
- [Team Member 3] - [Student ID]

---

**Note**: This is a fictional project for the "Letras Vivas" editorial. The API is designed to be scalable and follows industry best practices for Spring Boot applications. 