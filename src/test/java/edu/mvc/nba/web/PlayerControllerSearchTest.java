package edu.mvc.nba.web;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import edu.mvc.nba.model.Player;
import edu.mvc.nba.service.PlayerService;
import edu.mvc.nba.service.TeamService;
import edu.mvc.nba.web.PlayerController;

/**
 * Objective: Test the player search functionality in the Controller layer.
 *
 * Input: HTTP GET requests with optional searchName parameter.
 * Output: HTTP response with filtered player list and rendered view.
 */
public class PlayerControllerSearchTest {

    private MockMvc mockMvc;

    @Mock
    private PlayerService playerService;

    @Mock
    private TeamService teamService;

    private PlayerController playerController;

    /**
     * Objective: Initialize the test setup before each test method.
     *
     * Input: Mocked PlayerService
     * Output: MockMvc configured with PlayerController
     */
    @Before
    public void setUp() {
        // MockitoAnnotations.openMocks: Initializes Mockito annotations for this test
        // class.
        MockitoAnnotations.openMocks(this);
        playerController = new PlayerController(playerService, teamService);
        mockMvc = MockMvcBuilders.standaloneSetup(playerController).build();
    }

    /**
     * Objective: Verify that GET /players returns all players when no search
     * parameter is provided.
     *
     * Input: GET /players (no searchName parameter)
     * Output: HTTP 200 status, view name "players/index", all players in model.
     */
    @Test
    public void shouldReturnAllPlayers_WhenNoSearchParameterProvided() throws Exception {
        // Arrange
        Player player1 = new Player();
        player1.setId(1L);
        player1.setName("LeBron James");

        Player player2 = new Player();
        player2.setId(2L);
        player2.setName("Michael Jordan");

        List<Player> allPlayers = Arrays.asList(player1, player2);
        when(playerService.getAllPlayers()).thenReturn(allPlayers);

        // Act & Assert
        mockMvc.perform(get("/players"))
                .andExpect(status().isOk())
                .andExpect(view().name("players/index"))
                .andExpect(model().attributeExists("players"))
                .andExpect(model().attribute("players", allPlayers));

        verify(playerService).getAllPlayers();
        verify(playerService, never()).searchPlayersByName(anyString());
    }

    /**
     * Objective: Verify that GET /players?searchName=term returns filtered players.
     *
     * Input: GET /players?searchName=James
     * Output: HTTP 200 status, filtered players in model, search term preserved.
     */
    @Test
    public void shouldReturnFilteredPlayers_WhenSearchParameterProvided() throws Exception {
        // Arrange
        String searchName = "James";
        Player player1 = new Player();
        player1.setId(1L);
        player1.setName("LeBron James");

        List<Player> filteredPlayers = Collections.singletonList(player1);
        when(playerService.searchPlayersByName(searchName)).thenReturn(filteredPlayers);

        // Act & Assert
        mockMvc.perform(get("/players").param("searchName", searchName))
                .andExpect(status().isOk())
                .andExpect(view().name("players/index"))
                .andExpect(model().attributeExists("players"))
                .andExpect(model().attribute("players", filteredPlayers))
                .andExpect(model().attribute("searchName", searchName));

        verify(playerService).searchPlayersByName(searchName);
    }

    /**
     * Objective: Verify that search is case-insensitive by searching with
     * lowercase.
     *
     * Input: GET /players?searchName=lebron
     * Output: HTTP 200 status, players matching "lebron" (case-insensitive).
     */
    @Test
    public void shouldReturnPlayers_WhenSearchParameterIsLowercase() throws Exception {
        // Arrange
        String searchName = "lebron";
        Player player1 = new Player();
        player1.setId(1L);
        player1.setName("LeBron James");

        List<Player> filteredPlayers = Collections.singletonList(player1);
        when(playerService.searchPlayersByName(searchName)).thenReturn(filteredPlayers);

        // Act & Assert
        mockMvc.perform(get("/players").param("searchName", searchName))
                .andExpect(status().isOk())
                .andExpect(view().name("players/index"))
                .andExpect(model().attribute("players", filteredPlayers));

        verify(playerService).searchPlayersByName(searchName);
    }

    /**
     * Objective: Verify that empty search parameter returns all players.
     *
     * Input: GET /players?searchName= (empty string)
     * Output: HTTP 200 status, all players in model (no filter applied).
     */
    @Test
    public void shouldReturnAllPlayers_WhenSearchParameterIsEmpty() throws Exception {
        // Arrange
        Player player1 = new Player();
        player1.setId(1L);
        player1.setName("LeBron James");

        Player player2 = new Player();
        player2.setId(2L);
        player2.setName("Michael Jordan");

        List<Player> allPlayers = Arrays.asList(player1, player2);
        when(playerService.getAllPlayers()).thenReturn(allPlayers);

        // Act & Assert
        mockMvc.perform(get("/players").param("searchName", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("players/index"))
                .andExpect(model().attribute("players", allPlayers));

        verify(playerService).getAllPlayers();
        verify(playerService, never()).searchPlayersByName(anyString());
    }

    /**
     * Objective: Verify that search returns empty list when no players match.
     *
     * Input: GET /players?searchName=NonexistentPlayer
     * Output: HTTP 200 status, empty players list.
     */
    @Test
    public void shouldReturnEmptyList_WhenSearchTermHasNoMatches() throws Exception {
        // Arrange
        String searchName = "NonexistentPlayer";
        when(playerService.searchPlayersByName(searchName)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/players").param("searchName", searchName))
                .andExpect(status().isOk())
                .andExpect(view().name("players/index"))
                .andExpect(model().attribute("players", Collections.emptyList()))
                .andExpect(model().attribute("searchName", searchName));

        verify(playerService).searchPlayersByName(searchName);
    }

    /**
     * Objective: Verify that search parameter with spaces is trimmed properly.
     *
     * Input: GET /players?searchName=%20%20James%20%20 (spaces)
     * Output: HTTP 200 status, filtered players using trimmed search term.
     */
    @Test
    public void shouldHandleSearchTermWithSpaces() throws Exception {
        // Arrange
        String searchName = "  James  ";
        Player player1 = new Player();
        player1.setId(1L);
        player1.setName("LeBron James");

        List<Player> filteredPlayers = Collections.singletonList(player1);
        when(playerService.searchPlayersByName(searchName)).thenReturn(filteredPlayers);

        // Act & Assert
        mockMvc.perform(get("/players").param("searchName", searchName))
                .andExpect(status().isOk())
                .andExpect(view().name("players/index"))
                .andExpect(model().attribute("players", filteredPlayers));

        verify(playerService).searchPlayersByName(searchName);
    }
}
