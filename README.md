### README.md

```markdown
# Bidding System

This project is a bidding system where users can place bids on items. It is developed using Spring Boot and can be run using Docker.

## Requirements

- Java 11+
- Maven 3.6+
- Docker
- Docker Compose

## Setup

### 1. Clone the Repository

```bash
git clone https://github.com/your_username/bidding-system.git
cd bidding-system
```

### 2. Install Dependencies

```bash
mvn clean install
```

### 3. Configure the Database

Edit the `src/main/resources/application.properties` file to configure your database. For example, if you are using PostgreSQL, you can set the properties as follows:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/bidding_db
spring.datasource.username=postgres
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

### 4. Initialize the Database Schema

Load the database schema provided in the `V1__Initial_Setup.sql` file into your database. This file creates the necessary tables and inserts some initial data.

#### Load the Database Schema

```bash
psql -U postgres -d bidding_db -f V1__Initial_Setup.sql
```

#### V1__Initial_Setup.sql Content

```sql
DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_tables WHERE schemaname = 'public' AND tablename = 'app_user') THEN
            CREATE TABLE app_user (
                                      id BIGINT PRIMARY KEY,
                                      username VARCHAR(50) NOT NULL,
                                      password VARCHAR(100) NOT NULL,
                                      email VARCHAR(100) NOT NULL,
                                      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      UNIQUE (username),
                                      UNIQUE (email)
            );
        END IF;
    END
$$;

DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_tables WHERE schemaname = 'public' AND tablename = 'item') THEN
            CREATE TABLE item (
                                  id BIGINT PRIMARY KEY,
                                  name VARCHAR(255) NOT NULL,
                                  starting_price DECIMAL(10, 2) NOT NULL,
                                  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
            );
        END IF;
    END
$$;

DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_tables WHERE schemaname = 'public' AND tablename = 'offer') THEN
            CREATE TABLE offer (
                                   id BIGINT PRIMARY KEY,
                                   amount DECIMAL(10, 2) NOT NULL,
                                   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   user_id BIGINT,
                                   item_id BIGINT,
                                   CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES app_user (id),
                                   CONSTRAINT fk_item FOREIGN KEY (item_id) REFERENCES item (id)
            );
        END IF;
    END
$$;

-- Insert initial user data if it doesn't exist
INSERT INTO app_user (id, username, password, email)
SELECT 1, 'testuser1', 'password1', 'testuser1@example.com'
WHERE NOT EXISTS (SELECT 1 FROM app_user WHERE id = 1);

INSERT INTO app_user (id, username, password, email)
SELECT 2, 'testuser2', 'password2', 'testuser2@example.com'
WHERE NOT EXISTS (SELECT 1 FROM app_user WHERE id = 2);

-- Insert initial item data if it doesn't exist
INSERT INTO item (id, name, starting_price)
SELECT 1, 'Item1', 50.00
WHERE NOT EXISTS (SELECT 1 FROM item WHERE id = 1);

INSERT INTO item (id, name, starting_price)
SELECT 2, 'Item2', 100.00
WHERE NOT EXISTS (SELECT 1 FROM item WHERE id = 2);

-- Optionally, insert initial offer data if it doesn't exist
INSERT INTO offer (id, amount, user_id, item_id)
SELECT 1, 100.00, 1, 1
WHERE NOT EXISTS (SELECT 1 FROM offer WHERE id = 1);

INSERT INTO offer (id, amount, user_id, item_id)
SELECT 2, 200.00, 2, 2
WHERE NOT EXISTS (SELECT 1 FROM offer WHERE id = 2);
```

### 5. Run the Project with Docker

This project can be easily run using Docker. Make sure Docker and Docker Compose are installed on your system.

#### Running with Docker Compose

1. Create a `docker-compose.yml` file in the project root directory with the following content:

```yaml
version: '3.8'

services:
  app:
    image: openjdk:11
    container_name: bidding-system
    ports:
      - "8080:8080"
    volumes:
      - .:/app
    working_dir: /app
    command: ["sh", "-c", "mvn clean spring-boot:run"]

  db:
    image: postgres:latest
    container_name: postgres-db
    environment:
      POSTGRES_DB: bidding_db
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: password
    ports:
      - "5432:5432"
    volumes:
      - postgres-data:/var/lib/postgresql/data

volumes:
  postgres-data:
```

2. Run the Docker Compose command:

```bash
docker-compose up
```

This command will start the PostgreSQL database and the Spring Boot application in Docker containers.

### 6. Open the Application in Your Browser

Once the application is successfully started, you can open it in your browser at `http://localhost:8080`.

## API Usage

### Item APIs

- **Create Item**
  - URL: `POST /api/items`
  - Body: `{"name": "itemName", "description": "itemDescription", "startingPrice": 100}`
  - Params: `userId`

- **Get Item by ID**
  - URL: `GET /api/items/{id}`

- **Get All Items**
  - URL: `GET /api/items`

### User APIs

- **Create User**
  - URL: `POST /api/users`
  - Body: `{"username": "username", "password": "password"}`

- **Get User by ID**
  - URL: `GET /api/users/{id}`

- **Get All Users**
  - URL: `GET /api/users`

### Offer APIs

- **Create Offer**
  - URL: `POST /api/offers`
  - Body: `{"itemId": 1, "amount": 150, "userId": 2}`

- **Get Offer by ID**
  - URL: `GET /api/offers/{id}`

- **Get All Offers**
  - URL: `GET /api/offers`

