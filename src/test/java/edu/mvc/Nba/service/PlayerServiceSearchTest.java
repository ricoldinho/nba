package edu.mvc.nba.service;

import edu.mvc.nba.model.Player;
import edu.mvc.nba.repository.PlayerRepository;

import org.junit.Test;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Objective: Test the player search functionality using partial name matching.
 *
 * Input: Search terms and player collections.
 * Output: Filtered player lists based on search criteria.
 */
public class PlayerServiceSearchTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    /**
     * Objective: Initialize Mockito annotations before each test.
     *
     * Input: (None)
     * Output: (None)
     */
    @Before
    public void setUp() {
        // MockitoAnnotations.openMocks: Initializes Mockito annotations for this test class.
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Objective: Verify that search returns matching players by partial name match.
     *
     * Input: searchName = "James"
     * Output: List<Player> containing all players with "James" in their name.
     */
    @Test
    public void shouldReturnMatchingPlayers_WhenSearchNameExists() {
        // Arrange
        String searchName = "James";
        Player player1 = new Player();
        player1.setId(1L);
        player1.setName("LeBron James");
        
        Player player2 = new Player();
        player2.setId(2L);
        player2.setName("Lebron James Jr");
        
        List<Player> expectedPlayers = Arrays.asList(player1, player2);
        when(playerRepository.findByNameContainingIgnoreCase(searchName))
            .thenReturn(expectedPlayers);

        // Act
        List<Player> result = playerService.searchPlayersByName(searchName);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("LeBron James", result.get(0).getName());
        verify(playerRepository).findByNameContainingIgnoreCase(searchName);
    }

    /**
     * Objective: Verify that search returns all players when search term is null or empty.
     *
     * Input: searchName = null or ""
     * Output: List<Player> containing all players in the database.
     */
    @Test
    public void shouldReturnAllPlayers_WhenSearchNameIsEmpty() {
        // Arrange
        String searchName = "";
        Player player1 = new Player();
        player1.setId(1L);
        player1.setName("LeBron James");
        
        Player player2 = new Player();
        player2.setId(2L);
        player2.setName("Michael Jordan");
        
        List<Player> expectedPlayers = Arrays.asList(player1, player2);
        when(playerRepository.findAll()).thenReturn(expectedPlayers);

        // Act
        List<Player> result = playerService.searchPlayersByName(searchName);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(playerRepository).findAll();
        verify(playerRepository, never()).findByNameContainingIgnoreCase(anyString());
    }

    /**
     * Objective: Verify that search returns empty list when no players match the search term.
     *
     * Input: searchName = "NonexistentPlayer"
     * Output: Empty List<Player>.
     */
    @Test
    public void shouldReturnEmptyList_WhenSearchNameDoesNotMatch() {
        // Arrange
        String searchName = "NonexistentPlayer";
        when(playerRepository.findByNameContainingIgnoreCase(searchName))
            .thenReturn(Collections.emptyList());

        // Act
        List<Player> result = playerService.searchPlayersByName(searchName);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(playerRepository).findByNameContainingIgnoreCase(searchName);
    }

    /**
     * Objective: Verify that search is case-insensitive and finds players regardless of case.
     *
     * Input: searchName = "lebron" (lowercase)
     * Output: List<Player> containing "LeBron James" (proper case).
     */
    @Test
    public void shouldReturnPlayers_WhenSearchNameIgnoresCaseSensitivity() {
        // Arrange
        String searchName = "lebron";
        Player player1 = new Player();
        player1.setId(1L);
        player1.setName("LeBron James");
        
        List<Player> expectedPlayers = Collections.singletonList(player1);
        when(playerRepository.findByNameContainingIgnoreCase(searchName))
            .thenReturn(expectedPlayers);

        // Act
        List<Player> result = playerService.searchPlayersByName(searchName);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("LeBron James", result.get(0).getName());
        verify(playerRepository).findByNameContainingIgnoreCase(searchName);
    }

    /**
     * Objective: Verify that search trims whitespace from search term before querying.
     *
     * Input: searchName = "  James  " (with leading/trailing spaces)
     * Output: List<Player> with trimmed search term applied.
     */
    @Test
    public void shouldTrimSearchName_BeforeQuerying() {
        // Arrange
        String searchName = "  James  ";
        Player player1 = new Player();
        player1.setId(1L);
        player1.setName("LeBron James");
        
        List<Player> expectedPlayers = Collections.singletonList(player1);
        when(playerRepository.findByNameContainingIgnoreCase("James"))
            .thenReturn(expectedPlayers);

        // Act
        List<Player> result = playerService.searchPlayersByName(searchName);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        // Verify that the repository was called with trimmed value
        verify(playerRepository).findByNameContainingIgnoreCase("James");
    }
}
