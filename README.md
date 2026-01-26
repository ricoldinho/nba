# NBA Project - Spring MVC & Thymeleaf Learning Guide

## 📋 Project Purpose

This project is an educational implementation designed to demonstrate and explain the core concepts of **Spring MVC** and **Thymeleaf** within a Spring Boot 4.0.1 application. It serves as a practical reference for understanding how web requests flow through a Model-View-Controller architecture and how template engines render dynamic HTML content.

---

## 🆕 Recent Updates

### JWT Authentication & Spring Security (January 26, 2026)
A comprehensive **JWT authentication system** has been implemented to secure the API endpoints:

✅ **Security Dependencies Added:**
- `spring-boot-starter-security` - Spring Security framework
- `jjwt-api`, `jjwt-impl`, `jjwt-jackson` (v0.11.5) - JSON Web Token handling

✅ **User Entity:**
- Created [User.java](src/main/java/edu/mvc/nba/model/User.java) implementing `UserDetails` interface
- Properties: `id` (auto-generated), `username` (unique, required), `password` (required)
- All accounts granted with `ROLE_USER` authority
- Implements all `UserDetails` methods for Spring Security integration

✅ **Repository Layer:**
- Created [UserRepository.java](src/main/java/edu/mvc/nba/repository/UserRepository.java)
- Method: `findByUsername(String username)` - Returns Optional<User> for user lookup

✅ **Service Layer:**
- Created [JwtService.java](src/main/java/edu/mvc/nba/service/JwtService.java) with JWT operations:
  - `generateToken(UserDetails)` - Creates JWT token with 10-hour expiration
  - `isTokenValid(String, UserDetails)` - Validates token signature and expiration
  - `extractUsername(String)` - Extracts username from token claims
  - Helper methods for token parsing and validation
- **Security Key:** 256-bit HS256 secret key (must be moved to `application.properties` for production)

✅ **Configuration Layer:**
- Created [SecurityConfig.java](src/main/java/edu/mvc/nba/config/SecurityConfig.java):
  - `userDetailsService()` - Loads User from database by username
  - `authenticationProvider()` - DaoAuthenticationProvider with BCrypt password encoding
  - `authenticationManager()` - AuthenticationManager bean for credential verification
  - `passwordEncoder()` - BCryptPasswordEncoder for secure password storage
  - `securityFilterChain()` - Security configuration:
    - CSRF disabled for REST APIs
    - `/api/auth/**` endpoints public (login/register)
    - All other endpoints require authentication
    - Stateless session management (no server-side sessions)
    - JWT filter added before UsernamePasswordAuthenticationFilter

✅ **JWT Authentication Filter:**
- Created [JwtAuthenticationFilter.java](src/main/java/edu/mvc/nba/config/JwtAuthenticationFilter.java):
  - Extends `OncePerRequestFilter` to process every request once
  - Extracts JWT from `Authorization: Bearer <token>` header
  - Validates token and loads user from database
  - Sets SecurityContext authentication for Spring Security
  - Allows request to continue if no Authorization header present

✅ **Architecture:**
- Request Flow with JWT:
  1. Client sends request with `Authorization: Bearer <JWT_TOKEN>` header
  2. JwtAuthenticationFilter intercepts request
  3. Token extracted and username extracted from JWT claims
  4. User details loaded from database via UserRepository
  5. Token validated (signature + expiration)
  6. SecurityContext updated with authenticated user
  7. Request proceeds to protected endpoints
  8. Response sent with user context available

✅ **Test Support:**
- Dependencies added for unit testing: JUnit 4 (4.13.2), Mockito (5.2.0)

### Team Entity & Player-Team Relationship (January 23, 2026)
A new **Team entity** has been created with a **One-to-Many relationship** to Player:

✅ **New Entity:**
- Created [Team.java](src/main/java/edu/mvc/nba/model/Team.java) with properties: `id`, `name`, `city`, `country`, `foundingYear`, `active`
- Relationship: `@OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)`

✅ **Player Entity Updates:**
- Replaced `String team` field with `@ManyToOne` relationship to Team entity
- Updated [Player.java](src/main/java/edu/mvc/nba/model/Player.java) with `@JoinColumn(name = "team_id", nullable = false)`
- Added `getTeamEntity()` and `setTeamEntity()` methods for entity access
- Maintained `getTeam()` for backward compatibility (returns team name)

✅ **Repository Layer:**
- Created [TeamRepository.java](src/main/java/edu/mvc/nba/repository/TeamRepository.java) with methods: `findByName()`, `findByActive()`, `findByCity()`, `findByCountry()`, `findAllActiveTeams()`
- Updated [PlayerRepository.java](src/main/java/edu/mvc/nba/repository/PlayerRepository.java): All team-related queries now use Team entity instead of String

✅ **Service Layer:**
- Created [TeamService.java](src/main/java/edu/mvc/nba/service/TeamService.java) with full CRUD operations and team-specific queries
- Updated [PlayerService.java](src/main/java/edu/mvc/nba/service/PlayerService.java): 
  - Updated team-related methods to use Team objects
  - Added `getPlayersByTeamName()`, `getActivePlayersByTeamName()`, `countPlayersByTeamName()`, `countActivePlayersByTeamName()` for String-based lookups
  - Injected `TeamRepository` for team management

✅ **DTO & Form Handling:**
- Created [PlayerTeamDTO.java](src/main/java/edu/mvc/nba/dto/PlayerTeamDTO.java) for combined player-team form submission
- DTO includes validation annotations and properties from both Player and Team

✅ **Controller Enhancements:**
- Injected `TeamService` into [PlayerController.java](src/main/java/edu/mvc/nba/web/PlayerController.java)
- Updated `showCreateForm()` and `showEditForm()` to pass `teams` list to view
- Added new endpoints:
  - `showCreatePlayerWithTeamForm()` → GET `/players/new-with-team`
  - `createPlayerWithTeam()` → POST `/players/create-with-team`

✅ **Form Updates:**
- Modified [form.html](src/main/resources/templates/players/form.html): Changed team input from text field to `<select>` dropdown with active teams
- Created [player-team-form.html](src/main/resources/templates/players/player-team-form.html): New form for creating player and team together
- Both forms include proper validation error handling and responsive design

### Player Search Feature (January 2026)
A new **search functionality** has been added to filter players by name:

✅ **Backend Implementation:**
- Added `findByNameContainingIgnoreCase()` method in [PlayerRepository.java](src/main/java/edu/mvc/nba/repository/PlayerRepository.java) with JPQL query for case-insensitive partial name matching
- Added `searchPlayersByName()` method in [PlayerService.java](src/main/java/edu/mvc/nba/service/PlayerService.java)
- Updated `listPlayers()` endpoint in [PlayerController.java](src/main/java/edu/mvc/nba/web/PlayerController.java) to accept optional `@RequestParam searchName`

✅ **Frontend Implementation:**
- Added search form with input field and buttons in [index.html](src/main/resources/templates/players/index.html)
- Added responsive CSS styling in [styles.css](src/main/resources/static/css/styles.css)

✅ **Testing:**
- Created [PlayerServiceSearchTest.java](src/test/java/edu/mvc/Nba/service/PlayerServiceSearchTest.java) with 5 unit tests
- Created [PlayerControllerSearchTest.java](src/test/java/edu/mvc/Nba/web/PlayerControllerSearchTest.java) with 6 unit tests
- Added JUnit 4 (4.13.2) and Mockito (5.2.0) dependencies to pom.xml

### Player Form (Create/Edit) Feature (January 2026)
A comprehensive **player form** has been created for adding and editing players:

✅ **Frontend Implementation:**
- Created [form.html](src/main/resources/templates/players/form.html) with Thymeleaf binding (`th:object` and `th:field`)
- Form supports both create and edit operations with conditional rendering
- All form styles centralized in [styles.css](src/main/resources/static/css/styles.css)
- Responsive design with Bootstrap 5
- Form validation error display

✅ **Form Features:**
- **Basic Information Section**: Name, Jersey Number, Team, Country, Birth Date, Photo URL
- **Status Section**: Active/Inactive toggle switch
- **Binding**: Uses `th:object="${player}"` and `th:field="*{fieldName}"` for two-way binding
- **Validation**: Server-side error display with custom styling
- **Actions**: Cancel button and dynamic submit button (Create/Save)

✅ **Backend Support:**
- Controller methods already support form handling:
  - `showCreateForm()` → GET `/players/new` (displays empty form)
  - `createPlayer()` → POST `/players` (creates new player)
  - `showEditForm()` → GET `/players/{id}/edit` (displays prefilled form)
  - `updatePlayer()` → POST `/players/{id}/edit` (updates existing player)

---

## 🔐 JWT Authentication Usage

### How JWT Authentication Works in This Project

The application implements a **stateless authentication system** using JWT (JSON Web Tokens). Every protected endpoint requires a valid JWT token in the request header.

### Authentication Flow

```
1. User sends login credentials
   ↓
2. Server validates credentials against User entity
   ↓
3. Server generates JWT token with username as subject
   ↓
4. Client receives token and stores it
   ↓
5. Client includes token in subsequent requests:
   Authorization: Bearer <JWT_TOKEN>
   ↓
6. JwtAuthenticationFilter extracts and validates token
   ↓
7. If valid: Request proceeds with authenticated user context
   If invalid: Request is denied (401 Unauthorized)
```

### JWT Token Structure

The JWT token contains:
- **Header:** Algorithm (HS256) and token type
- **Payload (Claims):** Username as subject, issued time, expiration time (10 hours)
- **Signature:** HMAC-SHA256 signed with the secret key

### Making Authenticated Requests

All API requests to protected endpoints must include the JWT token:

```bash
# Example with curl
curl -X GET http://localhost:8080/players \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."

# Example with JavaScript/Fetch
fetch('http://localhost:8080/players', {
  headers: {
    'Authorization': `Bearer ${token}`
  }
})
```

### Token Validation Process

1. **Header Check:** Verify `Authorization: Bearer ` prefix exists
2. **Extraction:** Extract token part (remove "Bearer " prefix)
3. **Parsing:** Decode JWT using secret key
4. **Username Extraction:** Get subject claim (username)
5. **User Lookup:** Load user from database
6. **Signature Verification:** Validate token signature matches secret key
7. **Expiration Check:** Confirm token expiration time hasn't passed
8. **Context Setup:** Set authenticated user in SecurityContext

### Security Endpoints

Currently configured endpoints:

| Endpoint | Security | Description |
|----------|----------|-------------|
| `/api/auth/**` | ✅ PUBLIC | Login/Register endpoints (not yet implemented) |
| `/players/**` | 🔒 PROTECTED | All player endpoints require JWT |
| Other endpoints | 🔒 PROTECTED | Require valid JWT token |

### Password Security

Passwords are encoded using **BCrypt** with salt:
- When user registers: Password is hashed with BCrypt
- When user logs in: Entered password is hashed and compared
- Original password is never stored

---

The project follows a **Clean Architecture** pattern with strict separation of concerns:

```
HTTP Request
    ↓
[Controller Layer] - Handles requests and returns responses
    ↓
[Service Layer] - Contains business logic
    ↓
[Repository Layer] - Database access
    ↓
[Domain Models] - Entity representations
    ↓
[DTOs] - Data Transfer Objects
    ↓
[Thymeleaf Templates] - HTML rendering
    ↓
HTTP Response (HTML)
```

---

## 🌐 Understanding Spring MVC

### What is Spring MVC?

Spring MVC (Model-View-Controller) is a framework that separates an application into three interconnected layers:

| Layer | Responsibility | Example |
|-------|-----------------|---------|
| **Model** | Data and business logic | Entities, DTOs, Services |
| **View** | Presentation layer | Thymeleaf HTML templates |
| **Controller** | Handles user input and orchestration | `@RestController` or `@Controller` |

### Request Flow in Spring MVC

```
1. User sends HTTP request
        ↓
2. DispatcherServlet (Front Controller) intercepts the request
        ↓
3. Looks up appropriate @Controller method
        ↓
4. Controller calls Service layer
        ↓
5. Service executes business logic
        ↓
6. Service returns data (Model)
        ↓
7. Controller passes Model to View (Thymeleaf)
        ↓
8. Thymeleaf renders HTML from template
        ↓
9. HTTP Response sent to browser
```

### Controller Layer Example

```java
package com.app.nba.web;

import com.app.nba.dto.PlayerDTO;
import com.app.nba.service.PlayerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// @Controller: Marks this class as a web controller that returns views (HTML pages).
@Controller
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    /**
     * Objective: Retrieves all players and renders them in an HTML template.
     *
     * Input: model - Spring's Model object for passing data to the view.
     * Output: String - The name of the Thymeleaf template to render.
     */
    // @GetMapping: Maps HTTP GET requests to retrieve player list.
    @GetMapping
    public String listPlayers(Model model) {
        // Service call to fetch players
        var players = playerService.getAllPlayers();
        
        // Model.addAttribute: Passes data to the Thymeleaf template
        model.addAttribute("players", players);
        model.addAttribute("title", "NBA Players");
        
        // Returns the template name (Thymeleaf will look for players.html in templates/)
        return "players/list";
    }

    /**
     * Objective: Displays a single player details page.
     *
     * Input: id - The player ID from the URL path.
     * Output: String - The template name.
     */
    // @PathVariable: Extracts the ID from the URL path (/players/{id}).
    @GetMapping("/{id}")
    public String playerDetail(
        @PathVariable Long id,
        Model model
    ) {
        PlayerDTO player = playerService.getPlayerById(id);
        model.addAttribute("player", player);
        return "players/detail";
    }
}
```

---

## 🎨 Understanding Thymeleaf

### What is Thymeleaf?

Thymeleaf is a **server-side Java template engine** that processes HTML templates and injects dynamic data from the controller's Model object. It generates final HTML that is sent to the browser.

### Key Features

| Feature | Description |
|---------|-------------|
| **Natural Templates** | Valid HTML even without processing |
| **Spring Integration** | Direct support for Spring expressions and objects |
| **Attribute Processors** | `th:*` attributes for dynamic content |
| **Loop & Conditionals** | `th:each`, `th:if`, `th:unless` for control flow |
| **Expression Language** | `${variable}` to access model data |

### Thymeleaf Template Example

#### Template File: `src/main/resources/templates/players/list.html`

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title th:text="${title}">Players List</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
        th { background-color: #4CAF50; color: white; }
    </style>
</head>
<body>
    <!-- Inline HTML - static content -->
    <h1>NBA Players Database</h1>

    <!-- Dynamic title from Model -->
    <h2 th:text="'Welcome to ' + ${title}">Welcome to Default Title</h2>

    <!-- Conditional rendering: Shows message only if players list is empty -->
    <div th:if="${#lists.isEmpty(players)}">
        <p style="color: red;">No players found in the database.</p>
    </div>

    <!-- Loop through players collection: th:each iterates over Model data -->
    <table th:unless="${#lists.isEmpty(players)}">
        <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Position</th>
                <th>Team</th>
                <th>Points Per Game</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <!-- Each player object from the 'players' list becomes 'player' variable -->
            <tr th:each="player : ${players}">
                <!-- Access object properties using ${variable.property} -->
                <td th:text="${player.id}">1</td>
                <td th:text="${player.name}">Player Name</td>
                <td th:text="${player.position}">Guard</td>
                <td th:text="${player.team}">Team Name</td>
                <td th:text="${#numbers.formatDecimal(player.pointsPerGame, 1, 2)}">0.00</td>
                
                <!-- Generate URLs with Thymeleaf: @{} syntax -->
                <td>
                    <a th:href="@{/players/{id}(id=${player.id})}" 
                       th:text="View Details">View</a>
                </td>
            </tr>
        </tbody>
    </table>

    <!-- Link to create new player -->
    <hr>
    <p>
        <a th:href="@{/players/new}">Create New Player</a>
    </p>
</body>
</html>
```

#### Template File: `src/main/resources/templates/players/detail.html`

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title th:text="'Player: ' + ${player.name}">Player Details</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .detail-card { border: 1px solid #ddd; padding: 20px; max-width: 600px; }
        .label { font-weight: bold; color: #333; }
        .value { color: #666; margin-bottom: 10px; }
    </style>
</head>
<body>
    <h1>Player Details</h1>

    <!-- Conditional: Show error message if player not found -->
    <div th:if="${player == null}" style="color: red;">
        <p>Player not found.</p>
        <a th:href="@{/players}">Back to list</a>
    </div>

    <!-- Display player details if found -->
    <div th:if="${player != null}" class="detail-card">
        <p>
            <span class="label">Name:</span>
            <span class="value" th:text="${player.name}">N/A</span>
        </p>
        <p>
            <span class="label">Position:</span>
            <span class="value" th:text="${player.position}">N/A</span>
        </p>
        <p>
            <span class="label">Team:</span>
            <span class="value" th:text="${player.team}">N/A</span>
        </p>
        <p>
            <span class="label">Points Per Game:</span>
            <span class="value" th:text="${#numbers.formatDecimal(player.pointsPerGame, 1, 2)}">0.00</span>
        </p>
        <p>
            <span class="label">Assists Per Game:</span>
            <span class="value" th:text="${#numbers.formatDecimal(player.assistsPerGame, 1, 2)}">0.00</span>
        </p>

        <hr>
        <a th:href="@{/players}">Back to Players List</a>
        <a th:href="@{/players/{id}/edit(id=${player.id})}">Edit Player</a>
    </div>
</body>
</html>
```

---

## 🔄 Complete Request-Response Cycle Example

### Scenario: User clicks "View Details" for a player

#### 1. Browser sends request:
```
GET /players/42
```

#### 2. Spring's DispatcherServlet routes to Controller:
```java
@GetMapping("/{id}")
public String playerDetail(@PathVariable Long id, Model model) {
    // id = 42
    PlayerDTO player = playerService.getPlayerById(42);
    model.addAttribute("player", player); // Passes data to view
    return "players/detail"; // Returns template name
}
```

#### 3. Thymeleaf renders the template:
- Reads `src/main/resources/templates/players/detail.html`
- Replaces `${player.name}` with actual data (e.g., "LeBron James")
- Replaces `${player.pointsPerGame}` with actual value (e.g., "25.50")
- Processes conditionals and loops
- Generates final HTML

#### 4. Browser receives HTML:
```html
<!DOCTYPE html>
<html>
<head>
    <title>Player: LeBron James</title>
</head>
<body>
    <h1>Player Details</h1>
    <div class="detail-card">
        <p>
            <span class="label">Name:</span>
            <span class="value">LeBron James</span>
        </p>
        <p>
            <span class="label">Points Per Game:</span>
            <span class="value">25.50</span>
        </p>
        <!-- ... -->
    </div>
</body>
</html>
```

---

## 📦 Project Structure

```
src/main/java/edu/mvc/nba/
├── web/                      # Controllers - HTTP request handlers
│   └── PlayerController.java
├── service/                  # Business logic layer
│   └── PlayerService.java
├── repository/               # Database access
│   └── PlayerRepository.java
├── model/                    # JPA Entities
│   └── Player.java
├── dto/                      # Data Transfer Objects
│   └── PlayerDTO.java
└── NbaApplication.java       # Spring Boot entry point

src/main/resources/
├── templates/                # Thymeleaf HTML templates
│   ├── players/
│   │   ├── list.html
│   │   ├── detail.html
│   │   └── form.html
│   └── index.html
├── static/                   # Static files (CSS, JS, images)
└── application.properties    # Spring configuration
```

---

## 🛠️ Key Spring MVC Annotations

| Annotation | Purpose |
|-----------|---------|
| `@Controller` | Marks class as web controller for view rendering |
| `@RestController` | Returns JSON/data directly (no view rendering) |
| `@RequestMapping` | Maps URL path to controller |
| `@GetMapping` | Maps HTTP GET requests |
| `@PostMapping` | Maps HTTP POST requests |
| `@PathVariable` | Extracts value from URL path |
| `@RequestParam` | Extracts query parameters |
| `@ModelAttribute` | Binds form data to objects |
| `@Valid` | Validates input data |

---

## 🔍 Player Search Feature

The application includes a **player search functionality** that allows users to filter players by partial name matching.

### How It Works

#### Frontend (Thymeleaf Template):
```html
<!-- Search Form in index.html -->
<form method="GET" th:action="@{/players}" class="search-form-card">
    <input type="text" name="searchName" placeholder="Buscar jugador por nombre..." 
        th:value="${searchName != null ? searchName : ''}">
    <button type="submit" class="btn btn-primary">
        <i class="fas fa-search"></i> Buscar
    </button>
    <a th:href="@{/players}" class="btn btn-secondary">
        <i class="fas fa-redo"></i> Limpiar
    </a>
</form>
```

#### Backend (Controller):
```java
/**
 * Objective: Displays a list of all players with optional search filter by name.
 *
 * Input: searchName - Optional search term (case-insensitive partial match).
 * Output: String - "players/index" view with filtered players.
 */
@GetMapping
public String listPlayers(
    // @RequestParam: Extracts the 'searchName' query parameter with optional empty default.
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
```

#### Service Layer:
```java
/**
 * Objective: Searches for players by name using partial matching (case-insensitive).
 *
 * Input: name - The search term to match against player names.
 * Output: List<Player> - All players whose name contains the search term.
 */
@Transactional(readOnly = true)
public List<Player> searchPlayersByName(String name) {
    if (name == null || name.trim().isEmpty()) {
        return getAllPlayers();
    }
    return playerRepository.findByNameContainingIgnoreCase(name.trim());
}
```

#### Repository Query:
```java
/**
 * Objective: Find all players whose name contains the search term (case-insensitive).
 *
 * Input: name - The search term to match against player names.
 * Output: List<Player> - All players matching the search term, ordered by name.
 */
@Query("SELECT p FROM Player p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY p.name")
List<Player> findByNameContainingIgnoreCase(@Param("name") String name);
```

### Request Examples

```
# Get all players
GET http://localhost:8080/players

# Search for players containing "James"
GET http://localhost:8080/players?searchName=James

# Search for players containing "Lebron" (case-insensitive)
GET http://localhost:8080/players?searchName=lebron

# Clear search and show all players
GET http://localhost:8080/players
```

---

## 🎯 Key Thymeleaf Directives

| Directive | Purpose | Example |
|-----------|---------|---------|
| `th:text` | Sets element text content | `th:text="${player.name}"` |
| `th:href` | Generates URLs | `th:href="@{/players/{id}(id=${player.id})}"` |
| `th:each` | Loops through collections | `th:each="player : ${players}"` |
| `th:if` | Conditional rendering | `th:if="${players.size() > 0}"` |
| `th:unless` | Negative conditional | `th:unless="${player == null}"` |
| `th:object` | Creates context object | `th:object="${playerForm}"` |
| `th:field` | Binds form fields | `th:field="*{name}"` |
| `th:attr` | Sets any attribute | `th:attr="data-id=${player.id}"` |

---

## 📝 Thymeleaf Form Binding (Two-Way Data Binding)

Form binding allows seamless synchronization between HTML forms and Java objects. The player form (`form.html`) demonstrates this feature:

### Form Binding Example

```html
<!-- Form binds to the 'player' object from the Model -->
<form th:object="${player}" method="POST">
    
    <!-- th:field binds to player.name property -->
    <input type="text" th:field="*{name}" placeholder="Player Name">
    
    <!-- th:field automatically generates id, name, and binds the value -->
    <!-- This creates: <input id="name" name="name" value="LeBron James"> -->
    
    <!-- Error handling for validation -->
    <div th:if="${#fields.hasErrors('name')}" th:errors="*{name}">
        Error message
    </div>
</form>
```

### How It Works

1. **Binding Direction (Controller → View):**
   - Controller passes Model with player object: `model.addAttribute("player", player);`
   - Thymeleaf renders form with pre-filled values from player properties

2. **Form Submission (View → Controller):**
   - User submits form with filled data
   - Spring automatically converts form data back to Player object
   - Controller receives populated Player object via `@ModelAttribute("player")`

### Controller Method Example

```java
@PostMapping
public String createPlayer(
    @Valid @ModelAttribute("player") Player player,
    BindingResult bindingResult,
    Model model
) {
    if (bindingResult.hasErrors()) {
        // Validation failed, return form with errors
        return "players/form";
    }
    // Validation passed, save player to database
    playerService.createPlayer(player);
    return "redirect:/players";
}
```

### Key Form Binding Features

- **`th:object="${player}`** - Sets context object for the form
- **`th:field="*{propertyName}`** - Binds input to player property (uses `*{}` relative syntax)
- **`@Valid`** - Triggers validation constraints on Player entity
- **`BindingResult`** - Captures validation errors to display in form
- **`#fields.hasErrors()`** - Checks if specific field has errors
- **`th:errors="*{field}"`** - Displays validation error messages

---

## 📝 Development Guidelines

This project adheres to strict coding standards defined in [AGENTS.md](AGENTS.md):

- ✅ **Clean Architecture** - Layered separation of concerns
- ✅ **English naming conventions** - PascalCase classes, camelCase methods
- ✅ **Javadoc documentation** - All public methods documented
- ✅ **Annotation explanations** - Comments above every Spring annotation
- ✅ **Unit testing** - Every service/controller method tested (JUnit 4 + Mockito)
- ✅ **DTO pattern** - Entity-DTO conversion via Mappers

---

## 🚀 Running the Application

### Prerequisites
- Java 8+
- Maven 3.6+
- Spring Boot 4.0.1

### Build and Run
```bash
# Build project
mvn clean install

# Run application
mvn spring-boot:run

# Application starts at http://localhost:8080
```

### Access Points
- Players List: `http://localhost:8080/players` (requires JWT)
- Search Players: `http://localhost:8080/players?searchName=lebron` (requires JWT)
- Player Details: `http://localhost:8080/players/{id}` (requires JWT)
- New Player Form: `http://localhost:8080/players/new` (requires JWT)
- Active Players: `http://localhost:8080/players/active` (requires JWT)
- Statistics: `http://localhost:8080/players/stats` (requires JWT)
- **Public Endpoints:** `/api/auth/**` (login/register - not yet implemented)

---

## 📚 References

- [Spring MVC Documentation](https://spring.io/projects/spring-framework)
- [Thymeleaf Official Guide](https://www.thymeleaf.org/documentation.html)
- [Spring Boot Reference](https://spring.io/projects/spring-boot)
- [Spring Security Documentation](https://spring.io/projects/spring-security)
- [JWT (JSON Web Token) - RFC 7519](https://tools.ietf.org/html/rfc7519)
- [JJWT (Java JWT) Library](https://github.com/jwtk/jjwt)

---

## ⚠️ Important Security Notes

### JWT Secret Key
- **Current:** Hardcoded in [JwtService.java](src/main/java/edu/mvc/nba/service/JwtService.java)
- **Production:** MUST be moved to `application.properties` or environment variables
- **Requirements:** At least 256 bits (32 characters) for HS256 algorithm
- **Never:** Commit secret keys to version control

### Token Expiration
- **Duration:** 10 hours
- **Validation:** Every request checks token expiration
- **Expired Tokens:** Treated as unauthorized (401 response)

### Login/Register Endpoints
- **Status:** Not yet implemented
- **TODO:** Create AuthController with `/api/auth/register` and `/api/auth/login` endpoints
- **TODO:** Hash passwords before storing in database

### Session Management
- **Type:** Stateless (no server-side sessions)
- **Reason:** JWT tokens are self-contained and don't require session storage
- **Benefit:** Easily scalable across multiple servers

---

This is an educational project for learning purposes.
