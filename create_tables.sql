-- Create Grant table
CREATE TABLE IF NOT EXISTS `grant` (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    amount DECIMAL(10, 2) NOT NULL,
    legal_text TEXT NOT NULL,
    auto_accept BOOLEAN NOT NULL DEFAULT FALSE,
    accept_days INT NOT NULL DEFAULT 0
);

-- Create Participant table
CREATE TABLE IF NOT EXISTS participant (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20),
    address TEXT
);

-- Create Agreement table
CREATE TABLE IF NOT EXISTS agreement (
    id INT AUTO_INCREMENT PRIMARY KEY,
    status ENUM('PENDING', 'ACCEPTED', 'REJECTED') NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    accepted_at TIMESTAMP NULL,
    document_path VARCHAR(255),
    grant_id INT NOT NULL,
    participant_id INT NOT NULL,
    FOREIGN KEY (grant_id) REFERENCES `grant`(id),
    FOREIGN KEY (participant_id) REFERENCES participant(id)
);

-- Create User table
CREATE TABLE IF NOT EXISTS user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Insert test user
INSERT IGNORE INTO user (username, password) VALUES ('testuser_1729221017@example.com', '$2a$10$r88SZZ.qxcJ4dn3tOthjbeY7xx.ywmENXuEXCQRMjjZKLaLFWWJT6');
