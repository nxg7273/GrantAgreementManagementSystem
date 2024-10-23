# Database Design for Grant Agreement Management System

## Entity-Relationship Diagram (ERD)

```
+-------------+      +-------------+      +-------------+
|   Grant     |      | Agreement   |      | Participant |
+-------------+      +-------------+      +-------------+
| id          |      | id          |      | id          |
| name        |      | status      |      | name        |
| description |      | created_at  |      | email       |
| amount      |      | accepted_at |      | phone       |
| legal_text  |      | document_path|      | address     |
| auto_accept |<---->| grant_id    |<---->| agreement_id|
| accept_days |      | participant_id     |             |
+-------------+      +-------------+      +-------------+
```

## Table Descriptions

### Grant
- `id`: Unique identifier for the grant (Primary Key)
- `name`: Name of the grant
- `description`: Detailed description of the grant
- `amount`: Monetary value of the grant
- `legal_text`: Legal text associated with the grant agreement
- `auto_accept`: Boolean flag indicating if auto-acceptance is enabled
- `accept_days`: Number of days after which auto-acceptance occurs

### Agreement
- `id`: Unique identifier for the agreement (Primary Key)
- `status`: Current status of the agreement (e.g., PENDING, ACCEPTED, REJECTED)
- `created_at`: Timestamp when the agreement was created
- `accepted_at`: Timestamp when the agreement was accepted (null if not accepted)
- `document_path`: File path to the signed agreement document
- `grant_id`: Foreign key referencing the Grant table
- `participant_id`: Foreign key referencing the Participant table

### Participant
- `id`: Unique identifier for the participant (Primary Key)
- `name`: Name of the participant
- `email`: Email address of the participant
- `phone`: Phone number of the participant
- `address`: Physical address of the participant

## Relationships
- One Grant can have many Agreements (One-to-Many)
- One Participant can have many Agreements (One-to-Many)
- One Agreement belongs to one Grant and one Participant (Many-to-One for both)
