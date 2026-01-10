# AGENTS.md - Spring Boot Coding Standards & Architecture Guidelines

This document defines the strict coding standards, architectural rules, and documentation requirements for this project. AI agents and developers must adhere to these guidelines without exception.

## 1. Project Architecture (Clean Architecture)
The project follows a strict separation of concerns based on Spring Boot best practices.

* **Controller Layer (`web` package):**
    * **Role:** Handles HTTP requests, validates input (`@Valid`), and returns DTOs or View names.
    * **Restriction:** NEVER contains business logic. NEVER accesses the Repository directly.
    * **Dependency:** Only talks to the **Service Layer**.

* **Service Layer (`service` package):**
    * **Role:** Contains all business logic, transactions (`@Transactional`), and orchestration.
    * **Restriction:** Must be agnostic of the web layer (no `HttpServletRequest`, no `ResponseEntity`).
    * **Dependency:** Talks to **Repositories** and other **Services**.

* **Repository Layer (`repository` package):**
    * **Role:** Interfaces extending `JpaRepository` for database access.
    * **Restriction:** No logic here, only queries.

* **Domain Layer (`model` package):**
    * **Role:** JPA Entities (`@Entity`) representing the database schema.
    * **Restriction:** NEVER exposed to the Controller/Client.

* **DTO Layer (`dto` package):**
    * **Role:** DTOs (immutable classes or records) used to transfer data between layers.
    * **Rule:** Use `Mapper` classes to convert Entity <-> DTO.
    * **Note:** Use Java Records (Java 14+) or standard classes with final fields for Spring 4.0.1+.

## 2. Naming Conventions (Strict English)
All code, variables, comments, and file names must be in **English**.

* **Classes:** `PascalCase` (e.g., `UserService`, `ProductController`).
* **Methods & Variables:** `camelCase` (e.g., `findActiveUsers`, `customerEmail`).
* **Constants:** `UPPER_SNAKE_CASE` (e.g., `MAX_RETRY_ATTEMPTS`).
* **Tables (DB):** `snake_case` (e.g., `order_items`).

## 3. Documentation Standard
Every public method in the Service and Controller layers must include a Javadoc block following this exact template:

**Format:**

```java
/**
 * Objective: [Concise description of what the method achieves]
 *
 * Input: [Parameter Name] - [Description of data entering the method]
 * Output: [Return Type] - [Description of data leaving the method]
 */
```

## 4. README.md Maintenance Requirement
The `README.md` file serves as the **single source of truth** for project documentation and must be kept synchronized with the codebase.

* **Requirement:** Every new feature, endpoint, service method, or architectural change **MUST** be documented in `README.md` before the feature is considered complete.
* **Timeline:** README updates are not optional post-development tasks—they must be done as part of the feature implementation.
* **Content to Update:**
    * New Controller endpoints and their purpose
    * New Service layer methods and business logic
    * New Thymeleaf templates and their use cases
    * Updated architecture diagrams if layers are modified
    * New project structure folders or packages
    * Example code snippets if they demonstrate new patterns
    * Updated request-response cycles for new features
* **Quality Standard:** README examples must follow the same coding standards as the main codebase (see AGENTS.md sections 1-6).
* **Audience:** README.md is for developers, students, and maintainers—use clear, concise English with practical examples.

---

## 5. Annotation & Decorator Rules
To ensure clarity, **every Spring annotation must be explained**.
* **Rule:** Place a single-line comment `//` immediately above the annotation explaining its specific mission in that context.

**Format:**

```java
// [Annotation Name]: [Specific explanation of why it is used here]
@Annotation
```

## 6. Mandatory Unit Testing
**Rule:** Every single new method generated in `Service` or `Controller` layers must have a corresponding Unit Test.

* **Frameworks:** JUnit 4, Mockito (Spring 4.0.1 compatible).
* **Pattern:** **AAA** (Arrange, Act, Assert).
* **Naming Convention:** `should[ExpectedBehavior]_When[Condition]`
    * *Example:* `shouldReturnUserDTO_WhenIdExists()`
    * *Example:* `shouldThrowException_WhenStockIsInsufficient()`

### 5.1. Service Layer Testing Rules
* **Goal:** Test business logic in isolation.
* **Tools:** Use `@RunWith(MockitoJUnitRunner.class)` for JUnit 4 compatibility.
* **Mocks:** ALL dependencies (Repositories, other Services) must be mocked using `@Mock`.

### 5.2. Controller Layer Testing Rules
* **Goal:** Test HTTP status, JSON serialization, and Input Validation.
* **Tools:** Use `@WebMvcTest(ControllerClass.class)` and `MockMvc`.
* **Restriction:** Do NOT load the full context (`@SpringBootTest`). Mock the Service layer using `@MockBean`.

### 5.3. Test Implementation Example

```java
package com.app.store.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import java.util.Optional;

// @RunWith(MockitoJUnitRunner.class): Enables Mockito support for JUnit 4 (Spring 4.0.1 compatible).
@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void shouldReturnOrder_WhenIdExists() {
        // Arrange
        Long orderId = 1L;
        Order mockOrder = new Order(orderId, "Pending");
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));

        // Act
        OrderDTO result = orderService.getOrderById(orderId);

        // Assert
        assertNotNull(result);
        assertEquals(orderId, result.getId());
        verify(orderRepository).findById(orderId);
    }
}
```

## 7. Implementation Examples (The Gold Standard)

Use the following code snippets as the absolute reference for style, naming, documentation, and architecture.

### 6.1. Controller Example

```java
package com.app.store.web;

import com.app.store.dto.OrderRequestDTO;
import com.app.store.dto.OrderResponseDTO;
import com.app.store.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// @RestController: Indicates that this class handles HTTP requests and returns JSON responses directly.
// @RequestMapping: Sets the base URL path "/api/v1/orders" for all endpoints in this controller.
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    // @Autowired: (Implicit) Injects the required OrderService dependency via constructor injection.
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Objective: Processes a new customer order, validates stock, and persists it.
     *
     * Input: orderRequest - A DTO containing product IDs and quantities.
     * Output: OrderResponseDTO - The created order details including the generated ID and timestamp.
     */
    // @PostMapping: Maps HTTP POST requests to this method for resource creation.
    // @ResponseStatus: Returns HTTP 201 (Created) upon successful execution.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<OrderResponseDTO> createOrder(
        // @Valid: Triggers validation constraints defined in the DTO (e.g., @NotNull, @Min).
        // @RequestBody: Deserializes the JSON request body into the DTO.
        @RequestBody @Valid OrderRequestDTO orderRequest
    ) {
        OrderResponseDTO newOrder = orderService.placeOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
    }
}
```

### 6.2. Service Example

```java
package com.app.store.service;

import com.app.store.dto.OrderRequestDTO;
import com.app.store.dto.OrderResponseDTO;
import com.app.store.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// @Service: Marks this class as a Service component in the Spring context to hold business logic.
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Objective: Orchestrates the creation of an order and ensures database consistency.
     *
     * Input: request - The raw order data from the controller.
     * Output: OrderResponseDTO - A secure object returning only allowed data to the user.
     */
    // @Transactional: Ensures that all database operations in this method succeed or fail as a single atomic unit.
    @Transactional
    public OrderResponseDTO placeOrder(OrderRequestDTO request) {
        // Business logic implementation...
        // 1. Validate Stock
        // 2. Save Entity
        // 3. Return DTO
        return new OrderResponseDTO(/*...*/);
    }
}
```



