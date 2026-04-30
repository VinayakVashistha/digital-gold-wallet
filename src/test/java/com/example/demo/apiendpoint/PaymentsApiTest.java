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
class PaymentsApiTest {

    @Autowired
    private MockMvc mockMvc;

    // --- GET /payments (findAll) ---

    @Test
    void getAllPayments_returns200() throws Exception {
        mockMvc.perform(get("/payments"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllPayments_returnsHalJson() throws Exception {
        mockMvc.perform(get("/payments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"));
    }

    @Test
    void getAllPayments_responseContainsEmbeddedPayments() throws Exception {
        mockMvc.perform(get("/payments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.paymentses").exists());
    }

    @Test
    void getAllPayments_responseContainsPageMetadata() throws Exception {
        mockMvc.perform(get("/payments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$.page.totalElements").isNumber());
    }

    // --- GET /payments/{id} (findById) ---

    @Test
    void getPaymentById_returns200ForExistingId() throws Exception {
        mockMvc.perform(get("/payments/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getPaymentById_returnsCorrectFields() throws Exception {
        mockMvc.perform(get("/payments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").exists())
                .andExpect(jsonPath("$.paymentMethod").exists())
                .andExpect(jsonPath("$.paymentStatus").exists())
                .andExpect(jsonPath("$.transactionType").exists());
    }

    @Test
    void getPaymentById_returns404ForNonExistentId() throws Exception {
        mockMvc.perform(get("/payments/999999"))
                .andExpect(status().isNotFound());
    }

    // --- GET /payments/search/findByUserid (findByUser_UserId) ---

    @Test
    void findByUserId_returns200ForExistingUser() throws Exception {
        mockMvc.perform(get("/payments/search/findByUserid")
                        .param("userId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void findByUserId_returnsEmbeddedPayments() throws Exception {
        mockMvc.perform(get("/payments/search/findByUserid")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.paymentses").exists());
    }

    @Test
    void findByUserId_returnsEmptyForNonExistentUser() throws Exception {
        mockMvc.perform(get("/payments/search/findByUserid")
                        .param("userId", "999999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.paymentses").isEmpty());
    }
}