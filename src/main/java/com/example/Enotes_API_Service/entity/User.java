package com.example.Enotes_API_Service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstName;

    private String lastName;

    private String email;

    private String mobNo;

    private String password;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Role> roles;
}
