package com.turkcell.gameplus.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(nullable = false)
    private String name;

    private String city;
    private String segment;
}

