package com.joao.doit.domain.task;

import lombok.Getter;

@Getter
public enum TaskStatus {

    BACKLOG("backlog"),
    DOING("doing"),
    REVIEW("review"),
    DONE("done");

    private final String status;

    private TaskStatus(String status) {
        this.status = status;
    }

}
