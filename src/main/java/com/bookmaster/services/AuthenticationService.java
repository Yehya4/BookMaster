package com.bookmaster.services;

import com.bookmaster.dao.UserDAO;
import com.bookmaster.models.User;

public class AuthenticationService {

    private UserDAO userDAO;

    public AuthenticationService() {
        this.userDAO = new UserDAO();
    }

    public User login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }

        if (password == null || password.trim().isEmpty()) {
            return null;
        }

        User user = userDAO.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            return user;
        }

        return null;
    }

    public boolean validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }

        return username.length() >= 3;
    }

    public boolean validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return false;
        }

        return password.length() >= 4;
    }

    public boolean validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        return email.contains("@") && email.contains(".");
    }
}
