package com.example.demo.apiendpoint;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionHistoryApiTest {

    @Autowired
    private MockMvc mockMvc;

    // --- GET /transaction_history (findAll) ---

    @Test
    void getAllTransactions_returns200() throws Exception {
        mockMvc.perform(get("/transaction_history"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllTransactions_returnsHalJson() throws Exception {
        mockMvc.perform(get("/transaction_history"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"));
    }

    @Test
    void getAllTransactions_responseContainsEmbedded() throws Exception {
        mockMvc.perform(get("/transaction_history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.transactionHistories").exists());
    }

    @Test
    void getAllTransactions_responseContainsPageMetadata() throws Exception {
        mockMvc.perform(get("/transaction_history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$.page.totalElements").isNumber());
    }

    @Test
    void getAllTransactions_responseContainsSelfLink() throws Exception {
        mockMvc.perform(get("/transaction_history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self").exists());
    }

    // --- GET /transaction_history/{id} (findById) ---

    @Test
    void getTransactionById_returns200ForExistingId() throws Exception {
        mockMvc.perform(get("/transaction_history/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getTransactionById_returnsCorrectFields() throws Exception {
        mockMvc.perform(get("/transaction_history/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").exists())
                .andExpect(jsonPath("$.quantity").exists())
                .andExpect(jsonPath("$.transactionType").exists())
                .andExpect(jsonPath("$.transactionStatus").exists());
    }

    @Test
    void getTransactionById_returnsLinksInResponse() throws Exception {
        mockMvc.perform(get("/transaction_history/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void getTransactionById_returns404ForNonExistentId() throws Exception {
        mockMvc.perform(get("/transaction_history/999999"))
                .andExpect(status().isNotFound());
    }

    // --- GET /transaction_history/search/findByUserId ---

    @Test
    void findByUserId_returns200ForExistingUser() throws Exception {
        mockMvc.perform(get("/transaction_history/search/findByUserId")
                        .param("userId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void findByUserId_responseContainsEmbedded() throws Exception {
        mockMvc.perform(get("/transaction_history/search/findByUserId")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.transactionHistories").exists());
    }

    @Test
    void findByUserId_returnsEmptyForNonExistentUser() throws Exception {
        mockMvc.perform(get("/transaction_history/search/findByUserId")
                        .param("userId", "999999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.transactionHistories").isEmpty());
    }

    // --- GET /transaction_history/search/findByTransactionStatus ---

    @Test
    void findByTransactionStatus_returns200ForExistingStatus() throws Exception {
        mockMvc.perform(get("/transaction_history/search/findByTransactionStatus")
                        .param("status", "Success"))
                .andExpect(status().isOk());
    }

    @Test
    void findByTransactionStatus_responseContainsEmbedded() throws Exception {
        mockMvc.perform(get("/transaction_history/search/findByTransactionStatus")
                        .param("status", "Success"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.transactionHistories").exists());
    }

    @Test
    void findByTransactionStatus_returnsEmptyForNonExistentStatus() throws Exception {
        mockMvc.perform(get("/transaction_history/search/findByTransactionStatus")
                        .param("status", "NonExistentStatus999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.transactionHistories").isEmpty());
    }

    // --- GET /transaction_history/search/findByTransactionType ---

    @Test
    void findByTransactionType_returns200ForExistingType() throws Exception {
        mockMvc.perform(get("/transaction_history/search/findByTransactionType")
                        .param("type", "Buy"))
                .andExpect(status().isOk());
    }

    @Test
    void findByTransactionType_responseContainsEmbedded() throws Exception {
        mockMvc.perform(get("/transaction_history/search/findByTransactionType")
                        .param("type", "Buy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.transactionHistories").exists());
    }

    @Test
    void findByTransactionType_returnsEmptyForNonExistentType() throws Exception {
        mockMvc.perform(get("/transaction_history/search/findByTransactionType")
                        .param("type", "NonExistentType999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.transactionHistories").isEmpty());
    }

    // --- existsById (via findById) ---

    @Test
    void existsById_returns200ForExistingId() throws Exception {
        mockMvc.perform(get("/transaction_history/1"))
                .andExpect(status().isOk());
    }

    @Test
    void existsById_returns404ForNonExistentId() throws Exception {
        mockMvc.perform(get("/transaction_history/999999"))
                .andExpect(status().isNotFound());
    }
}