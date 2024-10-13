-- Insert sample data into Grant table
INSERT INTO grant (name, description, amount, legal_text, auto_accept, accept_days)
VALUES
('Research Grant 2024', 'Funding for innovative research projects', 50000.00, 'This grant is subject to the terms and conditions...', TRUE, 30),
('Community Development Fund', 'Supporting local community initiatives', 25000.00, 'By accepting this grant, the recipient agrees to...', FALSE, 0),
('Educational Scholarship', 'Promoting higher education opportunities', 10000.00, 'The scholarship recipient must maintain a GPA of...', TRUE, 14);

-- Insert sample data into Participant table
INSERT INTO participant (name, email, phone, address)
VALUES
('John Doe', 'john.doe@example.com', '123-456-7890', '123 Main St, Anytown, USA'),
('Jane Smith', 'jane.smith@example.com', '987-654-3210', '456 Elm St, Othertown, USA'),
('Bob Johnson', 'bob.johnson@example.com', '555-123-4567', '789 Oak St, Somewhere, USA');

-- Insert sample data into Agreement table
INSERT INTO agreement (status, created_at, grant_id, participant_id)
VALUES
('PENDING', CURRENT_TIMESTAMP, 1, 1),
('ACCEPTED', CURRENT_TIMESTAMP, 2, 2),
('REJECTED', CURRENT_TIMESTAMP, 3, 3);

-- Update accepted agreement with accepted_at timestamp
UPDATE agreement SET accepted_at = CURRENT_TIMESTAMP WHERE status = 'ACCEPTED';
