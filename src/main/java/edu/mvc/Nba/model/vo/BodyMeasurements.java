package edu.mvc.nba.model.vo;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;

/**
 * Objective: Encapsulates the physical measurements of a player.
 *
 * Input: Height in cm and weight in kg.
 * Output: Measurements and derived calculations (BMI).
 */
@Embeddable
public class BodyMeasurements {
    
    @Column(name = "height_cm")
    private Double heightCm;
    
    @Column(name = "weight_kg")
    private Double weightKg;
    
    // Empty constructor
    public BodyMeasurements() {}
    
    public BodyMeasurements(Double heightCm, Double weightKg) {
        this.heightCm = heightCm;
        this.weightKg = weightKg;
    }
    
    /**
     * Objective: Calculates the Body Mass Index (BMI) of the player.
     *
     * Input: None (uses internal height and weight).
     * Output: Double - Player's BMI value.
     */
    public Double getBMI() {
        if (heightCm == null || weightKg == null || heightCm == 0) {
            return null;
        }
        double heightMeters = heightCm / 100.0;
        return weightKg / (heightMeters * heightMeters);
    }
    
    // Getters and Setters
    public Double getHeightCm() { 
        return heightCm; 
    }
    
    public void setHeightCm(Double heightCm) { 
        this.heightCm = heightCm; 
    }
    
    public Double getWeightKg() { 
        return weightKg; 
    }
    
    public void setWeightKg(Double weightKg) { 
        this.weightKg = weightKg; 
    }
}
