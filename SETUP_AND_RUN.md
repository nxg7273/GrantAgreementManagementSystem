# Setup and Running Instructions for Grant Agreement Management System

This document provides step-by-step instructions on how to set up and run the Grant Agreement Management System.

## Prerequisites

Ensure you have the following installed on your system:

1. Java Development Kit (JDK) 11 or later
2. Node.js and npm (for React frontend)
3. MySQL Server
4. Maven (for building the Spring Boot backend)

## Database Setup

1. Start your MySQL server.
2. Create a new database:
   ```
   mysql -u root -p
   CREATE DATABASE grant_management;
   USE grant_management;
   ```
3. Run the SQL scripts to create tables and insert sample data:
   ```
   mysql -u root -p grant_management < create_tables.sql
   mysql -u root -p grant_management < insert_sample_data.sql
   ```

## Backend Setup (Java Spring Boot)

1. Navigate to the backend directory:
   ```
   cd /path/to/grant-agreement-system/backend
   ```
2. Update the `src/main/resources/application.properties` file with your MySQL credentials:
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
   The backend should now be running on `http://localhost:8080`.

## Frontend Setup (ReactJS)

1. Navigate to the frontend directory:
   ```
   cd /path/to/grant-agreement-system/frontend
   ```
2. Install dependencies:
   ```
   npm install
   ```
3. Update the API base URL in `src/config.js` if necessary:
   ```javascript
   export const API_BASE_URL = 'http://localhost:8080/api';
   ```
4. Start the development server:
   ```
   npm start
   ```
   The frontend should now be accessible at `http://localhost:3000`.

## Apache PDFBox and Java Signature API Integration

The backend already includes Apache PDFBox and Java Signature API dependencies. These are used in the document generation and digital signature services. No additional setup is required for these libraries.

## Running the Application

1. Ensure the MySQL server is running.
2. Start the backend server as described in the "Backend Setup" section.
3. Start the frontend development server as described in the "Frontend Setup" section.
4. Access the application by navigating to `http://localhost:3000` in your web browser.

## Troubleshooting

- If you encounter database connection issues, verify your MySQL credentials and ensure the server is running.
- For backend build failures, check that you have the correct version of JDK and Maven installed.
- If the frontend fails to connect to the backend, verify that the backend is running and the API_BASE_URL is set correctly in the frontend configuration.

For any persistent issues, please refer to the project documentation or contact the development team.
