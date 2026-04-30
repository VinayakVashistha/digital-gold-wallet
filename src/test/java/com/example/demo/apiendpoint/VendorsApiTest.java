package com.example.demo.apiendpoint;

import com.example.demo.model.Vendors;
import com.example.demo.repositories.VendorsRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VendorsApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VendorsRepository vendorsRepository;

    @Test
    void getAllVendors_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/vendors"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links").exists());
    }

    @Test
    void getVendorById_shouldReturnExistingVendor() throws Exception {
        Vendors vendor = vendorsRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        mockMvc.perform(get("/vendors/{id}", vendor.getVendorId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendorName").value(vendor.getVendorName()))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void getVendorById_shouldReturn404ForInvalidId() throws Exception {
        mockMvc.perform(get("/vendors/{id}", 999999))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void findByVendorName_shouldReturnExistingVendor() throws Exception {
        Vendors vendor = vendorsRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        mockMvc.perform(get("/vendors/search/findByVendorName")
                        .param("vendorName", vendor.getVendorName()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendorName").value(vendor.getVendorName()))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void findByVendorName_shouldReturn404ForInvalidVendorName() throws Exception {
        mockMvc.perform(get("/vendors/search/findByVendorName")
                        .param("vendorName", "InvalidVendorName"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void searchEndpoint_shouldReturnAvailableSearchLinks() throws Exception {
        mockMvc.perform(get("/vendors/search"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.findByVendorName").exists());
    }
}