package edu.mvc.nba.model.vo;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;

/**
 * Objective: Groups the main statistics of a player in a season.
 *
 * Input: Points, assists, and rebounds per game.
 * Output: Complete statistics and derived calculations.
 */
@Embeddable
public class SeasonStatistics {
    
    @Column(name = "points_per_game")
    private Double pointsPerGame;
    
    @Column(name = "assists_per_game")
    private Double assistsPerGame;
    
    @Column(name = "rebounds_per_game")
    private Double reboundsPerGame;
    
    @Column(name = "field_goal_percentage")
    private Double fieldGoalPercentage; // FG%
    
    // Empty constructor
    public SeasonStatistics() {}
    
    public SeasonStatistics(Double pointsPerGame, Double assistsPerGame, Double reboundsPerGame) {
        this.pointsPerGame = pointsPerGame;
        this.assistsPerGame = assistsPerGame;
        this.reboundsPerGame = reboundsPerGame;
    }
    
    /**
     * Objective: Calculates a simple efficiency index for the player.
     *
     * Input: None (uses internal statistics).
     * Output: Double - Efficiency value.
     */
    public Double getEfficiency() {
        if (pointsPerGame == null || assistsPerGame == null || reboundsPerGame == null) {
            return 0.0;
        }
        return (pointsPerGame * 1.3) + (assistsPerGame * 1.5) + (reboundsPerGame * 1.2);
    }
    
    // Getters and Setters
    public Double getPointsPerGame() { 
        return pointsPerGame; 
    }
    
    public void setPointsPerGame(Double pointsPerGame) { 
        this.pointsPerGame = pointsPerGame; 
    }
    
    public Double getAssistsPerGame() { 
        return assistsPerGame; 
    }
    
    public void setAssistsPerGame(Double assistsPerGame) { 
        this.assistsPerGame = assistsPerGame; 
    }
    
    public Double getReboundsPerGame() { 
        return reboundsPerGame; 
    }
    
    public void setReboundsPerGame(Double reboundsPerGame) { 
        this.reboundsPerGame = reboundsPerGame; 
    }
    
    public Double getFieldGoalPercentage() { 
        return fieldGoalPercentage; 
    }
    
    public void setFieldGoalPercentage(Double fieldGoalPercentage) { 
        this.fieldGoalPercentage = fieldGoalPercentage; 
    }
}
