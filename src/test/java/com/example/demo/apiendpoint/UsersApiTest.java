package com.example.demo.apiendpoint;

import com.example.demo.model.Users;
import com.example.demo.repositories.UsersRepo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UsersApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsersRepo usersRepo;

    // =========================================================================
    // GET /users  — Get All Users
    // =========================================================================

    @Test
    void getAllUsers_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links").exists());
    }

    @Test
    void getAllUsers_shouldReturnEmbeddedUsersList() throws Exception {
        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                // Spring Data REST uses entity name plural — "userses" for Users class
                .andExpect(jsonPath("$._embedded.userses").isArray());
    }

    @Test
    void getAllUsers_shouldReturnPageMetadata() throws Exception {
        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$.page.totalElements").isNumber());
    }

    // =========================================================================
    // GET /users/{id}  — Get User By ID
    // =========================================================================

    @Test
    void getUserById_shouldReturnExistingUser() throws Exception {
        Users user = usersRepo.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        mockMvc.perform(get("/users/{id}", user.getUserId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void getUserById_shouldReturn404ForInvalidId() throws Exception {
        mockMvc.perform(get("/users/{id}", 999999))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    // =========================================================================
    // POST /users  — Create New User
    // =========================================================================

    @Test
    void createUser_shouldReturnCreated_whenValidDataProvided() throws Exception {
        // System.nanoTime() guarantees unique email on every run
        String newUserJson = """
                {
                    "name": "Test User",
                    "email": "testapi_%d@example.com",
                    "balance": 1000.00
                }
                """.formatted(System.nanoTime());

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(newUserJson))
                .andDo(print())
                // Spring Data REST returns 201 with Location header, body is empty
                .andExpect(status().isCreated());
    }

    @Test
    void createUser_shouldReturn409_whenDuplicateEmail() throws Exception {
        // aman.gupta@example.in exists in seed data
        String duplicateJson = """
                {
                    "name": "Duplicate User",
                    "email": "aman.gupta@example.in",
                    "balance": 500.00
                }
                """;

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(duplicateJson))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void createUser_shouldReturn400_whenBodyIsEmpty() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(""))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    // =========================================================================
    // PUT /users/{id}  — Full Update User
    // =========================================================================

    @Test
    void updateUser_shouldReturnOk_whenValidRequest() throws Exception {
        Users user = usersRepo.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        String updateJson = """
                {
                    "name": "Updated Name",
                    "email": "%s",
                    "balance": 9999.00
                }
                """.formatted(user.getEmail());

        mockMvc.perform(put("/users/{id}", user.getUserId())
                        .contentType("application/json")
                        .content(updateJson))
                .andDo(print())
                // Spring Data REST PUT on existing resource returns 204 No Content
                .andExpect(status().isNoContent());
    }

    @Test
    void updateUser_shouldReturn400_whenBodyIsEmpty() throws Exception {
        mockMvc.perform(put("/users/{id}", 1)
                        .contentType("application/json")
                        .content(""))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    // =========================================================================
    // PATCH /users/{id}  — Partial Update User
    // NOTE: Spring Data REST returns 204 No Content for PATCH — no body
    // =========================================================================

    @Test
    void patchUser_shouldReturn204_whenPartialDataProvided() throws Exception {
        Users user = usersRepo.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        String patchJson = """
                {
                    "name": "Patched Name"
                }
                """;

        mockMvc.perform(patch("/users/{id}", user.getUserId())
                        .contentType("application/json")
                        .content(patchJson))
                .andDo(print())
                // Spring Data REST PATCH returns 204 No Content, not 200
                .andExpect(status().isNoContent());
    }

    @Test
    void patchUser_shouldReturn404_whenUserDoesNotExist() throws Exception {
        String patchJson = """
                {
                    "name": "Nobody"
                }
                """;

        mockMvc.perform(patch("/users/{id}", 999999)
                        .contentType("application/json")
                        .content(patchJson))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    // =========================================================================
    // GET /users/search  — List All Exposed Search Endpoints
    // =========================================================================

    @Test
    void searchEndpoint_shouldExposeAllSearchMethods() throws Exception {
        mockMvc.perform(get("/users/search"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.findByName").exists())
                .andExpect(jsonPath("$._links.findByAddress_City").exists())
                .andExpect(jsonPath("$._links.findByAddress_State").exists());
    }

    // =========================================================================
    // GET /users/search/findByName  — Search By Name
    // =========================================================================

    @Test
    void findByName_shouldReturnOk_whenNameExists() throws Exception {
        Users user = usersRepo.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        mockMvc.perform(get("/users/search/findByName")
                        .param("name", user.getName()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links").exists());
    }

    @Test
    void findByName_shouldReturnEmptyList_whenNameNotFound() throws Exception {
        mockMvc.perform(get("/users/search/findByName")
                        .param("name", "NonExistentName999"))
                .andDo(print())
                .andExpect(status().isOk())
                // Spring Data REST returns _embedded.userses: [] for empty results
                .andExpect(jsonPath("$._embedded.userses").isArray())
                .andExpect(jsonPath("$._embedded.userses").isEmpty());
    }

    // =========================================================================
    // GET /users/search/findByAddress_City  — Search By City
    // =========================================================================

    @Test
    void findByAddress_City_shouldReturnOk_whenCityExists() throws Exception {
        Users user = usersRepo.findAll()
                .stream()
                .filter(u -> u.getAddress() != null)
                .findFirst()
                .orElseThrow();

        String city = user.getAddress().getCity();

        mockMvc.perform(get("/users/search/findByAddress_City")
                        .param("city", city))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links").exists());
    }

    @Test
    void findByAddress_City_shouldReturnEmptyList_whenCityNotFound() throws Exception {
        mockMvc.perform(get("/users/search/findByAddress_City")
                        .param("city", "NonExistentCity999"))
                .andDo(print())
                .andExpect(status().isOk())
                // Spring Data REST returns _embedded.userses: [] for empty results
                .andExpect(jsonPath("$._embedded.userses").isArray())
                .andExpect(jsonPath("$._embedded.userses").isEmpty());
    }

    // =========================================================================
    // GET /users/search/findByAddress_State  — Search By State
    // =========================================================================

    @Test
    void findByAddress_State_shouldReturnOk_whenStateExists() throws Exception {
        Users user = usersRepo.findAll()
                .stream()
                .filter(u -> u.getAddress() != null)
                .findFirst()
                .orElseThrow();

        String state = user.getAddress().getState();

        mockMvc.perform(get("/users/search/findByAddress_State")
                        .param("state", state))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links").exists());
    }

    @Test
    void findByAddress_State_shouldReturnEmptyList_whenStateNotFound() throws Exception {
        mockMvc.perform(get("/users/search/findByAddress_State")
                        .param("state", "NonExistentState999"))
                .andDo(print())
                .andExpect(status().isOk())
                // Spring Data REST returns _embedded.userses: [] for empty results
                .andExpect(jsonPath("$._embedded.userses").isArray())
                .andExpect(jsonPath("$._embedded.userses").isEmpty());
    }
}