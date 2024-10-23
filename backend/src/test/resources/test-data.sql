-- Insert test data for grants
INSERT INTO grants (title, description, amount, legal_text, auto_acceptance_enabled, auto_acceptance_days, start_date, end_date, status, grant_number)
VALUES
('Test Grant 1', 'Description for Test Grant 1', 10000.00, 'Legal text for Test Grant 1', true, 7, '2023-01-01', '2023-12-31', 'ACTIVE', 'GR001'),
('Test Grant 2', 'Description for Test Grant 2', 20000.00, 'Legal text for Test Grant 2', false, 0, '2023-02-01', '2023-11-30', 'PENDING', 'GR002');

-- Insert test data for participants
INSERT INTO participants (name, email, organization, email_notifications_enabled, in_app_notifications_enabled, phone_number, grant_id)
VALUES
('John Doe', 'john@example.com', 'Org A', true, true, '1234567890', 1),
('Jane Smith', 'jane@example.com', 'Org B', true, false, '0987654321', 2);

-- Insert test data for agreements
INSERT INTO agreements (participant_id, grant_id, status, signed_date)
VALUES
(1, 1, 'ACCEPTED', '2023-01-15'),
(2, 2, 'PENDING', NULL);
