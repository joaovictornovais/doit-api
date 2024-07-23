package com.joao.doit.repositories;

import com.joao.doit.domain.task.Task;
import com.joao.doit.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findByUser(User user);
}
