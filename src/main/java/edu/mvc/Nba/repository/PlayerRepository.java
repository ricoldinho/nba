package edu.mvc.nba.repository;

import edu.mvc.nba.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Objective: Provides database access for Player entity operations.
 *
 * Input: Player objects and query parameters.
 * Output: Player objects retrieved from the database.
 */
// @Repository: Indicates this interface is a Spring Data JPA repository for persistence operations.
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    /**
     * Objective: Find a player by their name.
     *
     * Input: name - The player's full name.
     * Output: Optional<Player> - Contains the player if found.
     */
    Optional<Player> findByName(String name);

    /**
     * Objective: Find all players belonging to a specific team.
     *
     * Input: team - The team name.
     * Output: List<Player> - All players in that team.
     */
    List<Player> findByTeam(String team);

    /**
     * Objective: Find all active players in the database.
     *
     * Input: active - Boolean flag (true for active players).
     * Output: List<Player> - All players with active status matching the parameter.
     */
    List<Player> findByActive(Boolean active);

    /**
     * Objective: Find all players from a specific country.
     *
     * Input: countryOfOrigin - The player's country of origin.
     * Output: List<Player> - All players from that country.
     */
    List<Player> findByCountryOfOrigin(String countryOfOrigin);

    /**
     * Objective: Find a player by their jersey number on a specific team.
     *
     * Input: jerseyNumber - The player's jersey number.
     *        team - The team name.
     * Output: Optional<Player> - The player if found.
     */
    Optional<Player> findByJerseyNumberAndTeam(Integer jerseyNumber, String team);

    /**
     * Objective: Find all active players from a specific team.
     *
     * Input: team - The team name.
     *        active - Boolean flag (true for active players).
     * Output: List<Player> - All active players in that team.
     */
    List<Player> findByTeamAndActive(String team, Boolean active);

    /**
     * Objective: Count all active players in a specific team.
     *
     * Input: team - The team name.
     * Output: Long - The total count of active players in that team.
     */
    Long countByTeamAndActive(String team, Boolean active);

    /**
     * Objective: Count all players in a specific team.
     *
     * Input: team - The team name.
     * Output: Long - The total count of players in that team.
     */
    Long countByTeam(String team);

    /**
     * Objective: Find all active players in the database, using a custom JPQL query.
     *
     * Input: active - Boolean flag (true for active players).
     * Output: List<Player> - All active players.
     */
    // @Query: Executes a custom JPQL (Java Persistence Query Language) query for complex database access patterns.
    @Query("SELECT p FROM Player p WHERE p.active = :active ORDER BY p.team, p.name")
    List<Player> findAllActivePlayers(@Param("active") Boolean active);

    /**
     * Objective: Find all players in a team, sorted by jersey number.
     *
     * Input: team - The team name.
     * Output: List<Player> - All players in that team, sorted by jersey number.
     */
    // @Query: Custom query to retrieve players by team, ordered by jersey number.
    @Query("SELECT p FROM Player p WHERE p.team = :team ORDER BY p.jerseyNumber")
    List<Player> findByTeamOrderByJerseyNumber(@Param("team") String team);
}
