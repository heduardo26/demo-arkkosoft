package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="_task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String title;

    private String description;

    private Boolean complete;

    @JsonBackReference
    @ManyToOne(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private User user;

    public Task(String title, String description, Boolean complete, User user) {
        this.title = title;
        this.description = description;
        this.complete = complete;
        this.user = user;
    }
}
