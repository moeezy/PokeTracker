package com.moeezy.PokeTracker.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name="Users")
@Data
@ToString
public class User {
    @Id
    @Column(name="user_id")
    private int userId;

    @Column(name="username")
    private String username;
}
