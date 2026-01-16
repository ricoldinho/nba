package edu.mvc.nba.model.vo;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
/**
 * Objective: Represents the playing position of an NBA player.
 *
 * Input: Player position from Controller.
 * Output: Position type with code and description.
 */
@Embeddable
public class Position {
    
    @Enumerated(EnumType.STRING)
    private PositionType type;
    
    public enum PositionType {
        POINT_GUARD("PG", "Point Guard"),
        SHOOTING_GUARD("SG", "Shooting Guard"),
        SMALL_FORWARD("SF", "Small Forward"),
        POWER_FORWARD("PF", "Power Forward"),
        CENTER("C", "Center");
        
        private String code;
        private String description;
        
        PositionType(String code, String description) {
            this.code = code;
            this.description = description;
        }
        
        public String getCode() { 
            return code; 
        }
        
        public String getDescription() { 
            return description; 
        }
    }
    
    // Empty constructor (required by JPA)
    public Position() {}
    
    public Position(PositionType type) {
        this.type = type;
    }
    
    public PositionType getType() { 
        return type; 
    }
    
    public void setType(PositionType type) { 
        this.type = type; 
    }
}
