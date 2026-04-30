package com.example.demo.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.demo.model.Addresses;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AddressRepositoryTest {

    @Autowired
    private AddressesRepository addressesRepository;

    // ================== SAVE ==================
    @Test
    void testSaveAddress_success() {

        String unique = String.valueOf(System.currentTimeMillis());

        Addresses address = new Addresses();
        address.setStreet("Street__" + unique);
        address.setCity("Pune_" + unique);
        address.setState("Maharashtra");
        address.setPostalCode("411001_" + unique);
        address.setCountry("India");

        Addresses saved = addressesRepository.save(address);
        addressesRepository.flush();

        assertNotNull(saved.getAddressId());
        assertTrue(saved.getCity().contains("Pune"));
    }

    // ================== FIND BY ID ==================
    @Test
    void testFindById_success() {

        List<Addresses> list = addressesRepository.findAll();
        assertFalse(list.isEmpty());

        Integer id = list.get(0).getAddressId();

        Optional<Addresses> found = addressesRepository.findById(id);

        assertTrue(found.isPresent());
    }

    // ================== FIND ALL ==================
    @Test
    void testFindAll_success() {

        List<Addresses> list = addressesRepository.findAll();

        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

//     ================== UPDATE ==================
    @Test
    void testUpdate_success() {

        List<Addresses> list = addressesRepository.findAll();
        assertFalse(list.isEmpty());

        Addresses existing = list.get(0);
        existing.setCity("UpdatedCity");

        Addresses updated = addressesRepository.save(existing);

        assertEquals("UpdatedCity", updated.getCity());
    }

    // ================== DELETE ==================
//    @Test
//    void testDelete_success() {
//
//        List<Addresses> list = addressesRepository.findAll();
//        assertFalse(list.isEmpty());
//
//        Integer id = list.get(0).getAddressId();
//
//        addressesRepository.deleteById(id);
//        addressesRepository.flush(); // IMPORTANT for real DB
//
//        Optional<Addresses> deleted = addressesRepository.findById(id);
//
//        assertFalse(deleted.isPresent());
//    }

    // ================== FIND BY CITY ==================
    @Test
    void testFindByCity_success() {

        List<Addresses> result = addressesRepository.findByCity("Mumbai");

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    // ================== FIND BY STATE (ADDED) ==================
    @Test
    void testFindByState_success() {

        List<Addresses> result = addressesRepository.findByState("Maharashtra");

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    // ================== FIND BY COUNTRY (ADDED) ==================
    @Test
    void testFindByCountry_success() {

        List<Addresses> result = addressesRepository.findByCountry("India");

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    // ================== FIND BY POSTAL CODE ==================
    @Test
    void testFindByPostalCode_success() {

        List<Addresses> result = addressesRepository.findByPostalCode("400001");

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    // ================== FIND BY STREET CONTAINING (ADDED) ==================
    @Test
    void testFindByStreetContaining_success() {

        List<Addresses> result = addressesRepository.findByStreetContaining("Street");

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    // ================== NEGATIVE ==================

    @Test
    void testFindById_notFound() {

        Optional<Addresses> result = addressesRepository.findById(-1);

        assertFalse(result.isPresent());
    }

//    @Test
//    void testDelete_notExistingId() {
//        assertDoesNotThrow(() -> addressesRepository.deleteById(-1));
//    }

    @Test
    void testFindByCity_notFound() {

        List<Addresses> result =
                addressesRepository.findByCity("CITY_DOES_NOT_EXIST_123");

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByState_notFound() {

        List<Addresses> result =
                addressesRepository.findByState("STATE_DOES_NOT_EXIST_123");

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByCountry_notFound() {

        List<Addresses> result =
                addressesRepository.findByCountry("COUNTRY_DOES_NOT_EXIST_123");

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByPostalCode_notFound() {

        List<Addresses> result =
                addressesRepository.findByPostalCode("000000000");

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByStreetContaining_notFound() {

        List<Addresses> result =
                addressesRepository.findByStreetContaining("ZZZZZZ");

        assertTrue(result.isEmpty());
    }

    // ================== EDGE CASES ==================

    @Test
    void testFindByCity_nullInput() {

        List<Addresses> result = addressesRepository.findByCity(null);

        assertTrue(result.isEmpty());
    }

    @Test
    void testSaveAddress_nullRequiredFields() {

        Addresses address = new Addresses();
        address.setPostalCode("123456");

        assertThrows(Exception.class, () -> {
            addressesRepository.save(address);
            addressesRepository.flush();
        });
    }

    @Test
    void testUpdate_nonExistingId_createsNew() {

        Addresses address = new Addresses();
        address.setAddressId(-1);
        address.setStreet("New Street");
        address.setCity("Jaipur");
        address.setState("Rajasthan");
        address.setPostalCode("302001");
        address.setCountry("India");

        Addresses saved = addressesRepository.save(address);

        assertNotNull(saved.getAddressId());
    }
}