package com.pet.jiraliketracker.model;

import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="comment")
@NoArgsConstructor
public class Comment {

    public Comment(Task task, User user, String text) {
        this.task = task;
        this.user = user;
        this.text = text;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    private Task task;


    @ManyToOne
    private User user;

    @Column(nullable = false)
    private String text;
}
