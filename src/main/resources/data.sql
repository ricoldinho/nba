-- NBA Players Sample Data
-- This file is automatically loaded by Spring Boot on application startup

-- Disable foreign key checks for data loading
SET FOREIGN_KEY_CHECKS=0;

-- Insert sample players
INSERT INTO players (name, jersey_number, team, country_of_origin, birth_date, photo_url, active) 
VALUES ('LeBron James', 23, 'Los Angeles Lakers', 'USA', '1984-12-30', 'https://example.com/lebron.jpg', true);

INSERT INTO players (name, jersey_number, team, country_of_origin, birth_date, photo_url, active) 
VALUES ('Kevin Durant', 7, 'Phoenix Suns', 'USA', '1988-09-29', 'https://example.com/durant.jpg', true);

INSERT INTO players (name, jersey_number, team, country_of_origin, birth_date, photo_url, active) 
VALUES ('Stephen Curry', 30, 'Golden State Warriors', 'USA', '1988-03-14', 'https://example.com/curry.jpg', true);

INSERT INTO players (name, jersey_number, team, country_of_origin, birth_date, photo_url, active) 
VALUES ('Luka Doncic', 77, 'Dallas Mavericks', 'Slovenia', '1999-02-28', 'https://example.com/doncic.jpg', true);

INSERT INTO players (name, jersey_number, team, country_of_origin, birth_date, photo_url, active) 
VALUES ('Giannis Antetokounmpo', 34, 'Milwaukee Bucks', 'Greece', '1994-12-06', 'https://example.com/giannis.jpg', true);

INSERT INTO players (name, jersey_number, team, country_of_origin, birth_date, photo_url, active) 
VALUES ('Jayson Tatum', 0, 'Boston Celtics', 'USA', '1998-03-03', 'https://example.com/tatum.jpg', true);

INSERT INTO players (name, jersey_number, team, country_of_origin, birth_date, photo_url, active) 
VALUES ('Damian Lillard', 0, 'Milwaukee Bucks', 'USA', '1990-07-15', 'https://example.com/lillard.jpg', true);

INSERT INTO players (name, jersey_number, team, country_of_origin, birth_date, photo_url, active) 
VALUES ('Kawhi Leonard', 2, 'Los Angeles Clippers', 'USA', '1991-06-29', 'https://example.com/kawhi.jpg', true);

INSERT INTO players (name, jersey_number, team, country_of_origin, birth_date, photo_url, active) 
VALUES ('Nikola Jokic', 15, 'Denver Nuggets', 'Serbia', '1995-02-19', 'https://example.com/jokic.jpg', true);

INSERT INTO players (name, jersey_number, team, country_of_origin, birth_date, photo_url, active) 
VALUES ('Joel Embiid', 21, 'Philadelphia 76ers', 'Cameroon', '1994-03-16', 'https://example.com/embiid.jpg', true);

INSERT INTO players (name, jersey_number, team, country_of_origin, birth_date, photo_url, active) 
VALUES ('Kyrie Irving', 11, 'Dallas Mavericks', 'USA', '1992-03-23', 'https://example.com/kyrie.jpg', false);

INSERT INTO players (name, jersey_number, team, country_of_origin, birth_date, photo_url, active) 
VALUES ('Chris Paul', 3, 'Golden State Warriors', 'USA', '1985-05-06', 'https://example.com/chris_paul.jpg', false);

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS=1;
