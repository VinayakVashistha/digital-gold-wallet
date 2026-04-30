package com.example.demo.repositories;

import com.example.demo.model.Payments;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PaymentsRepositoryTest {

    @Autowired
    private PaymentsRepository paymentsRepository;

    // --- findByUser_UserId ---

    @Test
    void findByUser_UserId_returnsNonEmptyListForExistingUser() {
        List<Payments> result = paymentsRepository.findByUser_UserId(1);
        assertThat(result).isNotEmpty();
    }

    @Test
    void findByUser_UserId_returnsPaymentsBelongingToCorrectUser() {
        List<Payments> result = paymentsRepository.findByUser_UserId(1);
        assertThat(result).allMatch(p -> p.getUser().getUserId().equals(1));
    }

    @Test
    void findByUser_UserId_returnsEmptyForNonExistentUser() {
        List<Payments> result = paymentsRepository.findByUser_UserId(999999);
        assertThat(result).isEmpty();
    }

    // --- findAll ---

    @Test
    void findAll_returnsNonEmptyList() {
        List<Payments> result = paymentsRepository.findAll();
        assertThat(result).isNotEmpty();
    }

    @Test
    void findAll_eachPaymentHasNonNullAmount() {
        List<Payments> result = paymentsRepository.findAll();
        assertThat(result).allMatch(p -> p.getAmount() != null);
    }

    @Test
    void findAll_eachPaymentHasAssociatedUser() {
        List<Payments> result = paymentsRepository.findAll();
        assertThat(result).allMatch(p -> p.getUser() != null);
    }

    // --- findById ---

    @Test
    void findById_returnsPaymentForExistingId() {
        assertThat(paymentsRepository.findById(1)).isPresent();
    }

    @Test
    void findById_returnsEmptyForNonExistentId() {
        assertThat(paymentsRepository.findById(999999)).isEmpty();
    }

    // --- count ---

    @Test
    void count_isGreaterThanZero() {
        assertThat(paymentsRepository.count()).isGreaterThan(0);
    }

    // --- existsById ---

    @Test
    void existsById_returnsTrueForExistingPayment() {
        assertThat(paymentsRepository.existsById(1)).isTrue();
    }

    @Test
    void existsById_returnsFalseForNonExistentPayment() {
        assertThat(paymentsRepository.existsById(999999)).isFalse();
    }
}