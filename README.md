# Burnt Toast — Backend

A REST API for a recipe planner application, built with Spring Boot and Java 21.

> This is a learning project built to get hands-on experience with Spring Boot, Java, Spring Security, JPA/Hibernate, and MySQL in a short timeframe. It is intentionally not production-ready (see [Known Limitations](#known-limitations) at the bottom for a full list of what's missing or should be improved.)

---

## Stack

- **Java 21**
- **Spring Boot 3**
- **Spring Security** — JWT-based stateless authentication
- **Spring Data JPA / Hibernate** — ORM with MySQL
- **MySQL 8** — database (Docker container in production)
- **Jsoup** — thumbnail extraction from recipe URLs
- **Docker** — containerised deployment

---

## Deployment

- **Server:** Hetzner VPS (Ubuntu 24.04)
- **Backend:** Docker container, port 8081
- **Database:** MySQL Docker container on the same VPS
- **Live:** http://46.224.185.177

## Known Limitations

These are known issues and missing features. They are not oversights — the goal of this project was to learn Spring Boot, Java, Spring Security, and related tooling in a short timeframe, not to build a production-ready application.

### Security & authorization

- **No ownership check on update/delete** — any authenticated user can edit or delete any recipe, not just their own; `RecipeService.update()` and `delete()` should verify the recipe belongs to the current user
- **Search not filtered by user** — `RecipeService.search()` returns results across all users, not just the currently logged-in user

### Input validation

- **No DTO validation** — `AuthRequestDTO`, `RecipeDTO`, `CategoryDTO`, and `TagDTO` have no `@NotNull`, `@Size`, `@Email`, or other constraint annotations
- **No `@Valid` in controllers** — even if constraints were added to DTOs, they would not be enforced without `@Valid` on controller method parameters
- **Rating not validated** — `RecipeDTO.rating` is documented as 1–5 but no constraint prevents saving 0, -1, or 999

### Error handling

- **Generic exception handler** — `GlobalExceptionHandler` catches all `Exception.class` and returns a generic message; specific exceptions should be handled explicitly for better debugging and client responses

### Code quality

- **No unit or integration tests** — intentionally skipped to focus on feature development and learning
- **No pagination** — all list endpoints return the full result set; this will degrade with large datasets

  _And probably more bugs and issues that I haven't listed here since time spent finding them and writing them down is time I could spend fixing them._ 🤓

### What was done intentionally

- Clean layered architecture: Controller → Service → Repository
- DTO pattern for all request/response objects
- BCrypt password hashing
- JWT stateless authentication
- Custom exception hierarchy with appropriate HTTP status codes
