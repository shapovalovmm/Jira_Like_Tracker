package com.pet.jiraliketracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"owner_id", "name"}
        )
)
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToMany
    private List<User> members;

    private String name;

    @OneToMany(mappedBy = "project")
    private List<Task> tasks;
    public Project() {}

    public Project(User owner, List<User> members, String name) {
        this.owner = owner;
        this.members = members;
        this.name = name;
    }
}

//todo Настроить валидацию