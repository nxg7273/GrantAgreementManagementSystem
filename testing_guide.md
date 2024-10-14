# Grant Agreement Management System Testing Guide

## 1. Backend Testing

### 1.1 Health Check
- Use curl to test the health endpoint:
  ```
  curl http://localhost:8080/api/health
  ```
- Expected result: "Application is running"

### 1.2 Authentication
- Test user login:
  ```
  curl -X POST -H "Content-Type: application/json" -d '{"username":"admin","password":"password"}' http://localhost:8080/api/auth/login
  ```
- Expected result: JWT token

### 1.3 Grant Management
- Create a new grant (requires authentication)
- Retrieve all grants
- Retrieve a specific grant
- Update a grant
- Delete a grant

### 1.4 Agreement Management
- Create a new agreement
- Retrieve all agreements
- Retrieve a specific agreement
- Update an agreement status
- Test auto-acceptance functionality

### 1.5 Document Generation
- Generate an agreement document
- Verify the document is correctly stored

### 1.6 Digital Signature
- Sign a generated document
- Verify the signature on the document

## 2. Frontend Testing

### 2.1 User Interface
- Verify all pages load correctly
- Check responsiveness on different screen sizes

### 2.2 User Authentication
- Test login functionality
- Verify protected routes are not accessible without authentication

### 2.3 Grant Management
- Test creating, viewing, updating, and deleting grants
- Verify form validations

### 2.4 Agreement Management
- Test creating, viewing, and updating agreements
- Verify auto-acceptance functionality through the UI

### 2.5 Document Handling
- Test document generation through the UI
- Verify document signing process

## 3. Integration Testing

### 3.1 End-to-End Grant Agreement Process
- Create a new grant
- Create an agreement for the grant
- Test auto-acceptance (if applicable)
- Generate and sign the agreement document
- Verify all steps are reflected correctly in the database and UI

### 3.2 Error Handling
- Test system behavior with invalid inputs
- Verify appropriate error messages are displayed

## 4. Performance Testing

### 4.1 Load Testing
- Simulate multiple concurrent users
- Monitor system response times and resource usage

### 4.2 Database Performance
- Test with a large dataset
- Verify query performance for listing and searching functionalities

## 5. Security Testing

### 5.1 Authentication and Authorization
- Attempt to access protected endpoints without authentication
- Verify role-based access control

### 5.2 Data Validation
- Test input validation for all forms
- Attempt SQL injection and XSS attacks

### 5.3 API Security
- Verify HTTPS is used for all communications
- Check for proper handling of sensitive data

## 6. Usability Testing

### 6.1 User Experience
- Gather feedback on UI/UX from potential users
- Verify intuitive navigation and clear instructions

### 6.2 Accessibility
- Test with screen readers
- Verify color contrast and keyboard navigation

Remember to document any issues found during testing and create corresponding bug reports or improvement tasks.
