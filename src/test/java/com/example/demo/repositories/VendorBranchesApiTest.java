package com.example.demo.repositories;

import com.example.demo.model.VendorBranches;
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
class VendorBranchesApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VendorBranchesRepository vendorBranchesRepository;

    @Test
    void getAllVendorBranches_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/vendor_branches"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links").exists());
    }

    @Test
    void getVendorBranchById_shouldReturnExistingBranch() throws Exception {
        VendorBranches branch = vendorBranchesRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        mockMvc.perform(get("/vendor_branches/{id}", branch.getBranchId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").exists())
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void getVendorBranchById_shouldReturn404ForInvalidId() throws Exception {
        mockMvc.perform(get("/vendor_branches/{id}", 999999))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void findByVendorVendorId_shouldReturnOk() throws Exception {
        VendorBranches branch = vendorBranchesRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        Integer vendorId = branch.getVendor().getVendorId();

        mockMvc.perform(get("/vendor_branches/search/by_vendor")
                        .param("vendorId", vendorId.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links").exists());
    }

    @Test
    void findByVendorVendorId_shouldReturnOkForInvalidVendor() throws Exception {
        mockMvc.perform(get("/vendor_branches/search/by_vendor")
                        .param("vendorId", "999999"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links").exists());
    }

    @Test
    void findByAddressCity_shouldReturnOk() throws Exception {
        VendorBranches branch = vendorBranchesRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        String city = branch.getAddress().getCity();

        mockMvc.perform(get("/vendor_branches/search/by_city")
                        .param("city", city))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links").exists());
    }

    @Test
    void findByAddressCity_shouldReturnOkForInvalidCity() throws Exception {
        mockMvc.perform(get("/vendor_branches/search/by_city")
                        .param("city", "InvalidCity"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links").exists());
    }

    @Test
    void findByAddressState_shouldReturnOk() throws Exception {
        VendorBranches branch = vendorBranchesRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        String state = branch.getAddress().getState();

        mockMvc.perform(get("/vendor_branches/search/by_state")
                        .param("state", state))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links").exists());
    }

    @Test
    void findByAddressState_shouldReturnOkForInvalidState() throws Exception {
        mockMvc.perform(get("/vendor_branches/search/by_state")
                        .param("state", "InvalidState"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links").exists());
    }
}