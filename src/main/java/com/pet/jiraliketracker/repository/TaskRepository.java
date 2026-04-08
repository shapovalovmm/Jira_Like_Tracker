package com.pet.jiraliketracker.repository;

import com.pet.jiraliketracker.model.Status;
import com.pet.jiraliketracker.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByProjectId(Long projectId);
    Page<Task> findAllByProjectId(Long projectId, Pageable pageable);
    Page<Task> findAllByProjectIdAndStatus(Long projectId, Status status, Pageable pageable);
    Page<Task> findAllByProjectIdAndPriority(Long projectId, int priority, Pageable pageable);
}
