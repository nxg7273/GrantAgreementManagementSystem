-- Create GRANTS table
CREATE TABLE IF NOT EXISTS grants (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    legal_text LONGTEXT NOT NULL,
    auto_acceptance_enabled BOOLEAN NOT NULL,
    auto_acceptance_days INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    grant_number VARCHAR(255)
);

-- Create PARTICIPANTS table
CREATE TABLE IF NOT EXISTS participants (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    organization VARCHAR(255) NOT NULL,
    email_notifications_enabled BOOLEAN NOT NULL,
    in_app_notifications_enabled BOOLEAN NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    grant_id BIGINT,
    FOREIGN KEY (grant_id) REFERENCES grants(id)
);

-- Create AGREEMENTS table
CREATE TABLE IF NOT EXISTS agreements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    participant_id BIGINT NOT NULL,
    grant_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at DATE NOT NULL,
    updated_at DATE,
    document_path VARCHAR(255),
    accepted_at DATE,
    expires_at DATE,
    FOREIGN KEY (participant_id) REFERENCES participants(id),
    FOREIGN KEY (grant_id) REFERENCES grants(id)
);
