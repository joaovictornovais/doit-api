package com.joao.doit.repositories;

import com.joao.doit.domain.task.Task;
import com.joao.doit.domain.task.TaskPriority;
import com.joao.doit.domain.task.TaskRequestDTO;
import com.joao.doit.domain.task.TaskStatus;
import com.joao.doit.domain.user.RegisterRequestDTO;
import com.joao.doit.domain.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TaskRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    TaskRepository taskRepository;

    @Test
    @DisplayName("Should find User's Task list successfully from DB")
    void findByUserCase1() {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO(
                "João Victor",
                "Novais",
                "joaovkt.novais@gmail.com",
                "123456"
        );
        TaskRequestDTO taskRequestDTO = new TaskRequestDTO(
                "Unit tests",
                "Create unit tests for DoIt-API",
                TaskPriority.HIGH,
                TaskStatus.DOING,
                LocalDateTime.now().plusDays(3)
        );
        User user = createUser(registerRequestDTO);
        Task task = createTask(taskRequestDTO, user);

        List<Task> result = taskRepository.findByUser(user);

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.contains(task)).isTrue();
    }

    @Test
    @DisplayName("Should throw an exception when User not exists")
    void findByUserCase2() {
        Exception thrown = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            try {
                taskRepository.findByUser(new User());
            } catch (InvalidDataAccessApiUsageException e) {
                throw new EntityNotFoundException("User not found");
            }
        });

        assertThat(thrown.getMessage()).isEqualTo("User not found");
    }

    private User createUser(RegisterRequestDTO registerRequestDTO) {
        User user = new User(registerRequestDTO);
        entityManager.persist(user);
        return user;
    }

    private Task createTask(TaskRequestDTO taskRequestDTO, User user) {
        Task task = new Task(taskRequestDTO);
        task.setUser(user);
        entityManager.persist(task);
        return task;
    }

}