package com.example.demo.apiendpoint;

import com.example.demo.repositories.PhysicalGoldTransactionsRepo;

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
class PhysicalGoldTransactionApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PhysicalGoldTransactionsRepo repo;

    // =========================================================================
    // SEARCH ENDPOINTS
    // =========================================================================

    @Test
    void searchEndpoint_shouldExposeMethods() throws Exception {
        mockMvc.perform(get("/physical_gold_transactions/search"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.findByUserId").exists())
                .andExpect(jsonPath("$._links.findByBranchId").exists());
    }

    // =========================================================================
    // findByUserId
    // =========================================================================

    @Test
    void findByUserId_shouldReturnResults() throws Exception {
        mockMvc.perform(get("/physical_gold_transactions/search/findByUserId")
                        .param("userId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").isArray());
    }

    @Test
    void findByUserId_shouldReturnEmpty_whenNotFound() throws Exception {
        mockMvc.perform(get("/physical_gold_transactions/search/findByUserId")
                        .param("userId", "999999"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").isEmpty());
    }

    // =========================================================================
    // findByBranchId
    // =========================================================================

    @Test
    void findByBranchId_shouldReturnResults() throws Exception {
        mockMvc.perform(get("/physical_gold_transactions/search/findByBranchId")
                        .param("branchId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").isArray());
    }

    @Test
    void findByBranchId_shouldReturnEmpty_whenNotFound() throws Exception {
        mockMvc.perform(get("/physical_gold_transactions/search/findByBranchId")
                        .param("branchId", "999999"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").isEmpty());
    }
}