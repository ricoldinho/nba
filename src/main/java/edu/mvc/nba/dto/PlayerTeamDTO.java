package edu.mvc.nba.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.time.LocalDate;

/**
 * Objective: DTO to transfer Player and Team data together in a single request.
 *
 * Input: Form data containing both player and team information.
 * Output: DTO object with validated data for service processing.
 */
public class PlayerTeamDTO {

    // === TEAM PROPERTIES ===
    @NotBlank(message = "El nombre del equipo es requerido")
    private String teamName;

    private String teamCity;

    private String teamCountry;

    private Integer teamFoundingYear;

    // === PLAYER PROPERTIES ===
    @NotBlank(message = "El nombre del jugador es requerido")
    private String playerName;

    @Min(value = 0, message = "El número de camiseta debe ser mayor o igual a 0")
    @Max(value = 99, message = "El número de camiseta debe ser menor o igual a 99")
    private Integer jerseyNumber;

    private String countryOfOrigin;

    private LocalDate birthDate;

    private String photoUrl;

    @NotNull(message = "El estado del jugador es requerido")
    private Boolean active = true;

    // === CONSTRUCTORS ===
    public PlayerTeamDTO() {}

    public PlayerTeamDTO(String teamName, String playerName) {
        this.teamName = teamName;
        this.playerName = playerName;
    }

    // === GETTERS AND SETTERS ===
    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamCity() {
        return teamCity;
    }

    public void setTeamCity(String teamCity) {
        this.teamCity = teamCity;
    }

    public String getTeamCountry() {
        return teamCountry;
    }

    public void setTeamCountry(String teamCountry) {
        this.teamCountry = teamCountry;
    }

    public Integer getTeamFoundingYear() {
        return teamFoundingYear;
    }

    public void setTeamFoundingYear(Integer teamFoundingYear) {
        this.teamFoundingYear = teamFoundingYear;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Integer getJerseyNumber() {
        return jerseyNumber;
    }

    public void setJerseyNumber(Integer jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
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
