package com.joao.doit.repositories;

import com.joao.doit.domain.user.RegisterRequestDTO;
import com.joao.doit.domain.user.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("Should get a User by Email successfully from DB")
    void findByEmailCase1() {
        String email = "joaovkt.novais@gmail.com";
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO(
                "João Victor",
                "Novais",
                email,
                "123456"
        );
        User user = createUser(registerRequestDTO);

        Optional<User> result = userRepository.findByEmail(email);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(user);
    }

    @Test
    @DisplayName("Should not find a User by Email from DB")
    void findByEmailCase2() {
        String email = "joaovkt.novais@gmail.com";
        Optional<User> result = userRepository.findByEmail(email);

        assertThat(result).isEmpty();
    }

    private User createUser(RegisterRequestDTO registerRequestDTO) {
        User user = new User(registerRequestDTO);
        entityManager.persist(user);
        return user;
    }
}