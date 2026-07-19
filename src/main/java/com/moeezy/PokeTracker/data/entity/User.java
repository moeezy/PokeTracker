package com.moeezy.PokeTracker.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="Users")
@Data
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private int userId;

    @Size(min = 4, max = 255, message = "Minimum username length: 4 characters")
    @Column(nullable = false, name="username")
    private String username;

    @Column(nullable = false, name="email")
    private String email;

    @Column(nullable = false, name="password")
    private String password;
}
