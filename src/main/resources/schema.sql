-- Drop table if exists (for development)
DROP TABLE IF EXISTS users;

-- Create users table
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       phone VARCHAR(20)
);

-- Create index for email lookups
CREATE INDEX idx_users_email ON users(email);

-- Create index for name searches
CREATE INDEX idx_users_name ON users(name);

-- Insert sample data for testing
INSERT INTO users (name, email, phone) VALUES
                                           ('John Doe', 'john.doe@example.com', '+1234567890'),
                                           ('Jane Smith', 'jane.smith@example.com', '+1234567891'),
                                           ('Bob Johnson', 'bob.johnson@example.com', '+1234567892');