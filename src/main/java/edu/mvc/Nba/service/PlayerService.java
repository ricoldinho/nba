package edu.mvc.nba.service;

import edu.mvc.nba.model.Player;
import edu.mvc.nba.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Objective: Orchestrates business logic for Player entity operations.
 *
 * Input: Player data from controllers and repositories.
 * Output: Processed Player data and query results.
 */
// @Service: Marks this class as a Service component in the Spring context to hold business logic.
@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    /**
     * Objective: Retrieves a single player by ID from the database.
     *
     * Input: id - The unique player identifier.
     * Output: Optional<Player> - Contains the player if found, empty otherwise.
     */
    @Transactional(readOnly = true)
    public Optional<Player> getPlayerById(Long id) {
        return playerRepository.findById(id);
    }

    /**
     * Objective: Retrieves all players from the database.
     *
     * Input: (None)
     * Output: List<Player> - All players in the database.
     */
    // @Transactional(readOnly = true): Optimizes read-only operations by preventing unnecessary write transaction overhead.
    @Transactional(readOnly = true)
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    /**
     * Objective: Retrieves all active players in the database.
     *
     * Input: (None)
     * Output: List<Player> - All active players, sorted by team and name.
     */
    @Transactional(readOnly = true)
    public List<Player> getAllActivePlayers() {
        return playerRepository.findAllActivePlayers(true);
    }

    /**
     * Objective: Retrieves all inactive players in the database.
     *
     * Input: (None)
     * Output: List<Player> - All inactive players.
     */
    @Transactional(readOnly = true)
    public List<Player> getAllInactivePlayers() {
        return playerRepository.findByActive(false);
    }

    /**
     * Objective: Creates a new player and persists it to the database.
     *
     * Input: player - The Player entity to be created.
     * Output: Player - The persisted player with generated ID.
     */
    // @Transactional: Ensures that all database operations in this method succeed or fail as a single atomic unit.
    @Transactional
    public Player createPlayer(Player player) {
        return playerRepository.save(player);
    }

    /**
     * Objective: Updates an existing player's information.
     *
     * Input: id - The player ID to update.
     *        updatedPlayer - The new player data.
     * Output: Optional<Player> - The updated player if found, empty otherwise.
     */
    @Transactional
    public Optional<Player> updatePlayer(Long id, Player updatedPlayer) {
        return playerRepository.findById(id).map(existingPlayer -> {
            existingPlayer.setName(updatedPlayer.getName());
            existingPlayer.setJerseyNumber(updatedPlayer.getJerseyNumber());
            existingPlayer.setTeam(updatedPlayer.getTeam());
            existingPlayer.setCountryOfOrigin(updatedPlayer.getCountryOfOrigin());
            existingPlayer.setBirthDate(updatedPlayer.getBirthDate());
            existingPlayer.setPosition(updatedPlayer.getPosition());
            existingPlayer.setBodyMeasurements(updatedPlayer.getBodyMeasurements());
            existingPlayer.setSeasonStatistics(updatedPlayer.getSeasonStatistics());
            existingPlayer.setPhotoUrl(updatedPlayer.getPhotoUrl());
            existingPlayer.setActive(updatedPlayer.getActive());
            return playerRepository.save(existingPlayer);
        });
    }

    /**
     * Objective: Deletes a player from the database by ID.
     *
     * Input: id - The player ID to delete.
     * Output: Boolean - True if deletion was successful, false if player not found.
     */
    @Transactional
    public Boolean deletePlayer(Long id) {
        if (playerRepository.existsById(id)) {
            playerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Objective: Deactivates a player without removing them from the database.
     *
     * Input: id - The player ID to deactivate.
     * Output: Optional<Player> - The deactivated player if found, empty otherwise.
     */
    @Transactional
    public Optional<Player> deactivatePlayer(Long id) {
        return playerRepository.findById(id).map(player -> {
            player.setActive(false);
            return playerRepository.save(player);
        });
    }

    /**
     * Objective: Reactivates an inactive player.
     *
     * Input: id - The player ID to reactivate.
     * Output: Optional<Player> - The reactivated player if found, empty otherwise.
     */
    @Transactional
    public Optional<Player> reactivatePlayer(Long id) {
        return playerRepository.findById(id).map(player -> {
            player.setActive(true);
            return playerRepository.save(player);
        });
    }

    /**
     * Objective: Retrieves a player by their full name.
     *
     * Input: name - The player's name.
     * Output: Optional<Player> - The player if found.
     */
    @Transactional(readOnly = true)
    public Optional<Player> getPlayerByName(String name) {
        return playerRepository.findByName(name);
    }

    /**
     * Objective: Retrieves all players from a specific team.
     *
     * Input: team - The team name.
     * Output: List<Player> - All players in that team.
     */
    @Transactional(readOnly = true)
    public List<Player> getPlayersByTeam(String team) {
        return playerRepository.findByTeam(team);
    }

    /**
     * Objective: Retrieves all active players from a specific team.
     *
     * Input: team - The team name.
     * Output: List<Player> - All active players in that team.
     */
    @Transactional(readOnly = true)
    public List<Player> getActivePlayersByTeam(String team) {
        return playerRepository.findByTeamAndActive(team, true);
    }

    /**
     * Objective: Retrieves all players from a specific country.
     *
     * Input: countryOfOrigin - The country name.
     * Output: List<Player> - All players from that country.
     */
    @Transactional(readOnly = true)
    public List<Player> getPlayersByCountry(String countryOfOrigin) {
        return playerRepository.findByCountryOfOrigin(countryOfOrigin);
    }

    /**
     * Objective: Finds a player by jersey number and team.
     *
     * Input: jerseyNumber - The player's jersey number.
     *        team - The team name.
     * Output: Optional<Player> - The player if found.
     */
    @Transactional(readOnly = true)
    public Optional<Player> getPlayerByJerseyNumberAndTeam(Integer jerseyNumber, String team) {
        return playerRepository.findByJerseyNumberAndTeam(jerseyNumber, team);
    }

    /**
     * Objective: Retrieves all players in a team, sorted by jersey number.
     *
     * Input: team - The team name.
     * Output: List<Player> - All players in that team, sorted by jersey number.
     */
    @Transactional(readOnly = true)
    public List<Player> getPlayersByTeamSortedByJerseyNumber(String team) {
        return playerRepository.findByTeamOrderByJerseyNumber(team);
    }

    /**
     * Objective: Counts the total number of active players in a specific team.
     *
     * Input: team - The team name.
     * Output: Long - The count of active players in that team.
     */
    @Transactional(readOnly = true)
    public Long countActivePlayersByTeam(String team) {
        return playerRepository.countByTeamAndActive(team, true);
    }

    /**
     * Objective: Counts the total number of players in a specific team.
     *
     * Input: team - The team name.
     * Output: Long - The count of all players in that team.
     */
    @Transactional(readOnly = true)
    public Long countPlayersByTeam(String team) {
        return playerRepository.countByTeam(team);
    }

    /**
     * Objective: Counts the total number of all players in the database.
     *
     * Input: (None)
     * Output: Long - The total count of players.
     */
    @Transactional(readOnly = true)
    public Long countAllPlayers() {
        return playerRepository.count();
    }
}
