package com.pet.jiraliketracker.repository;

import com.pet.jiraliketracker.model.Comment;
import com.pet.jiraliketracker.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByTaskId(Long TaskId);
}
