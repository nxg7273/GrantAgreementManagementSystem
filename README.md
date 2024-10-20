# Grant Agreement Management System

A comprehensive system for managing grant agreements, including document generation and digital signatures.

This README provides instructions on how to set up and run the Grant Agreement Management System. The system consists of a Java Spring Boot backend, a ReactJS frontend, and uses a MySQL database.

## Prerequisites

Before you begin, ensure you have the following installed:
- Java Development Kit (JDK) 11 or later
- Node.js and npm
- MySQL Server
- Maven

## Backend Setup

1. Navigate to the backend directory:
   ```
   cd /path/to/grant-agreement-system/backend
   ```

2. Configure the database connection in `src/main/resources/application.properties`:
   ```
   spring.datasource.url=jdbc:mysql://localhost:3306/grant_management
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. Build the project:
   ```
   mvn clean install
   ```

4. Run the backend server:
   ```
   mvn spring-boot:run
   ```

The backend server should now be running on `http://localhost:8081`.

## Frontend Setup

1. Navigate to the frontend directory:
   ```
   cd /path/to/grant-agreement-system/frontend
   ```

2. Install dependencies:
   ```
   npm install
   ```

3. Configure the .env file for the frontend:
   ```
   REACT_APP_BACKEND_URL=http://localhost:8081
   ```

4. Start the development server:
   ```
   npm start
   ```

The frontend application should now be running on `http://localhost:3000`.

## Database Setup

1. Create a new MySQL database named `grant_management`:
   ```
   CREATE DATABASE grant_management;
   ```

2. The application will automatically create the necessary tables when it first runs, thanks to Spring Boot's auto-configuration.

## Additional Configuration

### PDF Generation (Apache PDFBox)

The system uses Apache PDFBox for PDF generation. This library is included as a Maven dependency in the backend's `pom.xml` file, so no additional setup is required.

### Digital Signature (Java Signature API)

The system uses the Java Signature API for digital signatures. This is part of the Java standard library, so no additional setup is required.

### WebSocket Functionality

The system includes WebSocket functionality for real-time updates. Ensure that the WebSocket server is running and accessible at the backend URL specified in the .env file.

## Running the Application

1. Start the MySQL server.
2. Run the backend server as described in the "Backend Setup" section.
3. Run the frontend application as described in the "Frontend Setup" section.
4. Access the application by navigating to `http://localhost:3000` in your web browser.

## Troubleshooting

If you encounter any issues:
1. Ensure all prerequisites are correctly installed.
2. Check that the database connection details in `application.properties` are correct.
3. Verify that the required ports (8081 for backend, 3000 for frontend) are not in use by other applications.
4. Ensure the WebSocket connection is established by checking the console logs for connection status.

For any persistent problems, please refer to the project documentation or contact the development team.
