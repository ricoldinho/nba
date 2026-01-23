package edu.mvc.nba.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Objective: Represents an NBA Team.
 *
 * Input: Team data from Controller.
 * Output: Persisted Team entity with associated players.
 */
@Entity
@Table(name = "teams")
public class Team {
    
    // === IDENTITY ===
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // === BASIC DATA ===
    @Column(nullable = false, unique = true)
    private String name;
    
    @Column(name = "city")
    private String city;
    
    @Column(name = "country")
    private String country;
    
    @Column(name = "founding_year")
    private Integer foundingYear;
    
    // === RELATIONSHIPS ===
    // @OneToMany: Establishes a one-to-many relationship where one Team can have many Players.
    // mappedBy = "team": Indicates that the relationship is managed by the 'team' field in the Player class.
    // cascade = CascadeType.ALL: Propagates all persistence operations (save, delete, etc.) to associated players.
    // orphanRemoval = true: Automatically deletes players when they are removed from the team's player list.
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Player> players = new ArrayList<>();
    
    // === METADATA ===
    @Column(name = "active")
    private Boolean active = true;
    
    // Empty constructor (required by JPA)
    public Team() {}
    
    // Constructor with basic data
    public Team(String name, String city, String country) {
        this.name = name;
        this.city = city;
        this.country = country;
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
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public Integer getFoundingYear() {
        return foundingYear;
    }
    
    public void setFoundingYear(Integer foundingYear) {
        this.foundingYear = foundingYear;
    }
    
    public List<Player> getPlayers() {
        return players;
    }
    
    public void setPlayers(List<Player> players) {
        this.players = players;
    }
    
    public Boolean getActive() {
        return active;
    }
    
    public void setActive(Boolean active) {
        this.active = active;
    }
}
