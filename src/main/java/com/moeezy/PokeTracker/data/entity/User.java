package com.moeezy.PokeTracker.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name="Users")
@Data
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private int userId;

    @Column(nullable = false, name="username")
    private String username;

    @Column(nullable = false, name="email")
    private String email;

    @Column(nullable = false, name="password_hash")
    private String passwordHash;
}
