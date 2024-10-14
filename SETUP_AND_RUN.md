# Setup and Running Instructions for Grant Agreement Management System

This document provides step-by-step instructions on how to set up and run the Grant Agreement Management System.

## Prerequisites

Ensure you have the following installed on your system:

1. Java Development Kit (JDK) 17 or later
2. Node.js 20.x or later and npm
3. MySQL Server 8.0 or later
4. Maven 3.6 or later (for building the Spring Boot backend)

## Database Setup

1. Start your MySQL server.
2. Create a new database and user:
   ```
   mysql -u root -p
   CREATE DATABASE grant_management;
   CREATE USER 'grantuser'@'localhost' IDENTIFIED BY 'grantpassword';
   GRANT ALL PRIVILEGES ON grant_management.* TO 'grantuser'@'localhost';
   FLUSH PRIVILEGES;
   EXIT;
   ```
3. Run the SQL scripts to create tables and insert sample data:
   ```
   mysql -u grantuser -pgrantpassword grant_management < create_tables.sql
   mysql -u grantuser -pgrantpassword grant_management < insert_sample_data.sql
   ```

## Backend Setup (Java Spring Boot)

1. Navigate to the backend directory:
   ```
   cd /path/to/GrantAgreementManagementSystem/backend
   ```
2. Update the `src/main/resources/application.properties` file with your MySQL credentials if different from the setup above:
   ```
   spring.datasource.url=jdbc:mysql://localhost:3306/grant_management
   spring.datasource.username=grantuser
   spring.datasource.password=grantpassword
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
   cd /path/to/GrantAgreementManagementSystem/frontend
   ```
2. Install dependencies:
   ```
   npm install
   ```
3. Start the development server:
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

## Note on the "grant-agreement-system" Directory

The "grant-agreement-system" directory contains an alternative or older version of the backend system implemented using Spring Boot. It is not required for the current setup and can be considered as a reference or backup implementation. The main application uses the "backend" directory for its server-side logic.

## Troubleshooting

- If you encounter database connection issues, verify your MySQL credentials and ensure the server is running.
- For backend build failures, check that you have the correct version of JDK and Maven installed.
- If the frontend fails to connect to the backend, verify that the backend is running and the API base URL is set correctly in the frontend configuration.
- For any npm-related issues in the frontend setup, try deleting the `node_modules` folder and running `npm install` again.
- If you face any port conflicts, you can modify the port numbers in `application.properties` for the backend and in the `package.json` scripts for the frontend.

For any persistent issues, please refer to the project documentation or contact the development team.
