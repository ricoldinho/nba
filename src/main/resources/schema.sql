-- Drop tables if they exist
DROP TABLE IF EXISTS players;
DROP TABLE IF EXISTS teams;
DROP TABLE IF EXISTS users;

-- Create users table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);

-- Create teams table
CREATE TABLE teams (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    city VARCHAR(255),
    country VARCHAR(255),
    founding_year INT,
    active BOOLEAN DEFAULT TRUE
);

-- Create players table
CREATE TABLE players (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    jersey_number INT,
    country_of_origin VARCHAR(255),
    birth_date DATE,
    team_id BIGINT NOT NULL,
    type VARCHAR(255),
    height_cm DOUBLE,
    weight_kg DOUBLE,
    points_per_game DOUBLE,
    assists_per_game DOUBLE,
    rebounds_per_game DOUBLE,
    field_goal_percentage DOUBLE,
    photo_url VARCHAR(255),
    active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (team_id) REFERENCES teams(id)
);