-- Create Grant table
CREATE TABLE grant (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    amount DECIMAL(10, 2) NOT NULL,
    legal_text TEXT NOT NULL,
    auto_accept BOOLEAN NOT NULL DEFAULT FALSE,
    accept_days INT NOT NULL DEFAULT 0
);

-- Create Participant table
CREATE TABLE participant (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20),
    address TEXT
);

-- Create Agreement table
CREATE TABLE agreement (
    id INT AUTO_INCREMENT PRIMARY KEY,
    status ENUM('PENDING', 'ACCEPTED', 'REJECTED') NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    accepted_at TIMESTAMP NULL,
    document_path VARCHAR(255),
    grant_id INT NOT NULL,
    participant_id INT NOT NULL,
    FOREIGN KEY (grant_id) REFERENCES grant(id),
    FOREIGN KEY (participant_id) REFERENCES participant(id)
);
