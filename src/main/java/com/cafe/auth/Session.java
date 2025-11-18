package com.cafe.auth;

import com.cafe.model.User;

/** Session login sederhana */
public class Session {
    private final User user;

    public Session(User user) { this.user = user; }
    public User getUser() { return user; }
}
