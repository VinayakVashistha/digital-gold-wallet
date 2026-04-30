package com.example.demo.repositories;

import com.example.demo.model.VendorBranches;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VendorBranchesRepositoryTest {

    @Autowired
    private VendorBranchesRepository vendorBranchesRepository;

    // --- findAll ---

    @Test
    void findAll_shouldReturnNonEmptyList() {
        List<VendorBranches> result = vendorBranchesRepository.findAll();
        assertThat(result).isNotEmpty();
    }

    @Test
    void findAll_shouldReturnBranchesWithVendorAndAddress() {
        List<VendorBranches> result = vendorBranchesRepository.findAll();

        assertThat(result).isNotEmpty();
        assertThat(result)
                .allMatch(b -> b.getVendor() != null && b.getAddress() != null);
    }

    // --- findById ---

    @Test
    void findById_shouldReturnBranchForExistingId() {

        VendorBranches branch = vendorBranchesRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No data found"));

        assertThat(vendorBranchesRepository.findById(branch.getBranchId()))
                .isPresent();
    }

    @Test
    void findById_shouldReturnEmptyForInvalidId() {
        assertThat(vendorBranchesRepository.findById(999999)).isEmpty();
    }

    // --- findByVendorVendorId ---

    @Test
    void findByVendorVendorId_shouldReturnCorrectBranches() {

        VendorBranches branch = vendorBranchesRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        Integer vendorId = branch.getVendor().getVendorId();

        List<VendorBranches> result =
                vendorBranchesRepository.findByVendorVendorId(vendorId);

        assertThat(result).isNotEmpty();
        assertThat(result)
                .allMatch(b -> b.getVendor().getVendorId().equals(vendorId));
    }

    @Test
    void findByVendorVendorId_shouldReturnEmptyForInvalidVendor() {
        List<VendorBranches> result =
                vendorBranchesRepository.findByVendorVendorId(999999);

        assertThat(result).isEmpty();
    }

    // --- findByAddressCity ---

    @Test
    void findByAddressCity_shouldReturnCorrectCityBranches() {

        VendorBranches branch = vendorBranchesRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        String city = branch.getAddress().getCity();

        List<VendorBranches> result =
                vendorBranchesRepository.findByAddressCity(city);

        assertThat(result).isNotEmpty();
        assertThat(result)
                .allMatch(b -> b.getAddress().getCity().equalsIgnoreCase(city));
    }

    @Test
    void findByAddressCity_shouldReturnEmptyForInvalidCity() {
        List<VendorBranches> result =
                vendorBranchesRepository.findByAddressCity("InvalidCity");

        assertThat(result).isEmpty();
    }

    // --- findByAddressState ---

    @Test
    void findByAddressState_shouldReturnCorrectStateBranches() {

        VendorBranches branch = vendorBranchesRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        String state = branch.getAddress().getState();

        List<VendorBranches> result =
                vendorBranchesRepository.findByAddressState(state);

        assertThat(result).isNotEmpty();
        assertThat(result)
                .allMatch(b -> b.getAddress().getState().equalsIgnoreCase(state));
    }

    // --- save (INSERT) ---

    @Test
    void save_shouldPersistNewBranch() {

        VendorBranches existing = vendorBranchesRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        VendorBranches newBranch = new VendorBranches();
        newBranch.setVendor(existing.getVendor());
        newBranch.setAddress(existing.getAddress());
        newBranch.setQuantity(existing.getQuantity());

        VendorBranches saved = vendorBranchesRepository.save(newBranch);

        assertThat(saved.getBranchId()).isNotNull();
        assertThat(vendorBranchesRepository.findById(saved.getBranchId()))
                .isPresent();
    }

    // --- save (UPDATE) ---

    @Test
    void save_shouldUpdateExistingBranch() {

        VendorBranches branch = vendorBranchesRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        BigDecimal newQuantity = branch.getQuantity().add(new BigDecimal("10"));

        branch.setQuantity(newQuantity);
        vendorBranchesRepository.save(branch);

        VendorBranches updated = vendorBranchesRepository.findById(branch.getBranchId())
                .orElseThrow();

        assertThat(updated.getQuantity()).isEqualByComparingTo(newQuantity);
    }

    // --- count ---

    @Test
    void count_shouldBeGreaterThanZero() {
        assertThat(vendorBranchesRepository.count()).isGreaterThan(0);
    }

    // --- existsById ---

    @Test
    void existsById_shouldWork() {

        VendorBranches branch = vendorBranchesRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        assertThat(vendorBranchesRepository.existsById(branch.getBranchId()))
                .isTrue();
    }
}