package com.cafe.auth;

import com.cafe.model.Role;
import com.cafe.model.User;

import java.util.*;

/**
 * Service untuk register, login, dan verifikasi role.
 */
public class AuthService {

    private final Map<String, User> emailMap = new HashMap<>();

    public AuthService() {
        // Tambahkan user default untuk Barista dan Admin
        register("Barista", "barista@mail.com", "456", Role.BARISTA);
        register("Admin", "admin@mail.com", "789", Role.ADMIN);
    }

    public User register(String nama, String email, String password) {
        return register(nama, email, password, Role.CUSTOMER);
    }

    public User register(String nama, String email, String password, Role role) {
        if (emailMap.containsKey(email)) {
            throw new RuntimeException("Email sudah terdaftar!");
        }
        User u = new User(UUID.randomUUID(), nama, email, role, password);
        emailMap.put(email, u);
        return u;
    }

    public Session login(String email, String password) {
        User u = emailMap.get(email);
        if (u == null) throw new RuntimeException("Email tidak ditemukan");
        if (!u.getPasswordHash().equals(password)) throw new RuntimeException("Password salah");
        return new Session(u);
    }

    public boolean authorize(User u, Role role) {
        return u.getRole() == role;
    }
}