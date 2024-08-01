package com.joao.doit.services;

import com.joao.doit.domain.task.Task;
import com.joao.doit.domain.task.TaskPriority;
import com.joao.doit.domain.task.TaskRequestDTO;
import com.joao.doit.domain.task.TaskStatus;
import com.joao.doit.domain.user.RegisterRequestDTO;
import com.joao.doit.domain.user.User;
import com.joao.doit.repositories.TaskRepository;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskServiceTest {

    @Mock
    TaskRepository taskRepository;

    @Mock
    UserService userService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    @InjectMocks
    TaskService taskService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should create a task successfully")
    void createTaskCase1() {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("João Victor", "Novais", "joaovkt.novais@gmail.com", "123456");
        User user = new User(registerRequestDTO);
        UserDetails userDetails = mock(UserDetails.class);
        when(userService.findByEmail(userDetails.getUsername())).thenReturn(user);
        TaskRequestDTO taskRequestDTO = new TaskRequestDTO(
                "Create unit tests",
                "Create unit tests for my new project",
                TaskPriority.HIGH,
                TaskStatus.BACKLOG,
                LocalDateTime.now().plusDays(7));
        Task task = new Task(taskRequestDTO);
        task.setUser(user);
        user.getTasks().add(task);

        assertThat(user.getTasks()).size().isEqualTo(1);
        assertThat(user.getTasks()).contains(task);
        assertThat(task.getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("Should return all tasks from user")
    void findTasksCase1() {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("João Victor", "Novais", "joaovkt.novais@gmail.com", "123456");
        User user = new User(registerRequestDTO);
        UserDetails userDetails = mock(UserDetails.class);
        when(userService.findByEmail(userDetails.getUsername())).thenReturn(user);
        TaskRequestDTO taskRequestDTO1 = new TaskRequestDTO(
                "Create unit tests",
                "Create unit tests for my new project",
                TaskPriority.HIGH,
                TaskStatus.BACKLOG,
                LocalDateTime.now().plusDays(7));
        TaskRequestDTO taskRequestDTO2 = new TaskRequestDTO(
                "Read a book",
                "Learn about spring",
                TaskPriority.MEDIUM,
                TaskStatus.BACKLOG,
                LocalDateTime.now().plusDays(7));
        Task task1 = new Task(taskRequestDTO1);
        Task task2 = new Task(taskRequestDTO2);
        task1.setUser(user);
        task2.setUser(user);
        user.getTasks().add(task1);
        user.getTasks().add(task2);

        assertThat(user.getTasks()).containsAll(List.of(task1, task2));

    }

    @Test
    @DisplayName("Should return a task by Id if belongs to logged user")
    void findTaskByIdCase1() {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("João Victor", "Novais", "joaovkt.novais@gmail.com", "123456");
        User user = new User(registerRequestDTO);
        TaskRequestDTO taskRequestDTO = new TaskRequestDTO(
                "Create unit tests",
                "Create unit tests for my new project",
                TaskPriority.HIGH,
                TaskStatus.BACKLOG,
                LocalDateTime.now().plusDays(7));
        Task task = new Task(taskRequestDTO);
        task.setId(UUID.randomUUID());
        user.getTasks().add(task);
        task.setUser(user);

        UserDetails userDetails = mock(UserDetails.class);
        when(userService.findByEmail(userDetails.getUsername())).thenReturn(user);
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        Optional<Task> responseTask = taskRepository.findById(task.getId());

        if (responseTask.isPresent() && !responseTask.get().getUser().equals(user)) {
            throw new RuntimeException("Access denied");
        }

        assertThat(responseTask).isPresent();
        assertThat(responseTask.get().getUser()).isEqualTo(user);

    }

    @Test
    @DisplayName("Should throw exception when logged users tries to access a task that dont belongs to him")
    void findTaskByIdCase2() {
        RegisterRequestDTO registerRequestDTO1 = new RegisterRequestDTO("Fulano", "da Silva", "fulanodasilva@gmail.com", "123456");
        User user1 = new User(registerRequestDTO1);
        RegisterRequestDTO registerRequestDTO2 = new RegisterRequestDTO("João Victor", "Novais", "joaovkt.novais@gmail.com", "123456");
        User user2 = new User(registerRequestDTO2);
        TaskRequestDTO taskRequestDTO = new TaskRequestDTO(
                "Create unit tests",
                "Create unit tests for my new project",
                TaskPriority.HIGH,
                TaskStatus.BACKLOG,
                LocalDateTime.now().plusDays(7));
        Task task = new Task(taskRequestDTO);
        task.setId(UUID.randomUUID());
        task.setUser(user2);

        UserDetails userDetails = mock(UserDetails.class);
        when(userService.findByEmail(userDetails.getUsername())).thenReturn(user1);
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        Exception thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            verificateTask(task, user1);
        });

        assertEquals(thrown.getMessage(), "Access denied");
    }

    @Test
    @DisplayName("Should edit task infos")
    void editTaskCase1() {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("Fulano", "da Silva", "fulanodasilva@gmail.com", "123456");
        User user = new User(registerRequestDTO);
        TaskRequestDTO taskRequestDTO = new TaskRequestDTO(
                "Create unit tests",
                "Create unit tests for my new project",
                TaskPriority.HIGH,
                TaskStatus.BACKLOG,
                LocalDateTime.now().plusDays(7)
        );
        Task task = new Task(taskRequestDTO);
        task.setId(UUID.randomUUID());
        task.setUser(user);
        user.getTasks().add(task);

        verificateTask(task, user);

        TaskRequestDTO newTask = new TaskRequestDTO(
                "Create unit tests for service layer",
                "Create unit tests for my new project",
                TaskPriority.LOW,
                TaskStatus.DOING,
                LocalDateTime.now().plusDays(3)
        );
        BeanUtils.copyProperties(newTask, task);

        assertThat(task.getTitle()).isEqualTo(newTask.title());
        assertThat(task.getTaskPriority()).isEqualTo(newTask.taskPriority());
        assertThat(task.getTaskStatus()).isEqualTo(newTask.taskStatus());
    }

    @Test
    @DisplayName("Should throw an exception when trying to user tries to edit an task that dont belongs to him")
    void editTaskCase2() {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("Fulano", "da Silva", "fulanodasilva@gmail.com", "123456");
        User user = new User(registerRequestDTO);
        RegisterRequestDTO registerRequestDTO2 = new RegisterRequestDTO("Joao", "Santos", "joao@gmail.com", "123456");
        User user2 = new User(registerRequestDTO2);
        TaskRequestDTO taskRequestDTO = new TaskRequestDTO(
                "Create unit tests",
                "Create unit tests for my new project",
                TaskPriority.HIGH,
                TaskStatus.BACKLOG,
                LocalDateTime.now().plusDays(7)
        );
        Task task = new Task(taskRequestDTO);
        task.setId(UUID.randomUUID());
        task.setUser(user);
        user.getTasks().add(task);

        Exception thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            verificateTask(task, user2);
        });

        assertEquals(thrown.getMessage(), "Access denied");

    }

    @Test
    @DisplayName("Should delete a task from user")
    void deleteTaskCase1() {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("Fulano", "da Silva", "fulanodasilva@gmail.com", "123456");
        User user = new User(registerRequestDTO);
        TaskRequestDTO taskRequestDTO = new TaskRequestDTO(
                "Create unit tests",
                "Create unit tests for my new project",
                TaskPriority.HIGH,
                TaskStatus.BACKLOG,
                LocalDateTime.now().plusDays(7)
        );
        Task task = new Task(taskRequestDTO);
        task.setId(UUID.randomUUID());
        task.setUser(user);
        user.getTasks().add(task);

        verificateTask(task, user);

        taskRepository.deleteById(task.getId());
        user.getTasks().remove(task);

        assertThat(user.getTasks().contains(task)).isFalse();
        assertThat(user.getTasks()).isEmpty();
    }

    @Test
    @DisplayName("Should throw an exception when user tries to delete a task that dont belongs to him")
    void deleteTaskCase2() {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("Fulano", "da Silva", "fulanodasilva@gmail.com", "123456");
        User user = new User(registerRequestDTO);
        RegisterRequestDTO registerRequestDTO2 = new RegisterRequestDTO("Joao", "Santos", "joao@gmail.com", "123456");
        User user2 = new User(registerRequestDTO2);
        TaskRequestDTO taskRequestDTO = new TaskRequestDTO(
                "Create unit tests",
                "Create unit tests for my new project",
                TaskPriority.HIGH,
                TaskStatus.BACKLOG,
                LocalDateTime.now().plusDays(7)
        );
        Task task = new Task(taskRequestDTO);
        task.setId(UUID.randomUUID());
        task.setUser(user);
        user.getTasks().add(task);

        Exception thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            verificateTask(task, user2);
        });

        assertEquals(thrown.getMessage(), "Access denied");

    }

    private Task verificateTask(Task task, User user) {
        if (task.getUser() == user) return task;
        throw new RuntimeException("Access denied");
    }



}