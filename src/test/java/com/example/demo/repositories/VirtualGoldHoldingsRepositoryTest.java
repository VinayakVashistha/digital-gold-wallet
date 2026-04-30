package com.example.demo.repositories;

import com.example.demo.model.VirtualGoldHoldings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VirtualGoldHoldingsRepositoryTest {

    @Autowired
    private VirtualGoldHoldingsRepository virtualGoldHoldingsRepository;

    @Test
    void findByBranch_BranchId_returnsNonEmptyListForExistingBranchId() {
        List<VirtualGoldHoldings> result = virtualGoldHoldingsRepository.findByBranch_BranchId(1);
        assertThat(result).isNotEmpty();
    }

    @Test
    void findByBranch_BranchId_returnsEmptyForNonExistentBranchId() {
        List<VirtualGoldHoldings> result = virtualGoldHoldingsRepository.findByBranch_BranchId(999999);
        assertThat(result).isEmpty();
    }
    
    @Test
    void findByBranch_BranchId_returnsOnlyMatchingRecords() {
        List<VirtualGoldHoldings> result =
                virtualGoldHoldingsRepository.findByBranch_BranchId(1);
        assertThat(result)
                .allMatch(h -> h.getBranch().getBranchId().equals(1));
    }
}