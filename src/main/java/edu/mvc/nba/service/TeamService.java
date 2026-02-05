package edu.mvc.nba.service;

import edu.mvc.nba.model.Team;
import edu.mvc.nba.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Objective: Orchestrates business logic for Team entity operations.
 *
 * Input: Team data from controllers and repositories.
 * Output: Processed Team data and query results.
 */
// @Service: Marks this class as a Service component in the Spring context to hold business logic.
@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    /**
     * Objective: Retrieves a single team by ID from the database.
     *
     * Input: id - The unique team identifier.
     * Output: Optional<Team> - Contains the team if found, empty otherwise.
     */
    @Transactional(readOnly = true)
    public Optional<Team> getTeamById(Long id) {
        return teamRepository.findById(id);
    }

    /**
     * Objective: Retrieves all teams from the database.
     *
     * Input: (None)
     * Output: List<Team> - All teams in the database.
     */
    @Transactional(readOnly = true)
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    /**
     * Objective: Retrieves all active teams in the database.
     *
     * Input: (None)
     * Output: List<Team> - All active teams, sorted by name.
     */
    @Transactional(readOnly = true)
    public List<Team> getAllActiveTeams() {
        return teamRepository.findAllActiveTeams(true);
    }

    /**
     * Objective: Retrieves all inactive teams in the database.
     *
     * Input: (None)
     * Output: List<Team> - All inactive teams.
     */
    @Transactional(readOnly = true)
    public List<Team> getAllInactiveTeams() {
        return teamRepository.findByActive(false);
    }

    /**
     * Objective: Creates a new team and persists it to the database.
     *
     * Input: team - The Team entity to be created.
     * Output: Team - The persisted team with generated ID.
     */
    @Transactional
    public Team createTeam(Team team) {
        return teamRepository.save(team);
    }

    /**
     * Objective: Updates an existing team's information.
     *
     * Input: id - The team ID to update.
     *        updatedTeam - The new team data.
     * Output: Optional<Team> - The updated team if found, empty otherwise.
     */
    @Transactional
    public Optional<Team> updateTeam(Long id, Team updatedTeam) {
        return teamRepository.findById(id).map(existingTeam -> {
            existingTeam.setName(updatedTeam.getName());
            existingTeam.setCity(updatedTeam.getCity());
            existingTeam.setCountry(updatedTeam.getCountry());
            existingTeam.setFoundingYear(updatedTeam.getFoundingYear());
            existingTeam.setActive(updatedTeam.getActive());
            return teamRepository.save(existingTeam);
        });
    }

    /**
     * Objective: Deletes a team from the database by ID.
     *
     * Input: id - The team ID to delete.
     * Output: Boolean - True if deletion was successful, false if team not found.
     */
    @Transactional
    public Boolean deleteTeam(Long id) {
        if (teamRepository.existsById(id)) {
            teamRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Objective: Deactivates a team without removing it from the database.
     *
     * Input: id - The team ID to deactivate.
     * Output: Optional<Team> - The deactivated team if found, empty otherwise.
     */
    @Transactional
    public Optional<Team> deactivateTeam(Long id) {
        return teamRepository.findById(id).map(team -> {
            team.setActive(false);
            return teamRepository.save(team);
        });
    }

    /**
     * Objective: Reactivates an inactive team.
     *
     * Input: id - The team ID to reactivate.
     * Output: Optional<Team> - The reactivated team if found, empty otherwise.
     */
    @Transactional
    public Optional<Team> reactivateTeam(Long id) {
        return teamRepository.findById(id).map(team -> {
            team.setActive(true);
            return teamRepository.save(team);
        });
    }

    /**
     * Objective: Retrieves a team by their name.
     *
     * Input: name - The team's name.
     * Output: Optional<Team> - The team if found.
     */
    @Transactional(readOnly = true)
    public Optional<Team> getTeamByName(String name) {
        return teamRepository.findByName(name);
    }

    /**
     * Objective: Retrieves all teams from a specific city.
     *
     * Input: city - The city name.
     * Output: List<Team> - All teams in that city.
     */
    @Transactional(readOnly = true)
    public List<Team> getTeamsByCity(String city) {
        return teamRepository.findByCity(city);
    }

    /**
     * Objective: Retrieves all teams from a specific country.
     *
     * Input: country - The country name.
     * Output: List<Team> - All teams from that country.
     */
    @Transactional(readOnly = true)
    public List<Team> getTeamsByCountry(String country) {
        return teamRepository.findByCountry(country);
    }

    /**
     * Objective: Counts the total number of players in a specific team.
     *
     * Input: id - The team ID.
     * Output: Long - The count of players in that team.
     */
    @Transactional(readOnly = true)
    public Long countPlayersByTeam(Long id) {
        return teamRepository.findById(id)
            .map(team -> (long) team.getPlayers().size())
            .orElse(0L);
    }

    /**
     * Objective: Counts the total number of all teams in the database.
     *
     * Input: (None)
     * Output: Long - The total count of teams.
     */
    @Transactional(readOnly = true)
    public Long countAllTeams() {
        return teamRepository.count();
    }
}
