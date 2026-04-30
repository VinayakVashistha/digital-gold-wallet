package com.example.demo.repositories;

import com.example.demo.model.Vendors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VendorsRepositoryTest {

    @Autowired
    private VendorsRepository vendorsRepository;

    // --- findAll ---

    @Test
    void findAll_shouldReturnNonEmptyList() {
        List<Vendors> result = vendorsRepository.findAll();
        assertThat(result).isNotEmpty();
    }

    // --- findById ---

    @Test
    void findById_shouldReturnVendorForValidId() {

        Vendors vendor = vendorsRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        Optional<Vendors> result =
                vendorsRepository.findById(vendor.getVendorId());

        assertThat(result).isPresent();
    }

    @Test
    void findById_shouldReturnEmptyForInvalidId() {
        assertThat(vendorsRepository.findById(999999)).isEmpty();
    }

    // --- findByVendorName (MAIN METHOD) ---

    @Test
    void findByVendorName_shouldReturnCorrectVendor() {

        Vendors vendor = vendorsRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        String name = vendor.getVendorName();

        Vendors result =
                vendorsRepository.findByVendorName(name);

        assertThat(result).isNotNull();
        assertThat(result.getVendorName()).isEqualTo(name);
    }

    @Test
    void findByVendorName_shouldReturnNullForInvalidName() {

        Vendors result =
                vendorsRepository.findByVendorName("InvalidVendorName");

        assertThat(result).isNull();
    }
}