-- Populate Grant Agreement Management System Database

-- Insert sample grants
INSERT IGNORE INTO `grant` (name, description, amount, legal_text, auto_accept, accept_days) VALUES
('Community Development Grant', 'Funding for local community projects', 50000.00, 'This grant is provided to support community development initiatives...', FALSE, 30),
('Educational Scholarship Fund', 'Scholarships for underprivileged students', 100000.00, 'This scholarship fund aims to provide educational opportunities...', TRUE, 14),
('Environmental Conservation Grant', 'Support for environmental protection projects', 75000.00, 'This grant is dedicated to supporting initiatives that protect and preserve...', FALSE, 0);

-- Insert sample participants
INSERT IGNORE INTO participant (name, email, phone, address) VALUES
('Alice Brown', 'alice.brown@example.com', '123-456-7890', 'Local Community Center, 123 Main St, Anytown, USA'),
('Charlie Davis', 'charlie.davis@example.com', '987-654-3210', 'Green Earth NGO, 456 Oak Ave, Othertown, USA'),
('Eva Wilson', 'eva.wilson@example.com', '555-123-4567', 'City School District, 789 Elm St, Somewhere, USA');

-- Insert sample agreements
INSERT IGNORE INTO agreement (status, created_at, grant_id, participant_id) VALUES
('PENDING', CURRENT_TIMESTAMP, 1, 1),
('ACCEPTED', CURRENT_TIMESTAMP, 2, 3),
('PENDING', CURRENT_TIMESTAMP, 3, 2);

-- Display inserted data
SELECT 'Grants' AS table_name, COUNT(*) AS record_count FROM `grant`
UNION ALL
SELECT 'Participants' AS table_name, COUNT(*) AS record_count FROM participant
UNION ALL
SELECT 'Agreements' AS table_name, COUNT(*) AS record_count FROM agreement;
