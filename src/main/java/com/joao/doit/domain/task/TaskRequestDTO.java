package com.joao.doit.domain.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TaskRequestDTO(
        @NotBlank(message = "Título não deve ser vazio")
        String title,
        String description,
        @NotNull(message = "Prioridade de tarefa não deve ser vazio")
        TaskPriority taskPriority,
        @NotNull(message = "Status da tarefa não deve ser vazio")
        TaskStatus taskStatus,
        LocalDateTime endDate
        ) {
}
