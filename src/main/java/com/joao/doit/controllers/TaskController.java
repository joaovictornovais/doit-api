package com.joao.doit.controllers;

import com.joao.doit.domain.task.Task;
import com.joao.doit.domain.task.TaskRequestDTO;
import com.joao.doit.services.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<Task> createTask(@AuthenticationPrincipal UserDetails userDetails,
                                           @RequestBody @Valid TaskRequestDTO data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(userDetails, data));
    }

    @GetMapping
    public ResponseEntity<List<Task>> findUserTasks(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.findUserTasks(userDetails));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> findById(@AuthenticationPrincipal UserDetails userDetails,
                                         @PathVariable UUID taskId) {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.findById(taskId, userDetails));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteById(@AuthenticationPrincipal UserDetails userDetails,
                                           @PathVariable UUID taskId) {
        taskService.deleteTask(taskId, userDetails);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Task> edit(@AuthenticationPrincipal UserDetails userDetails,
                                     @PathVariable UUID taskId,
                                     @RequestBody @Valid TaskRequestDTO data) {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.editTask(taskId, userDetails, data));
    }

}
