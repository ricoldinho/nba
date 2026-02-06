-- NBA Players Sample Data
-- This file is automatically loaded by Spring Boot on application startup

-- Disable foreign key checks for data loading
SET FOREIGN_KEY_CHECKS=0;

-- ============================================
-- INSERT TEAMS
-- ============================================
INSERT INTO teams (id, active, city, country, founding_year, name) VALUES
(1, 1, 'Los Angeles', 'USA', 1972, 'Los Angeles Lakers'),
(2, 1, 'Golden State', 'USA', 1946, 'Golden State Warriors'),
(3, 1, 'Denver', 'USA', 1976, 'Denver Nuggets'),
(4, 1, 'Dallas', 'USA', 1980, 'Dallas Mavericks'),
(5, 1, 'Milwaukee', 'USA', 1968, 'Milwaukee Bucks'),
(6, 1, 'Boston', 'USA', 1957, 'Boston Celtics'),
(7, 1, 'Phoenix', 'USA', 1968, 'Phoenix Suns'),
(8, 1, 'Philadelphia', 'USA', 1963, 'Philadelphia 76ers'),
(9, 1, 'Los Angeles', 'USA', 1984, 'LA Clippers'),
(10, 1, 'San Antonio', 'USA', 1973, 'San Antonio Spurs'),
(11, 1, 'Atlanta', 'USA', 1957, 'Atlanta Hawks'),
(12, 1, 'Oklahoma City', 'USA', 1967, 'Oklahoma City Thunder'),
(13, 1, 'Cleveland', 'USA', 1970, 'Cleveland Cavaliers'),
(14, 1, 'Indianapolis', 'USA', 1967, 'Indiana Pacers'),
(15, 1, 'Minneapolis', 'USA', 1989, 'Minnesota Timberwolves');

-- ============================================
-- INSERT SAMPLE PLAYERS
-- ============================================
INSERT INTO players (
    active, assists_per_game, birth_date, field_goal_percentage, 
    height_cm, jersey_number, points_per_game, rebounds_per_game, 
    weight_kg, country_of_origin, name, photo_url, team_id, type
) VALUES 
(1, 7.3, '1984-12-30', 50.5, 206, 23, 27.1, 7.5, 113, 'USA', 'LeBron James', 'https://ak-static.cms.nba.com/wp-content/uploads/headshots/nba/latest/260x190/2544.png', 1, 'SMALL_FORWARD'),
(1, 5.0, '1988-03-14', 47.3, 188, 30, 26.4, 4.7, 84, 'USA', 'Stephen Curry', 'https://ak-static.cms.nba.com/wp-content/uploads/headshots/nba/latest/260x190/201939.png', 2, 'POINT_GUARD'),
(1, 9.0, '1995-02-19', 55.3, 211, 15, 20.9, 10.7, 129, 'Serbia', 'Nikola Jokic', 'https://ak-static.cms.nba.com/wp-content/uploads/headshots/nba/latest/260x190/203999.png', 3, 'CENTER'),
(1, 8.7, '1999-02-28', 46.6, 201, 77, 28.7, 8.7, 104, 'Slovenia', 'Luka Doncic', 'https://ak-static.cms.nba.com/wp-content/uploads/headshots/nba/latest/260x190/1629029.png', 4, 'POINT_GUARD'),
(1, 4.1, '1994-12-06', 54.5, 211, 34, 23.3, 9.6, 110, 'Greece', 'Giannis Antetokounmpo', 'https://ak-static.cms.nba.com/wp-content/uploads/headshots/nba/latest/260x190/203507.png', 5, 'POWER_FORWARD'),
(1, 3.4, '1998-03-03', 46.0, 203, 0, 23.1, 7.1, 95, 'USA', 'Jayson Tatum', 'https://ak-static.cms.nba.com/wp-content/uploads/headshots/nba/latest/260x190/1628369.png', 6, 'SMALL_FORWARD'),
(1, 5.0, '1988-09-29', 49.9, 211, 35, 27.3, 7.0, 109, 'USA', 'Kevin Durant', 'https://ak-static.cms.nba.com/wp-content/uploads/headshots/nba/latest/260x190/201142.png', 7, 'POWER_FORWARD'),
(1, 4.3, '1994-03-16', 50.4, 213, 21, 27.2, 11.2, 127, 'Cameroon', 'Joel Embiid', 'https://ak-static.cms.nba.com/wp-content/uploads/headshots/nba/latest/260x190/203954.png', 8, 'CENTER'),
(1, 3.7, '1996-10-30', 49.1, 196, 1, 24.8, 4.8, 97, 'USA', 'Devin Booker', 'https://ak-static.cms.nba.com/wp-content/uploads/headshots/nba/latest/260x190/1626164.png', 7, 'SHOOTING_GUARD'),
(1, 1.3, '2004-01-04', 46.5, 224, 1, 21.4, 10.6, 95, 'France', 'Victor Wembanyama', 'https://ak-static.cms.nba.com/wp-content/uploads/headshots/nba/latest/260x190/1641705.png', 10, 'CENTER'),
(1, 6.7, '1989-08-26', 44.4, 196, 1, 24.3, 4.2, 100, 'USA', 'James Harden', 'https://ak-static.cms.nba.com/wp-content/uploads/headshots/nba/latest/260x190/201935.png', 9, 'SHOOTING_GUARD'),
(1, 5.2, '1992-03-23', 48.5, 191, 2, 25.2, 5.5, 87, 'USA', 'Kyrie Irving', 'https://ak-static.cms.nba.com/wp-content/uploads/headshots/nba/latest/260x190/202681.png', 4, 'POINT_GUARD'),
(1, 2.3, '1993-03-11', 51.9, 208, 23, 24.1, 10.6, 115, 'USA', 'Anthony Davis', 'https://ak-static.cms.nba.com/wp-content/uploads/headshots/nba/latest/260x190/203076.png', 1, 'CENTER'),
(1, 6.2, '1998-07-12', 43.8, 185, 11, 24.3, 3.8, 82, 'USA', 'Trae Young', 'https://ak-static.cms.nba.com/wp-content/uploads/headshots/nba/latest/260x190/1629027.png', 11, 'POINT_GUARD'),
(1, 3.9, '1991-06-29', 49.1, 201, 2, 23.9, 6.4, 102, 'USA', 'Kawhi Leonard', 'https://ak-static.cms.nba.com/wp-content/uploads/headshots/nba/latest/260x190/202695.png', 9, 'SMALL_FORWARD'),
(1, 6.7, '1990-07-15', 43.8, 188, 0, 25.1, 4.2, 88, 'USA', 'Damian Lillard', 'https://ak-static.cms.nba.com/wp-content/uploads/headshots/nba/latest/260x190/203081.png', 5, 'POINT_GUARD'),
(1, 6.2, '1998-07-12', 53.0, 198, 2, 30.1, 5.5, 88, 'Canada', 'Shai Gilgeous-Alexander', 'https://ak-static.cms.nba.com/wp-content/uploads/headshots/nba/latest/260x190/1628983.png', 12, 'POINT_GUARD'),
(1, 1.9, '1996-12-19', 46.1, 185, 45, 24.8, 4.3, 98, 'USA', 'Donovan Mitchell', 'https://ak-static.cms.nba.com/wp-content/uploads/headshots/nba/latest/260x190/1628378.png', 13, 'SHOOTING_GUARD'),
(1, 8.1, '2000-02-29', 45.4, 196, 0, 19.3, 6.6, 95, 'USA', 'Tyrese Haliburton', 'https://ak-static.cms.nba.com/wp-content/uploads/headshots/nba/latest/260x190/1630169.png', 14, 'POINT_GUARD'),
(1, 4.1, '2001-08-05', 47.5, 193, 5, 25.9, 5.4, 102, 'USA', 'Anthony Edwards', 'https://ak-static.cms.nba.com/wp-content/uploads/headshots/nba/latest/260x190/1630162.png', 15, 'SHOOTING_GUARD');


-- Insert sample users
-- Passwords are encrypted using BCrypt
-- admin: Admin@123
-- user1: User@123
-- player1: Player@123
-- manager: Manager@123
-- coach: Coach@123
INSERT INTO users (username, password, role) VALUES
('admin', '$2a$10$0hu4CONr.xZL3tItqMs5AuVhLyHVxZQvOabG9C/pYq6SAlC3mwgE.', 'ADMIN'),
('user1', '$2a$10$Fi9kL62C5R/j56X7DRdSK.fyaNptgMvzJgTAY8j.MT6dXNV3jonjK', 'USER'),
('player1', '$2a$10$AEAf0GSCNBIbgkax8A9qxu9rv7KuDMySp/TgvUSG0e2psh26uNqI.', 'USER'),
('manager', '$2a$10$HCtOEXXFEJsdxIQl1vlDz.wZt67AEAKoOKJY.P16zGH4XNCQFGpDG', 'USER'),
('coach', '$2a$10$.3PsOUM3GdiH1tNUR.j05.9jyC2kB9soDQqyLFzag93tBqY2h7l/6', 'USER');

-- ============================================
-- RE-ENABLE FOREIGN KEY CHECKS
-- ============================================
SET FOREIGN_KEY_CHECKS=1;