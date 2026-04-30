package com.example.demo.repositories;

import com.example.demo.model.Users;
import com.example.demo.model.Addresses;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UsersRepoTest {

    @Autowired
    private UsersRepo usersRepo;

    // --- findByName ---

    @Test
    void findByName_returnsNonEmptyListForExistingName() {
        List<Users> result = usersRepo.findByName("Aman Gupta");
        assertThat(result).isNotEmpty();
    }

    @Test
    void findByName_returnsUsersWithCorrectName() {
        List<Users> result = usersRepo.findByName("Aman Gupta");
        assertThat(result).allMatch(u -> u.getName().equals("Aman Gupta"));
    }

    @Test
    void findByName_returnsEmptyForNonExistentName() {
        List<Users> result = usersRepo.findByName("NonExistentName999");
        assertThat(result).isEmpty();
    }

    // --- findByAddress_City ---

    @Test
    void findByAddress_City_returnsNonEmptyListForExistingCity() {
        List<Users> result = usersRepo.findByAddress_City("Mumbai");
        assertThat(result).isNotEmpty();
    }

    @Test
    void findByAddress_City_returnsUsersFromCorrectCity() {
        List<Users> result = usersRepo.findByAddress_City("Mumbai");
        assertThat(result).allMatch(u -> u.getAddress().getCity().equals("Mumbai"));
    }

    @Test
    void findByAddress_City_returnsEmptyForNonExistentCity() {
        List<Users> result = usersRepo.findByAddress_City("NonExistentCity999");
        assertThat(result).isEmpty();
    }

    // --- findByAddress_State ---

    @Test
    void findByAddress_State_returnsNonEmptyListForExistingState() {
        List<Users> result = usersRepo.findByAddress_State("Maharashtra");
        assertThat(result).isNotEmpty();
    }

    @Test
    void findByAddress_State_returnsUsersFromCorrectState() {
        List<Users> result = usersRepo.findByAddress_State("Maharashtra");
        assertThat(result).allMatch(u -> u.getAddress().getState().equals("Maharashtra"));
    }

    @Test
    void findByAddress_State_returnsEmptyForNonExistentState() {
        List<Users> result = usersRepo.findByAddress_State("NonExistentState999");
        assertThat(result).isEmpty();
    }

    // --- save (NEW TESTS) ---

    @Test
    void save_createsNewUser() {
        Addresses address = new Addresses();
        address.setCity("Delhi");
        address.setState("Delhi");
        address.setCountry("India");
        address.setStreet("Test Street");

        Users user = new Users();
        user.setName("Test User");
        user.setEmail("testuser123@gmail.com");
        user.setAddresses(address);

        Users savedUser = usersRepo.save(user);

        assertThat(savedUser.getUserId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("Test User");
    }

    @Test
    void save_updatesExistingUser() {
        Users user = usersRepo.findById(1).orElse(null);
        assertThat(user).isNotNull();

        user.setName("Updated Name");
        Users updatedUser = usersRepo.save(user);

        assertThat(updatedUser.getName()).isEqualTo("Updated Name");
    }

    // --- findAll ---

    @Test
    void findAll_returnsNonEmptyList() {
        List<Users> result = usersRepo.findAll();
        assertThat(result).isNotEmpty();
    }

    @Test
    void findAll_eachUserHasNonNullEmail() {
        List<Users> result = usersRepo.findAll();
        assertThat(result).allMatch(u -> u.getEmail() != null);
    }

    @Test
    void findAll_eachUserHasNonNullName() {
        List<Users> result = usersRepo.findAll();
        assertThat(result).allMatch(u -> u.getName() != null);
    }

    // --- findById ---

    @Test
    void findById_returnsUserForExistingId() {
        assertThat(usersRepo.findById(1)).isPresent();
    }

    @Test
    void findById_returnsEmptyForNonExistentId() {
        assertThat(usersRepo.findById(999999)).isEmpty();
    }

    // --- count ---

    @Test
    void count_isGreaterThanZero() {
        assertThat(usersRepo.count()).isGreaterThan(0);
    }

    // --- existsById ---

    @Test
    void existsById_returnsTrueForExistingUser() {
        assertThat(usersRepo.existsById(1)).isTrue();
    }

    @Test
    void existsById_returnsFalseForNonExistentUser() {
        assertThat(usersRepo.existsById(999999)).isFalse();
    }
}