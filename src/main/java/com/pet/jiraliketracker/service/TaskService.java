package com.pet.jiraliketracker.service;

import com.pet.jiraliketracker.dto.*;
import com.pet.jiraliketracker.model.Comment;
import com.pet.jiraliketracker.model.Project;
import com.pet.jiraliketracker.model.Status;
import com.pet.jiraliketracker.model.Task;
import com.pet.jiraliketracker.repository.CommentRepository;
import com.pet.jiraliketracker.repository.ProjectRepository;
import com.pet.jiraliketracker.repository.TaskRepository;
import com.pet.jiraliketracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final CommentRepository commentRepository;

    public TaskService(TaskRepository taskRepository,
                       UserRepository userRepository,
                       ProjectRepository projectRepository,
                       CommentRepository commentRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.commentRepository = commentRepository;
    }

    // TODO: Багато запитів на БД, треба кешувати.
    // TODO: Перевірка, чи assignee та author у проєкті
    public TaskResponseDTO createTask(CreateTaskRequestDTO request) {
        Task task = new Task(request.getTitle(), request.getDescription(),
                userRepository.findByEmail(request.getAuthorEmail())
                        .orElseThrow(()-> new RuntimeException("User email is not found")),
                userRepository.findByEmail(request.getAssigneeEmail())
                        .orElseThrow(()-> new RuntimeException("User email is not found")),
                projectRepository.findById(request.getProjectId())
                        .orElseThrow(()-> new RuntimeException("Project is not found")),
                Status.TODO);
        taskRepository.save(task);

        return new TaskResponseDTO(task.getTitle(), task.getDescription(), task.getCreatedAt(), task.getUpdatedAt(),
                task.getAuthor().getEmail(), task.getAssignee().getEmail(), task.getProject().getName(), task.getStatus());
    }

    @Transactional
    public TaskResponseDTO changeStatus(ChangeStatusRequestDTO request) {
        Task task = taskRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Task is not found"));
        task.setStatus(request.getStatus());
        return new TaskResponseDTO(task.getTitle(), task.getDescription(), task.getCreatedAt(), task.getUpdatedAt(),
                task.getAuthor().getEmail(), task.getAssignee().getEmail(), task.getProject().getName(), task.getStatus());
    }

    @Transactional
    public TaskResponseDTO assign(AssignRequestDTO request) {
        Task task = taskRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Task is not found"));
        task.setAssignee(userRepository.findByEmail(request.getEmail()).
                orElseThrow(()-> new RuntimeException("User is not found")));
        return new TaskResponseDTO(task.getTitle(), task.getDescription(), task.getCreatedAt(), task.getUpdatedAt(),
                task.getAuthor().getEmail(), task.getAssignee().getEmail(), task.getProject().getName(), task.getStatus());
    }

    @Transactional
    public List<TaskResponseDTO> getTasks(Long id) {
        List<Task> tasks = taskRepository.findAllByProjectId(id);
        List<TaskResponseDTO> list = new ArrayList<>();
        for(Task task : tasks) {
            list.add(new TaskResponseDTO(task.getTitle(), task.getDescription(), task.getCreatedAt(), task.getUpdatedAt(),
                    task.getAuthor().getEmail(), task.getAssignee().getEmail(), task.getProject().getName(), task.getStatus()));
        }
        return list;
    }

    @Transactional
    public CommentResponseDTO addComment(AddCommentRequestDTO request) {
        Comment comment = new Comment(
                taskRepository.findById(request.getId())
                    .orElseThrow(()-> new RuntimeException("Task is not found")),
                userRepository.findByEmail(request.getAuthorEmail())
                        .orElseThrow(()-> new RuntimeException("User is not found")),
                request.getText());
        return new CommentResponseDTO(comment.getTask().getId(), comment.getUser().getEmail(), comment.getText());
    }

    public List<CommentResponseDTO> getTaskComments(Long id) {
        List<Comment> comments = commentRepository.findAllByTaskId(id);
        List<CommentResponseDTO> list = new ArrayList<>();
        for(Comment comment : comments) {
            list.add(new CommentResponseDTO(comment.getTask().getId(), comment.getUser().getEmail(), comment.getText()));
        }
        return list;
    }
}
