package com.joao.doit.domain.task;

import lombok.Getter;

@Getter
public enum TaskPriority {

    LOW("low"),
    MEDIUM("medium"),
    HIGH("high");

    private final String priority;

    private TaskPriority(String priority) {
        this.priority = priority;
    }

}
