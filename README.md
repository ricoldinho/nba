# NBA Project - Spring MVC & Thymeleaf Learning Guide

## 📋 Project Purpose

This project is an educational implementation designed to demonstrate and explain the core concepts of **Spring MVC** and **Thymeleaf** within a Spring Boot 4.0.1 application. It serves as a practical reference for understanding how web requests flow through a Model-View-Controller architecture and how template engines render dynamic HTML content.

---

## 🏗️ Architecture Overview

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
- Players List: `http://localhost:8080/players`
- Player Details: `http://localhost:8080/players/{id}`
- New Player Form: `http://localhost:8080/players/new`

---

## 📚 References

- [Spring MVC Documentation](https://spring.io/projects/spring-framework)
- [Thymeleaf Official Guide](https://www.thymeleaf.org/documentation.html)
- [Spring Boot Reference](https://spring.io/projects/spring-boot)

---

## 📄 License

This is an educational project for learning purposes.
