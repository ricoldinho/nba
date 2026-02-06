package edu.mvc.nba.web;

import edu.mvc.nba.model.Player;
import edu.mvc.nba.model.Team;
import edu.mvc.nba.dto.PlayerTeamDTO;
import edu.mvc.nba.service.PlayerService;
import edu.mvc.nba.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    private final TeamService teamService;

    // @Autowired: (Implicit) Injects the required PlayerService and TeamService dependencies via constructor injection.
    public PlayerController(PlayerService playerService, TeamService teamService) {
        this.playerService = playerService;
        this.teamService = teamService;
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
    @PreAuthorize("isAuthenticated()")
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
    @PreAuthorize("isAuthenticated()")
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
    @PreAuthorize("isAuthenticated()")
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
    @PreAuthorize("hasRole('ADMIN')")
    public String showCreateForm(Model model) {
        model.addAttribute("player", new Player());
        model.addAttribute("isEdit", false);
        model.addAttribute("teams", teamService.getAllActiveTeams());
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
    @PreAuthorize("hasRole('ADMIN')")
    public String createPlayer(
        @Valid @ModelAttribute("player") Player player,
        // BindingResult: Captures validation errors from the @Valid annotation.
        BindingResult bindingResult,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", false);
            model.addAttribute("teams", teamService.getAllActiveTeams());
            return "players/form";
        }
        
        // Convert teamId to Team object before saving
        if (player.getTeamId() != null) {
            Optional<Team> team = teamService.getTeamById(player.getTeamId());
            if (team.isPresent()) {
                player.setTeamEntity(team.get());
            } else {
                bindingResult.rejectValue("teamId", "error.player", "El equipo seleccionado no existe");
                model.addAttribute("isEdit", false);
                model.addAttribute("teams", teamService.getAllActiveTeams());
                return "players/form";
            }
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
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditForm(
        @PathVariable Long id,
        Model model
    ) {
        Optional<Player> player = playerService.getPlayerById(id);
        if (player.isPresent()) {
            Player p = player.get();
            // Set teamId from the current team for form binding
            if (p.getTeamEntity() != null) {
                p.setTeamId(p.getTeamEntity().getId());
            }
            model.addAttribute("player", p);
            model.addAttribute("isEdit", true);
            model.addAttribute("teams", teamService.getAllActiveTeams());
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
    @PreAuthorize("hasRole('ADMIN')")
    public String updatePlayer(
        @PathVariable Long id,
        @Valid @ModelAttribute("player") Player player,
        BindingResult bindingResult,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", true);
            model.addAttribute("teams", teamService.getAllActiveTeams());
            return "players/form";
        }
        
        // Convert teamId to Team object before saving
        if (player.getTeamId() != null) {
            Optional<Team> team = teamService.getTeamById(player.getTeamId());
            if (team.isPresent()) {
                player.setTeamEntity(team.get());
            } else {
                bindingResult.rejectValue("teamId", "error.player", "El equipo seleccionado no existe");
                model.addAttribute("isEdit", true);
                model.addAttribute("teams", teamService.getAllActiveTeams());
                return "players/form";
            }
        }
        
        Optional<Team> team = teamService.getTeamById(player.getTeamId());
        if (team.isPresent()) {
            player.setTeamEntity(team.get());
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("isAuthenticated()")
    public String getPlayersByTeam(
        @RequestParam String team,
        Model model
    ) {
        model.addAttribute("team", team);
        model.addAttribute("players", playerService.getPlayersByTeamName(team));
        return "players/by-team";
    }

    /**
     * Objective: Retrieves all active players from a specific team.
     *
     * Input: team - The team name from the request parameter.
     * Output: String - The view name "players/by-team" with active players filtered by team.
     */
    @GetMapping("/team/active")
    @PreAuthorize("isAuthenticated()")
    public String getActivePlayersByTeam(
        @RequestParam String team,
        Model model
    ) {
        model.addAttribute("team", team);
        model.addAttribute("players", playerService.getActivePlayersByTeamName(team));
        return "players/by-team";
    }

    /**
     * Objective: Retrieves all players from a specific country.
     *
     * Input: country - The country name from the request parameter.
     * Output: String - The view name "players/by-country" with players filtered by country.
     */
    @GetMapping("/country")
    @PreAuthorize("isAuthenticated()")
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
    @PreAuthorize("isAuthenticated()")
    public String getTeamStatistics(
        @RequestParam String team,
        Model model
    ) {
        Long totalPlayers = playerService.countPlayersByTeamName(team);
        Long activePlayers = playerService.countActivePlayersByTeamName(team);
        
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
    @PreAuthorize("isAuthenticated()")
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    public String reactivatePlayer(
        @PathVariable Long id
    ) {
        Optional<Player> reactivatedPlayer = playerService.reactivatePlayer(id);
        if (reactivatedPlayer.isPresent()) {
            return "redirect:/players/" + id;
        }
        return "redirect:/players";
    }

    /**
     * Objective: Displays the form for creating a new player and team together.
     *
     * Input: (None)
     * Output: String - The view name "players/player-team-form" with an empty PlayerTeamDTO object.
     */
    // @GetMapping("new-with-team"): Maps HTTP GET requests to show the combined create form.
    @GetMapping("new-with-team")
    @PreAuthorize("hasRole('ADMIN')")
    public String showCreatePlayerWithTeamForm(Model model) {
        model.addAttribute("playerTeamDTO", new PlayerTeamDTO());
        return "players/player-team-form";
    }

    /**
     * Objective: Processes the creation of a new player and team from a combined form submission.
     *
     * Input: playerTeamDTO - DTO containing both player and team data from the form.
     *        bindingResult - Validation results from the form submission.
     * Output: String - Redirect to the new player's details page or back to form if validation fails.
     */
    // @PostMapping("create-with-team"): Maps HTTP POST requests for creating player and team together.
    // @Valid: Triggers validation constraints defined in the PlayerTeamDTO.
    // @ModelAttribute: Binds form data to the playerTeamDTO object automatically.
    @PostMapping("create-with-team")
    @PreAuthorize("hasRole('ADMIN')")
    public String createPlayerWithTeam(
        @Valid @ModelAttribute("playerTeamDTO") PlayerTeamDTO playerTeamDTO,
        // BindingResult: Captures validation errors from the @Valid annotation.
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "players/player-team-form";
        }
        Player savedPlayer = playerService.createPlayerWithTeam(playerTeamDTO);
        return "redirect:/players/" + savedPlayer.getId();
    }

}
