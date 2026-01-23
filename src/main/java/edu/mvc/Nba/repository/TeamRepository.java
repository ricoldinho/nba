package edu.mvc.nba.repository;

import edu.mvc.nba.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Objective: Provides database access for Team entity operations.
 *
 * Input: Team objects and query parameters.
 * Output: Team objects retrieved from the database.
 */
// @Repository: Indicates this interface is a Spring Data JPA repository for persistence operations.
@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    /**
     * Objective: Find a team by their name.
     *
     * Input: name - The team's name.
     * Output: Optional<Team> - Contains the team if found.
     */
    Optional<Team> findByName(String name);

    /**
     * Objective: Find all active teams in the database.
     *
     * Input: active - Boolean flag (true for active teams).
     * Output: List<Team> - All teams with active status matching the parameter.
     */
    List<Team> findByActive(Boolean active);

    /**
     * Objective: Find all teams from a specific city.
     *
     * Input: city - The city name.
     * Output: List<Team> - All teams located in that city.
     */
    List<Team> findByCity(String city);

    /**
     * Objective: Find all teams from a specific country.
     *
     * Input: country - The country name.
     * Output: List<Team> - All teams from that country.
     */
    List<Team> findByCountry(String country);

    /**
     * Objective: Find all active teams, sorted by name.
     *
     * Input: active - Boolean flag (true for active teams).
     * Output: List<Team> - All active teams, sorted alphabetically by name.
     */
    // @Query: Executes a custom JPQL query to retrieve active teams sorted by name.
    @Query("SELECT t FROM Team t WHERE t.active = :active ORDER BY t.name")
    List<Team> findAllActiveTeams(@Param("active") Boolean active);
}
