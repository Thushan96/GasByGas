package com.esoft.gascollect.entity;

import com.esoft.gascollect.entity.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private int contactNo;

    private String email;

    private String password; // Add this field for storing the hashed password

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders; // List of orders associated with this user

    public User(int id, String name, int contactNo, String encodedPassword, Set<Role> roles) {
        this.id = id;
        this.name = name;
        this.contactNo = contactNo;
        this.password = encodedPassword;
        this.roles = roles;
    }
}
