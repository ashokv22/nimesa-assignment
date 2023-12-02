# Nimesa Assignment API Documentation

This Spring Boot application exposes a set of REST APIs for discovering EC2 instances and S3 buckets, retrieving job results, and querying S3 bucket objects.

## API Endpoints

### 1. Discover Services

#### Endpoint: `GET /api/discover`
- **Description:** Initiates the discovery process for specified services (EC2, S3).
- **Request:**
    - **Method:** GET
    - **URL:** `/api/discover`
    - **RequestBody:** List of Strings representing services (`["EC2", "S3"]`)
- **Response:**
    - **Status Code:** 200 OK
    - **ResponseBody:** JobId (`String`)

### 2. Get Job Result

#### Endpoint: `GET /api/job-result/{jobId}`
- **Description:** Retrieves the status of a job identified by the JobId.
- **Request:**
    - **Method:** GET
    - **URL:** `/api/job-result/{jobId}`
    - **PathVariable:** jobId (`String`)
- **Response:**
    - **ResponseBody:** Job Status (`String`)
    - **Note:** This endpoint returns a CompletableFuture and allows asynchronous querying of job status.

### 3. Get Discovery Result

#### Endpoint: `GET /api/discovery-result/{service}`
- **Description:** Retrieves the result of the discovery for a specific service (EC2 or S3).
- **Request:**
    - **Method:** GET
    - **URL:** `/api/discovery-result/{service}`
    - **PathVariable:** service (`String`)
- **Response:**
    - **ResponseBody:** List of EC2 Instance IDs or S3 Bucket Names (`List<String>`)
    - **Note:** This endpoint returns a CompletableFuture and allows asynchronous querying of discovery results.

### 4. Get S3 Bucket Objects

#### Endpoint: `GET /api/s3-bucket-objects/{bucketName}`
- **Description:** Initiates the discovery of file names in an S3 bucket and persists the result in the database.
- **Request:**
    - **Method:** GET
    - **URL:** `/api/s3-bucket-objects/{bucketName}`
    - **PathVariable:** bucketName (`String`)
- **Response:**
    - **Status Code:** 200 OK
    - **ResponseBody:** JobId (`String`)
    - **Note:** This endpoint returns a CompletableFuture and allows asynchronous initiation of the discovery process.

### 5. Get S3 Bucket Object Count

#### Endpoint: `GET /api/s3-bucket-object-count/{bucketName}`
- **Description:** Retrieves the count of objects in an S3 bucket from the database.
- **Request:**
    - **Method:** GET
    - **URL:** `/api/s3-bucket-object-count/{bucketName}`
    - **PathVariable:** bucketName (`String`)
- **Response:**
    - **ResponseBody:** Object Count (`Integer`)

### 6. Get S3 Bucket Object Like

#### Endpoint: `GET /api/s3-bucket-object-like/{bucketName}`
- **Description:** Retrieves a list of file names in an S3 bucket matching a specified pattern.
- **Request:**
    - **Method:** GET
    - **URL:** `/api/s3-bucket-object-like/{bucketName}`
    - **PathVariable:** bucketName (`String`)
    - **RequestParam:** pattern (`String`)
- **Response:**
    - **ResponseBody:** List of File Names (`List<String>`)

## Usage

1. Clone the repository.
2. Build and run the Spring Boot application.
3. Postman collection included in the Repository. Import `Nimesa Assignment API Documentation.postman_collection.json` in postman and test them out.

## Dependencies

- Java 21
- Spring Boot 3.2.0
- AWS SDK for Java
- MySQL Database
- Maven (for building)

## Configuration

- Update the `application.yml` file with your AWS credentials and database configuration.
- Run the `init.sql` script for to create the tables
## Build and Run

```bash
mvn clean install
java -jar target/spring-boot-aws-s3-ec2-discovery-1.0.0.jar
