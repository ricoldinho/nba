package edu.mvc.nba.model;

import edu.mvc.nba.model.vo.Position;
import edu.mvc.nba.model.vo.BodyMeasurements;
import edu.mvc.nba.model.vo.SeasonStatistics;
import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Objective: Represents a Basketball Player in the NBA.
 *
 * Input: Player data from Controller.
 * Output: Persisted Player entity with all details.
 */
@Entity
@Table(name = "players")
public class Player {
    
    // === IDENTITY ===
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // === BASIC DATA ===
    @Column(nullable = false)
    private String name;
    
    @Column(name = "jersey_number")
    private Integer jerseyNumber;
    
    @Column(nullable = false)
    private String team;
    
    @Column(name = "country_of_origin")
    private String countryOfOrigin;
    
    @Column(name = "birth_date")
    private LocalDate birthDate;
    
    // === VALUE OBJECTS ===
    @Embedded
    private Position position;
    
    @Embedded
    private BodyMeasurements bodyMeasurements;
    
    @Embedded
    private SeasonStatistics seasonStatistics;
    
    // === METADATA ===
    @Column(name = "photo_url")
    private String photoUrl;
    
    @Column(name = "active")
    private Boolean active = true;
    
    // Empty constructor (required by JPA)
    public Player() {}
    
    // Constructor with basic data
    public Player(String name, Integer jerseyNumber, String team) {
        this.name = name;
        this.jerseyNumber = jerseyNumber;
        this.team = team;
    }
    
    // Getters and Setters
    public Long getId() { 
        return id; 
    }
    
    public void setId(Long id) { 
        this.id = id; 
    }
    
    public String getName() { 
        return name; 
    }
    
    public void setName(String name) { 
        this.name = name; 
    }
    
    public Integer getJerseyNumber() { 
        return jerseyNumber; 
    }
    
    public void setJerseyNumber(Integer jerseyNumber) { 
        this.jerseyNumber = jerseyNumber; 
    }
    
    public String getTeam() { 
        return team; 
    }
    
    public void setTeam(String team) { 
        this.team = team; 
    }
    
    public String getCountryOfOrigin() { 
        return countryOfOrigin; 
    }
    
    public void setCountryOfOrigin(String countryOfOrigin) { 
        this.countryOfOrigin = countryOfOrigin; 
    }
    
    public LocalDate getBirthDate() { 
        return birthDate; 
    }
    
    public void setBirthDate(LocalDate birthDate) { 
        this.birthDate = birthDate; 
    }
    
    public Position getPosition() { 
        return position; 
    }
    
    public void setPosition(Position position) { 
        this.position = position; 
    }
    
    public BodyMeasurements getBodyMeasurements() { 
        return bodyMeasurements; 
    }
    
    public void setBodyMeasurements(BodyMeasurements bodyMeasurements) { 
        this.bodyMeasurements = bodyMeasurements; 
    }
    
    public SeasonStatistics getSeasonStatistics() { 
        return seasonStatistics; 
    }
    
    public void setSeasonStatistics(SeasonStatistics seasonStatistics) { 
        this.seasonStatistics = seasonStatistics; 
    }
    
    public String getPhotoUrl() { 
        return photoUrl; 
    }
    
    public void setPhotoUrl(String photoUrl) { 
        this.photoUrl = photoUrl; 
    }
    
    public Boolean getActive() { 
        return active; 
    }
    
    public void setActive(Boolean active) { 
        this.active = active; 
    }
}
