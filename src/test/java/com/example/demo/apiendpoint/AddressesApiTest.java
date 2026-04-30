package com.example.demo.apiendpoint;

import com.example.demo.model.Addresses;
import com.example.demo.repositories.AddressesRepository;

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
 * API Test class for Addresses using MockMvc.
 * Tests Spring Data REST auto-generated endpoints.
 */
@SpringBootTest // loads full application context (required for REST endpoints)
@AutoConfigureMockMvc // enables MockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // use real DB
class AddressesApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AddressesRepository addressesRepository;

    /**
     * Test: GET /address
     * Verify all addresses endpoint works.
     */
    @Test
    void getAllAddresses_shouldReturnList() throws Exception {
        mockMvc.perform(get("/address"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links").exists());
    }

    /**
     * Test: GET /address/{id}
     * Fetch existing address dynamically from DB.
     */
    @Test
    void getAddressById_shouldReturnAddress() throws Exception {

        Addresses address = addressesRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        mockMvc.perform(get("/address/{id}", address.getAddressId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value(address.getCity()))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    /**
     * Test: invalid ID → should return 404
     */
    @Test
    void getAddressById_invalidId_shouldReturn404() throws Exception {
        mockMvc.perform(get("/address/{id}", 999999))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    /**
     * Test: findByCity
     * Endpoint: /address/search/findByCity?city=...
     */
    @Test
    void findByCity_shouldReturnAddresses() throws Exception {

        Addresses address = addressesRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        String city = address.getCity();

        mockMvc.perform(get("/address/search/findByCity")
                        .param("city", city))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.address.length()")
                        .value(greaterThanOrEqualTo(1)));
    }

    /**
     * Test: findByState
     */
    @Test
    void findByState_shouldReturnAddresses() throws Exception {

        Addresses address = addressesRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        String state = address.getState();

        mockMvc.perform(get("/address/search/findByState")
                        .param("state", state))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.address.length()")
                        .value(greaterThanOrEqualTo(1)));
    }

    /**
     * Test: findByCountry
     */
    @Test
    void findByCountry_shouldReturnAddresses() throws Exception {

        Addresses address = addressesRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        String country = address.getCountry();

        mockMvc.perform(get("/address/search/findByCountry")
                        .param("country", country))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.address.length()")
                        .value(greaterThanOrEqualTo(1)));
    }

    /**
     * Test: findByPostalCode
     */
    @Test
    void findByPostalCode_shouldReturnAddresses() throws Exception {

        Addresses address = addressesRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        String postalCode = address.getPostalCode();

        mockMvc.perform(get("/address/search/findByPostalCode")
                        .param("postalCode", postalCode))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.address.length()")
                        .value(greaterThanOrEqualTo(1)));
    }

    /**
     * Test: findByStreetContaining
     */
    @Test
    void findByStreetContaining_shouldReturnAddresses() throws Exception {

        Addresses address = addressesRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        String street = address.getStreet();

        mockMvc.perform(get("/address/search/findByStreetContaining")
                        .param("street", street))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.address.length()")
                        .value(greaterThanOrEqualTo(1)));
    }

    /**
     * Test: invalid city → should return empty result
     */
    @Test
    void findByCity_invalid_shouldReturnEmpty() throws Exception {
        mockMvc.perform(get("/address/search/findByCity")
                        .param("city", "InvalidCity"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}