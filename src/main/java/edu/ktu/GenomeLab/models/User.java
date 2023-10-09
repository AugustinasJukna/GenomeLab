package edu.ktu.GenomeLab.models;

import edu.ktu.GenomeLab.models.enums.Role;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "name")
        private String name;

        @Column(name = "password")
        private String password;

        @Column(name = "email")
        private String email;

        @Enumerated(EnumType.STRING)
        private Role role;

        public User(String name, String password, String email, Role role) {
            this.name = name;
            this.password = password;
            this.email = email;
            this.role = role;
        }

    public User() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}

