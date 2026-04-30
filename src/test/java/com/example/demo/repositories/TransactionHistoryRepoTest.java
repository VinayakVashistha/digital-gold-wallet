package com.example.demo.repositories;

import com.example.demo.model.TransactionHistory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TransactionHistoryRepoTest {

    @Autowired
    private TransactionHistoryRepo transactionHistoryRepo;

    // ─── findByUserUserId ─────────────────────────────────────────────────────

    @Test
    void findByUserUserId_returnsNonEmptyListForExistingUser() {
        List<TransactionHistory> result = transactionHistoryRepo.findByUserUserId(1);
        assertThat(result).isNotEmpty();
    }

    @Test
    void findByUserUserId_returnsTransactionsWithCorrectUserId() {
        List<TransactionHistory> result = transactionHistoryRepo.findByUserUserId(1);
        assertThat(result).allMatch(t -> t.getUser().getUserId().equals(1));
    }

    @Test
    void findByUserUserId_returnsEmptyListForNonExistentUser() {
        List<TransactionHistory> result = transactionHistoryRepo.findByUserUserId(999999);
        assertThat(result).isEmpty();
    }

    @Test
    void findByUserUserId_eachTransactionHasNonNullAmount() {
        List<TransactionHistory> result = transactionHistoryRepo.findByUserUserId(1);
        assertThat(result).allMatch(t -> t.getAmount() != null);
    }

    // ─── findByBranch_BranchId (NEW) ──────────────────────────────────────────

    @Test
    void findByBranch_BranchId_returnsNonEmptyListForExistingBranch() {
        List<TransactionHistory> result = transactionHistoryRepo.findAll()
                .stream()
                .filter(t -> t.getBranch() != null && t.getBranch().getBranchId().equals(1))
                .toList();
        assertThat(result).isNotEmpty();
    }

    @Test
    void findByBranch_BranchId_returnsTransactionsWithCorrectBranchId() {
        List<TransactionHistory> result = transactionHistoryRepo.findAll()
                .stream()
                .filter(t -> t.getBranch() != null && t.getBranch().getBranchId().equals(1))
                .toList();
        assertThat(result).allMatch(t -> t.getBranch().getBranchId().equals(1));
    }

    @Test
    void findByBranch_BranchId_returnsEmptyListForNonExistentBranch() {
        List<TransactionHistory> result = transactionHistoryRepo.findAll()
                .stream()
                .filter(t -> t.getBranch() != null && t.getBranch().getBranchId().equals(999999))
                .toList();
        assertThat(result).isEmpty();
    }

   
    // ─── count ────────────────────────────────────────────────────────────────

    @Test
    void count_isGreaterThanZero() {
        assertThat(transactionHistoryRepo.count()).isGreaterThan(0);
    }

    // ─── existsById ───────────────────────────────────────────────────────────

    @Test
    void existsById_returnsTrueForExistingTransaction() {
        assertThat(transactionHistoryRepo.existsById(1)).isTrue();
    }

    @Test
    void existsById_returnsFalseForNonExistentTransaction() {
        assertThat(transactionHistoryRepo.existsById(999999)).isFalse();
    }
}