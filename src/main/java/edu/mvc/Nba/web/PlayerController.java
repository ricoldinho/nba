package edu.mvc.nba.web;

import edu.mvc.nba.model.Player;
import edu.mvc.nba.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

/**
 * Objective: Handles HTTP requests related to Player management and renders Thymeleaf views.
 *
 * Input: HTTP requests, form data, path variables.
 * Output: View names and model data for Thymeleaf template rendering.
 */
// @Controller: Indicates that this class handles HTTP requests and returns view names for server-side rendering (not REST).
// @RequestMapping: Sets the base URL path "/players" for all endpoints in this controller.
@Controller
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService playerService;

    // @Autowired: (Implicit) Injects the required PlayerService dependency via constructor injection.
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    /**
     * Objective: Displays a list of all players in the database with optional search filter by name.
     *
     * Input: searchName - Optional search term to filter players by name (case-insensitive partial match).
     * Output: String - The view name "players/index" and Model containing filtered players.
     */
    // @GetMapping: Maps HTTP GET requests to this method for retrieving the player list.
    // @RequestParam: Extracts the 'searchName' query parameter from the request URL with optional empty default value.
    @GetMapping
    public String listPlayers(
        @RequestParam(name = "searchName", required = false, defaultValue = "") String searchName,
        Model model
    ) {
        List<Player> players;
        if (searchName != null && !searchName.trim().isEmpty()) {
            players = playerService.searchPlayersByName(searchName);
            model.addAttribute("searchName", searchName);
        } else {
            players = playerService.getAllPlayers();
        }
        model.addAttribute("players", players);
        return "players/index";
    }

    /**
     * Objective: Displays a list of all active players in the database.
     *
     * Input: (None directly from request)
     * Output: String - The view name "players/active" and Model containing active players.
     */
    @GetMapping("/active")
    public String listActivePlayers(Model model) {
        model.addAttribute("players", playerService.getAllActivePlayers());
        return "players/active";
    }

    /**
     * Objective: Displays detailed information about a specific player.
     *
     * Input: id - The player ID from the URL path.
     * Output: String - The view name "players/details" with player data, or redirect if not found.
     */
    // @GetMapping("{id}"): Maps HTTP GET requests to this method with a path variable for the player ID.
    // @PathVariable: Extracts the ID from the URL path and injects it as a method parameter.
    @GetMapping("{id}")
    public String getPlayerDetails(
        // @PathVariable: Binds the {id} from the URL to the id parameter.
        @PathVariable Long id,
        Model model
    ) {
        Optional<Player> player = playerService.getPlayerById(id);
        if (player.isPresent()) {
            model.addAttribute("player", player.get());
            return "players/details";
        }
        return "redirect:/players";
    }

    /**
     * Objective: Displays the form for creating a new player.
     *
     * Input: (None)
     * Output: String - The view name "players/form" with an empty player object for the form.
     */
    // @GetMapping("new"): Maps HTTP GET requests to show the create player form.
    @GetMapping("new")
    public String showCreateForm(Model model) {
        model.addAttribute("player", new Player());
        model.addAttribute("isEdit", false);
        return "players/form";
    }

    /**
     * Objective: Processes the creation of a new player from form submission.
     *
     * Input: player - The Player object populated from the form data.
     *        bindingResult - Validation results from the form submission.
     * Output: String - Redirect to the new player's details page or back to form if validation fails.
     */
    // @PostMapping: Maps HTTP POST requests for creating new players.
    // @Valid: Triggers validation constraints defined in the Player entity (e.g., @NotNull, @Column(nullable = false)).
    // @ModelAttribute: Binds form data to the player object automatically.
    @PostMapping
    public String createPlayer(
        @Valid @ModelAttribute("player") Player player,
        // BindingResult: Captures validation errors from the @Valid annotation.
        BindingResult bindingResult,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", false);
            return "players/form";
        }
        Player savedPlayer = playerService.createPlayer(player);
        return "redirect:/players/" + savedPlayer.getId();
    }

    /**
     * Objective: Displays the form for editing an existing player.
     *
     * Input: id - The player ID from the URL path.
     * Output: String - The view name "players/form" with the player data, or redirect if not found.
     */
    // @GetMapping("{id}/edit"): Maps HTTP GET requests to show the edit form for a specific player.
    @GetMapping("{id}/edit")
    public String showEditForm(
        @PathVariable Long id,
        Model model
    ) {
        Optional<Player> player = playerService.getPlayerById(id);
        if (player.isPresent()) {
            model.addAttribute("player", player.get());
            model.addAttribute("isEdit", true);
            return "players/form";
        }
        return "redirect:/players";
    }

    /**
     * Objective: Processes the update of an existing player's information.
     *
     * Input: id - The player ID from the URL path.
     *        player - The updated Player object from the form submission.
     *        bindingResult - Validation results from the form submission.
     * Output: String - Redirect to the updated player's details page or back to form if validation fails.
     */
    // @PostMapping("{id}/edit"): Maps HTTP POST requests for updating a specific player.
    @PostMapping("{id}/edit")
    public String updatePlayer(
        @PathVariable Long id,
        @Valid @ModelAttribute("player") Player player,
        BindingResult bindingResult,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", true);
            return "players/form";
        }
        Optional<Player> updatedPlayer = playerService.updatePlayer(id, player);
        if (updatedPlayer.isPresent()) {
            return "redirect:/players/" + id;
        }
        return "redirect:/players";
    }

    /**
     * Objective: Deletes a player via modal confirmation (POST with _method=DELETE).
     *
     * Input: id - The player ID from the URL path.
     * Output: String - Redirect to the player list after deletion.
     */
    // @PostMapping("{id}"): Maps HTTP POST requests with _method=DELETE for deleting a specific player.
    @PostMapping("{id}")
    public String deletePlayerModal(
        @PathVariable Long id
    ) {
        playerService.deletePlayer(id);
        return "redirect:/players";
    }

    /**
     * Objective: Retrieves all players from a specific team.
     *
     * Input: team - The team name from the request parameter.
     * Output: String - The view name "players/by-team" with players filtered by team.
     */
    // @RequestParam: Extracts the 'team' query parameter from the request URL (e.g., ?team=Lakers).
    @GetMapping("/team")
    public String getPlayersByTeam(
        @RequestParam String team,
        Model model
    ) {
        model.addAttribute("team", team);
        model.addAttribute("players", playerService.getPlayersByTeam(team));
        return "players/by-team";
    }

    /**
     * Objective: Retrieves all active players from a specific team.
     *
     * Input: team - The team name from the request parameter.
     * Output: String - The view name "players/by-team" with active players filtered by team.
     */
    @GetMapping("/team/active")
    public String getActivePlayersByTeam(
        @RequestParam String team,
        Model model
    ) {
        model.addAttribute("team", team);
        model.addAttribute("players", playerService.getActivePlayersByTeam(team));
        return "players/by-team";
    }

    /**
     * Objective: Retrieves all players from a specific country.
     *
     * Input: country - The country name from the request parameter.
     * Output: String - The view name "players/by-country" with players filtered by country.
     */
    @GetMapping("/country")
    public String getPlayersByCountry(
        @RequestParam String country,
        Model model
    ) {
        model.addAttribute("country", country);
        model.addAttribute("players", playerService.getPlayersByCountry(country));
        return "players/by-country";
    }

    /**
     * Objective: Displays a page with statistics about players in a specific team.
     *
     * Input: team - The team name from the request parameter.
     * Output: String - The view name "players/team-stats" with team statistics.
     */
    @GetMapping("/stats/team")
    public String getTeamStatistics(
        @RequestParam String team,
        Model model
    ) {
        Long totalPlayers = playerService.countPlayersByTeam(team);
        Long activePlayers = playerService.countActivePlayersByTeam(team);
        
        model.addAttribute("team", team);
        model.addAttribute("totalPlayers", totalPlayers);
        model.addAttribute("activePlayers", activePlayers);
        model.addAttribute("inactivePlayers", totalPlayers - activePlayers);
        
        return "players/team-stats";
    }

    /**
     * Objective: Displays a page with overall statistics about all players.
     *
     * Input: (None)
     * Output: String - The view name "players/stats" with overall player statistics.
     */
    @GetMapping("/stats")
    public String getOverallStatistics(Model model) {
        Long totalPlayers = playerService.countAllPlayers();
        model.addAttribute("totalPlayers", totalPlayers);
        return "players/stats";
    }

    /**
     * Objective: Deactivates a player without deleting them from the database.
     *
     * Input: id - The player ID from the URL path.
     * Output: String - Redirect to the player's details page or player list.
     */
    @GetMapping("{id}/deactivate")
    public String deactivatePlayer(
        @PathVariable Long id
    ) {
        Optional<Player> deactivatedPlayer = playerService.deactivatePlayer(id);
        if (deactivatedPlayer.isPresent()) {
            return "redirect:/players/" + id;
        }
        return "redirect:/players";
    }

    /**
     * Objective: Reactivates an inactive player.
     *
     * Input: id - The player ID from the URL path.
     * Output: String - Redirect to the player's details page or player list.
     */
    @GetMapping("{id}/reactivate")
    public String reactivatePlayer(
        @PathVariable Long id
    ) {
        Optional<Player> reactivatedPlayer = playerService.reactivatePlayer(id);
        if (reactivatedPlayer.isPresent()) {
            return "redirect:/players/" + id;
        }
        return "redirect:/players";
    }

}
