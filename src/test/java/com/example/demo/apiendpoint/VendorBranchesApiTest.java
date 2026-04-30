package com.example.demo.apiendpoint;

import com.example.demo.model.VendorBranches;
import com.example.demo.repositories.VendorBranchesRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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

    @Test
    void createVendorBranch_shouldReturnCreated() throws Exception {
        VendorBranches existing = vendorBranchesRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        Integer vendorId = existing.getVendor().getVendorId();
        Integer addressId = existing.getAddress().getAddressId();

        String json = """
                {
                  "quantity": 100.00,
                  "vendor": "/vendors/%d",
                  "address": "/addresses/%d"
                }
                """.formatted(vendorId, addressId);

        mockMvc.perform(post("/vendor_branches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void updateVendorBranch_shouldReturnNoContent() throws Exception {
        VendorBranches branch = vendorBranchesRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        String json = """
                {
                  "quantity": 999.99
                }
                """;

        mockMvc.perform(patch("/vendor_branches/{id}", branch.getBranchId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/vendor_branches/{id}", branch.getBranchId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(999.99));
    }

    @Test
    void createVendorBranch_shouldReturnBadRequestForMalformedJson() throws Exception {
        String malformedJson = """
                {
                  "quantity": 100.00,
                  "vendor": "/vendors/1",
                """;

        mockMvc.perform(post("/vendor_branches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void createVendorBranch_shouldReturnBadRequestForInvalidQuantityType() throws Exception {
        VendorBranches existing = vendorBranchesRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        Integer vendorId = existing.getVendor().getVendorId();
        Integer addressId = existing.getAddress().getAddressId();

        String json = """
                {
                  "quantity": "wrong-value",
                  "vendor": "/vendors/%d",
                  "address": "/addresses/%d"
                }
                """.formatted(vendorId, addressId);

        mockMvc.perform(post("/vendor_branches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateVendorBranch_shouldReturnNotFoundForInvalidId() throws Exception {
        String json = """
                {
                  "quantity": 500.00
                }
                """;

        mockMvc.perform(patch("/vendor_branches/{id}", 999999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void updateVendorBranch_shouldReturnBadRequestForMalformedJson() throws Exception {
        VendorBranches branch = vendorBranchesRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        String malformedJson = """
                {
                  "quantity": 500.00,
                """;

        mockMvc.perform(patch("/vendor_branches/{id}", branch.getBranchId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateVendorBranch_shouldReturnBadRequestForInvalidQuantityType() throws Exception {
        VendorBranches branch = vendorBranchesRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        String json = """
                {
                  "quantity": "wrong-value"
                }
                """;

        mockMvc.perform(patch("/vendor_branches/{id}", branch.getBranchId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}