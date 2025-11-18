package com.cafe.model;

import java.util.UUID;

/** Data user + role */
public class User {
    private UUID id;
    private String nama;
    private String email;
    private Role role;
    private String passwordHash;

    public User(UUID id, String nama, String email, Role role, String passwordHash) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.role = role;
        this.passwordHash = passwordHash;
    }

    public UUID getId() { return id; }
    public String getNama() { return nama; }
    public String getEmail() { return email; }
    public Role getRole() { return role; }
    public String getPasswordHash() { return passwordHash; }
}
