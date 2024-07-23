package com.joao.doit.services;

import com.joao.doit.domain.task.Task;
import com.joao.doit.domain.task.TaskRequestDTO;
import com.joao.doit.domain.user.User;
import com.joao.doit.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    public Task createTask(UserDetails userDetails, TaskRequestDTO data) {
        User user = userService.findByEmail(userDetails.getUsername());
        Task task = new Task(data);
        task.setUser(user);
        return taskRepository.save(task);
    }

    public List<Task> findUserTasks(UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        return taskRepository.findByUser(user);
    }

    public Task findById(UUID taskId, UserDetails userDetails) {
        return verificateTask(taskId, userDetails);
    }

    public void deleteTask(UUID taskId, UserDetails userDetails) {
        verificateTask(taskId, userDetails);
        taskRepository.deleteById(taskId);
    }

    public Task editTask(UUID taskId, UserDetails userDetails, TaskRequestDTO data) {
        Task task = verificateTask(taskId, userDetails);
        BeanUtils.copyProperties(data, task);
        return taskRepository.save(task);
    }

    private Task verificateTask(UUID taskId, UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new RuntimeException("Tarefa não encontrada")
        );
        if (task.getUser() != user) throw new RuntimeException("Você não possui permissão para visualizar essa tarefa");
        return task;
    }

}
