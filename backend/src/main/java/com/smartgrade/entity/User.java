package com.smartgrade.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "ref_id")
    private String refId;

    public User() {}

    public User(Long id, String username, String passwordHash, Role role, String refId) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.refId = refId;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public Role getRole() { return role; }
    public String getRefId() { return refId; }

    public enum Role {
        STUDENT, STAFF, CC, HOD
    }

    public static class UserBuilder {
        private Long id;
        private String username;
        private String passwordHash;
        private Role role;
        private String refId;

        public UserBuilder id(Long id) { this.id = id; return this; }
        public UserBuilder username(String username) { this.username = username; return this; }
        public UserBuilder passwordHash(String passwordHash) { this.passwordHash = passwordHash; return this; }
        public UserBuilder role(Role role) { this.role = role; return this; }
        public UserBuilder refId(String refId) { this.refId = refId; return this; }
        public User build() { return new User(id, username, passwordHash, role, refId); }
    }
}
