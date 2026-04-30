package com.example.demo.apiendpoint;

import com.example.demo.model.VirtualGoldHoldings;
import com.example.demo.repositories.VirtualGoldHoldingsRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * API tests for Spring Data REST generated endpoints of VirtualGoldHoldings.
 * Entity has holdingId, user, branch, quantity, createdAt.
 */
@SpringBootTest // Loads full app context including Spring Data REST endpoints
@AutoConfigureMockMvc // Enables MockMvc for API endpoint testing
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Uses real MySQL DB
class VirtualGoldHoldingsApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VirtualGoldHoldingsRepository virtualGoldHoldingsRepository;

    /**
     * Test: GET /virtual-gold-holdings
     * Checks whether collection endpoint is exposed.
     */
    @Test
    void getAllVirtualGoldHoldings_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/virtual-gold-holdings"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links").exists());
    }

    /**
     * Test: GET /virtual-gold-holdings/{id}
     * Fetches an existing holding dynamically from DB.
     */
    @Test
    void getVirtualGoldHoldingById_shouldReturnExistingHolding() throws Exception {

        VirtualGoldHoldings holding = virtualGoldHoldingsRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        mockMvc.perform(get("/virtual-gold-holdings/{id}", holding.getHoldingId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").exists())
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    /**
     * Test: GET /virtual-gold-holdings/{id}
     * Invalid ID should return 404.
     */
    @Test
    void getVirtualGoldHoldingById_shouldReturn404ForInvalidId() throws Exception {
        mockMvc.perform(get("/virtual-gold-holdings/{id}", 999999))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    /**
     * Test: GET /virtual-gold-holdings/search/by-branch?branchId=...
     * Spring Data REST exposes custom finder methods under /search.
     */
    @Test
    void findByBranchId_shouldReturnHoldingsForExistingBranch() throws Exception {

        VirtualGoldHoldings holding = virtualGoldHoldingsRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        Integer branchId = holding.getBranch().getBranchId();

        mockMvc.perform(get("/virtual-gold-holdings/search/by-branch")
                        .param("branchId", branchId.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.virtualGoldHoldings.length()")
                        .value(greaterThanOrEqualTo(1)));
    }

    /**
     * Test: Invalid branch ID.
     * Since finder returns List, API should return 200 with empty result.
     */
    @Test
    void findByBranchId_shouldReturnEmptyForInvalidBranch() throws Exception {
        mockMvc.perform(get("/virtual-gold-holdings/search/by-branch")
                        .param("branchId", "999999"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links").exists());
    }

    /**
     * Test: GET /virtual-gold-holdings/search
     * Confirms custom search endpoint is exposed.
     */
    @Test
    void searchEndpoint_shouldExposeByBranchFinder() throws Exception {
        mockMvc.perform(get("/virtual-gold-holdings/search"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.by-branch").exists());
    }
}