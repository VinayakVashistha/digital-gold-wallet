package com.example.demo.repositories;

import com.example.demo.model.PhysicalGoldTransactions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PhysicalGoldTransactionRepoTest {

    @Autowired
    private PhysicalGoldTransactionsRepo physicalGoldTransactionsRepo;

    // ─── findByUser_UserId ────────────────────────────────────────────────────

    @Test
    void findByUser_UserId_returnsNonEmptyListForExistingUser() {
        List<PhysicalGoldTransactions> result = physicalGoldTransactionsRepo.findByUser_UserId(1);
        assertThat(result).isNotEmpty();
    }

    @Test
    void findByUser_UserId_returnsTransactionsWithCorrectUserId() {
        List<PhysicalGoldTransactions> result = physicalGoldTransactionsRepo.findByUser_UserId(1);
        assertThat(result).allMatch(t -> t.getUser().getUserId().equals(1));
    }

    @Test
    void findByUser_UserId_returnsEmptyListForNonExistentUser() {
        List<PhysicalGoldTransactions> result = physicalGoldTransactionsRepo.findByUser_UserId(999999);
        assertThat(result).isEmpty();
    }

    @Test
    void findByUser_UserId_eachTransactionHasNonNullQuantity() {
        List<PhysicalGoldTransactions> result = physicalGoldTransactionsRepo.findByUser_UserId(1);
        assertThat(result).allMatch(t -> t.getQuantity() != null);
    }

    @Test
    void findByUser_UserId_eachTransactionHasNonNullBranch() {
        List<PhysicalGoldTransactions> result = physicalGoldTransactionsRepo.findByUser_UserId(1);
        assertThat(result).allMatch(t -> t.getBranch() != null);
    }

    // ─── findByBranch_BranchId ────────────────────────────────────────────────

    @Test
    void findByBranch_BranchId_returnsNonEmptyListForExistingBranch() {
        List<PhysicalGoldTransactions> result = physicalGoldTransactionsRepo.findByBranch_BranchId(1);
        assertThat(result).isNotEmpty();
    }

    @Test
    void findByBranch_BranchId_returnsTransactionsWithCorrectBranchId() {
        List<PhysicalGoldTransactions> result = physicalGoldTransactionsRepo.findByBranch_BranchId(1);
        assertThat(result).allMatch(t -> t.getBranch().getBranchId().equals(1));
    }

    @Test
    void findByBranch_BranchId_returnsEmptyListForNonExistentBranch() {
        List<PhysicalGoldTransactions> result = physicalGoldTransactionsRepo.findByBranch_BranchId(999999);
        assertThat(result).isEmpty();
    }

    @Test
    void findByBranch_BranchId_eachTransactionHasNonNullQuantity() {
        List<PhysicalGoldTransactions> result = physicalGoldTransactionsRepo.findByBranch_BranchId(1);
        assertThat(result).allMatch(t -> t.getQuantity() != null);
    }

    @Test
    void findByBranch_BranchId_eachTransactionHasNonNullUser() {
        List<PhysicalGoldTransactions> result = physicalGoldTransactionsRepo.findByBranch_BranchId(1);
        assertThat(result).allMatch(t -> t.getUser() != null);
    }


    // ─── count ────────────────────────────────────────────────────────────────

    @Test
    void count_isGreaterThanZero() {
        assertThat(physicalGoldTransactionsRepo.count()).isGreaterThan(0);
    }

    // ─── existsById ───────────────────────────────────────────────────────────

    @Test
    void existsById_returnsTrueForExistingTransaction() {
        assertThat(physicalGoldTransactionsRepo.existsById(1)).isTrue();
    }

    @Test
    void existsById_returnsFalseForNonExistentTransaction() {
        assertThat(physicalGoldTransactionsRepo.existsById(999999)).isFalse();
    }
}