package com.mariodoumbanov.documentqa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Entity
@Table(name="authorities")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(unique = true, length = 30, nullable = false)
    private String name;

    private String description;

    private List<User> users;

    private Date createdAt;

    public Authority(String name, String description) {
        this(null, name, description, List.of(), new Date(System.currentTimeMillis()));
    }
}
