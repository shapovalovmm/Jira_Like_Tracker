package com.pet.jiraliketracker.service;

import com.pet.jiraliketracker.dto.*;
import com.pet.jiraliketracker.mapper.CommentMapper;
import com.pet.jiraliketracker.mapper.TaskMapper;
import com.pet.jiraliketracker.model.*;
import com.pet.jiraliketracker.repository.CommentRepository;
import com.pet.jiraliketracker.repository.ProjectRepository;
import com.pet.jiraliketracker.repository.TaskRepository;
import com.pet.jiraliketracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final CommentRepository commentRepository;
    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

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
    public TaskResponseDTO createTask(CreateTaskRequestDTO request) {
        log.info("Creating task with title: {}", request.getTitle());

        User user = getCurrentUser();

        List<Long> membersId = userRepository.findMemberIdsByProjectId(request.getProjectId());
        if(!(membersId.contains(user.getId()))) {
            throw new AccessDeniedException("You have no rights");
        }

        Task task = new Task(request.getTitle(), request.getDescription(),
                userRepository.findByEmail(request.getAuthorEmail())
                        .orElseThrow(() -> {
                            log.error("User not found with email: {}", request.getAuthorEmail());
                            return new RuntimeException("User not found");
                        }),
                userRepository.findByEmail(request.getAssigneeEmail())
                        .orElseThrow(() -> {
                            log.error("User not found with email: {}", request.getAssigneeEmail());
                            return new RuntimeException("User not found");
                        }),
                projectRepository.findById(request.getProjectId())
                        .orElseThrow(() -> {
                            log.error("Project not found with id: {}", request.getProjectId());
                            return new RuntimeException("User not found");
                        }),
                Status.TODO, 0);
        taskRepository.save(task);
        log.info("Task created successfully. id={}, author={}, assignee={}",
                task.getId(),
                task.getAuthor().getEmail(),
                task.getAssignee().getEmail());
        return TaskMapper.toDto(task);
    }

    @Transactional
    public TaskResponseDTO changeStatus(ChangeStatusRequestDTO request) {
        log.info("Changing status for task {}", request.getId());
        User user = getCurrentUser();
        Task task = taskRepository.findById(request.getId())
                .orElseThrow(() -> {
                    log.error("Task not found with id: {}", request.getId());
                    return new RuntimeException("Task not found");
                });

        // Змінювати статус може тільки assignee або автор
        boolean isAssignee = task.getAssignee() != null &&
                task.getAssignee().getId().equals(user.getId());
        boolean isAuthor = task.getAuthor() != null &&
                task.getAuthor().getId().equals(user.getId());
        if (!(isAssignee || isAuthor)) {
            log.warn("User {} tried to change status of task {} without access", user.getEmail(), task.getId());
            throw new AccessDeniedException("You cannot change this task");
        }

        task.setStatus(request.getStatus());
        task.setUpdatedAt(LocalDateTime.now());

        log.info("User {} changed status of task {} to {}",
                user.getEmail(),
                task.getId(),
                request.getStatus());

        return TaskMapper.toDto(task);
    }

    @Transactional
    public TaskResponseDTO assign(AssignRequestDTO request) {
        log.info("Assigning user {} to task {}", request.getEmail(), request.getId());
        User user = getCurrentUser();

        Task task = taskRepository.findById(request.getId())
                .orElseThrow(() -> {
                    log.error("Task not found with id: {}", request.getId());
                    return new RuntimeException("Task not found");
                });

        // Назначити відповідального може тільки автор або власник проєкту
        if(!(user.getId().equals(task.getAuthor().getId())
                || user.getId().equals(task.getProject().getOwner().getId())))
        {
            log.warn("User {} tried to assign to task {} without access", user.getEmail(), task.getId());
            throw new AccessDeniedException("You cannot change this task");
        }

        task.setAssignee(userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("User not found with email: {}", request.getEmail());
                    return new RuntimeException("User not found");
                }));
        task.setUpdatedAt(LocalDateTime.now());

        log.info("User {} assigned task {} to {}",
                user.getEmail(),
                task.getId(),
                request.getEmail());

        return TaskMapper.toDto(task);
    }

    @Transactional
    public Page<TaskResponseDTO> getTasks(Long id, Pageable pageable) {
        User user = getCurrentUser();

        List<Long> membersId = userRepository.findMemberIdsByProjectId(id);
        if(!(membersId.contains(user.getId()))) {
            log.warn("User {} tried to view tasks in project {} without access", user.getEmail(), id);
            throw new AccessDeniedException("You cannot view tasks");
        }

        Page<Task> tasksPage = taskRepository.findAllByProjectId(id, pageable);
        return tasksPage.map(task -> TaskMapper.toDto(task));
    }

    @Transactional
    public Page<TaskResponseDTO> getTasksByPriority(Long id, int priority, Pageable pageable) {
        User user = getCurrentUser();

        List<Long> membersId = userRepository.findMemberIdsByProjectId(id);
        if(!(membersId.contains(user.getId()))) {
            log.warn("User {} tried to view tasks in project {} without access", user.getEmail(), id);
            throw new AccessDeniedException("You cannot view tasks");
        }
        Page<Task> tasksPage = taskRepository.findAllByProjectIdAndPriority(id, priority, pageable);
        return tasksPage.map(task -> TaskMapper.toDto(task));
    }

    @Transactional
    public Page<TaskResponseDTO> getTasksByStatus(Long id, Status status, Pageable pageable) {
        User user = getCurrentUser();
        List<Long> membersId = userRepository.findMemberIdsByProjectId(id);
        if(!(membersId.contains(user.getId()))) {
            log.warn("User {} tried to view tasks in project {} without access", user.getEmail(), id);
            throw new AccessDeniedException("You cannot view tasks");
        }

        Page<Task> tasksPage = taskRepository.findAllByProjectIdAndStatus(id, status, pageable);
        return tasksPage.map(task -> TaskMapper.toDto(task));
    }

    @Transactional
    public CommentResponseDTO addComment(AddCommentRequestDTO request) {
        User user = getCurrentUser();
        Task task = taskRepository.findById(request.getId())
                .orElseThrow(() -> {
                    log.warn("Task not found: {}", request.getId());
                    return new RuntimeException("Task not found");
                });
        // Тільки учасники проєкту можуть залишати коментарі
        Project project = taskRepository.findById(request.getId())
                .orElseThrow(()-> new RuntimeException("Task is not found")).getProject();
        List<Long> membersId = userRepository.findMemberIdsByProjectId(project.getId());
        if(!(membersId.contains(user.getId()))) {
            log.warn("User {} tried to comment task in project {} without access", user.getEmail(), project.getId());
            throw new AccessDeniedException("You cannot add comment");
        }

        Comment comment = new Comment(
                task,
                userRepository.findByEmail(request.getAuthorEmail())
                        .orElseThrow(() ->
                           new RuntimeException("User not found")
                        ),
                request.getText());
        commentRepository.save(comment);

        log.info("User {} added comment to task {}",
                user.getEmail(),
                request.getId());
        return CommentMapper.toDto(comment);
    }

    public List<CommentResponseDTO> getTaskComments(Long id) {
        User user = getCurrentUser();

        List<Long> membersId = userRepository.findMemberIdsByProjectId(taskRepository.findById(id)
                .orElseThrow().getProject().getId());
        if(!(membersId.contains(user.getId()))) {
            throw new AccessDeniedException("You cannot add comment");
        }
        List<Comment> comments = commentRepository.findAllByTaskId(id);
        List<CommentResponseDTO> list = new ArrayList<>();
        for(Comment comment : comments) {
            list.add(CommentMapper.toDto(comment));
        }
        return list;
    }

    private User getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> {
                    log.warn("User not found with email: {}", userDetails.getUsername());
                    return new RuntimeException("User not found");
                });
    }

}
