-- Populate Grant Agreement Management System Database

-- Insert sample clients
INSERT INTO clients (name, email, organization) VALUES
('John Doe', 'john.doe@example.com', 'ABC Corporation'),
('Jane Smith', 'jane.smith@example.com', 'XYZ Foundation'),
('Bob Johnson', 'bob.johnson@example.com', '123 Nonprofit');

-- Insert sample grants
INSERT INTO grants (client_id, name, description, amount, legal_text, status) VALUES
(1, 'Community Development Grant', 'Funding for local community projects', 50000.00, 'This grant is provided to support community development initiatives...', 'ACTIVE'),
(2, 'Educational Scholarship Fund', 'Scholarships for underprivileged students', 100000.00, 'This scholarship fund aims to provide educational opportunities...', 'ACTIVE'),
(3, 'Environmental Conservation Grant', 'Support for environmental protection projects', 75000.00, 'This grant is dedicated to supporting initiatives that protect and preserve...', 'DRAFT');

-- Insert sample participants
INSERT INTO participants (name, email, organization) VALUES
('Alice Brown', 'alice.brown@example.com', 'Local Community Center'),
('Charlie Davis', 'charlie.davis@example.com', 'Green Earth NGO'),
('Eva Wilson', 'eva.wilson@example.com', 'City School District');

-- Insert sample agreements
INSERT INTO agreements (grant_id, participant_id, status, document_path) VALUES
(1, 1, 'PENDING', '/agreements/community_dev_001.pdf'),
(2, 3, 'ACCEPTED', '/agreements/scholarship_001.pdf'),
(3, 2, 'PENDING', '/agreements/environmental_001.pdf');

-- Insert sample audit logs
INSERT INTO audit_logs (entity_type, entity_id, action, details) VALUES
('GRANT', 1, 'CREATE', 'Created new Community Development Grant'),
('AGREEMENT', 2, 'SIGN', 'Agreement signed by participant'),
('PARTICIPANT', 3, 'UPDATE', 'Updated participant contact information');

-- Insert sample auto-acceptance criteria
INSERT INTO auto_acceptance_criteria (grant_id, condition_type, condition_value) VALUES
(1, 'AMOUNT_THRESHOLD', '10000'),
(2, 'TIME_LIMIT', '30'),
(3, 'PARTICIPANT_TYPE', 'NONPROFIT');

-- Display inserted data
SELECT 'Clients' AS table_name, COUNT(*) AS record_count FROM clients
UNION ALL
SELECT 'Grants' AS table_name, COUNT(*) AS record_count FROM grants
UNION ALL
SELECT 'Participants' AS table_name, COUNT(*) AS record_count FROM participants
UNION ALL
SELECT 'Agreements' AS table_name, COUNT(*) AS record_count FROM agreements
UNION ALL
SELECT 'Audit Logs' AS table_name, COUNT(*) AS record_count FROM audit_logs
UNION ALL
SELECT 'Auto-acceptance Criteria' AS table_name, COUNT(*) AS record_count FROM auto_acceptance_criteria;
