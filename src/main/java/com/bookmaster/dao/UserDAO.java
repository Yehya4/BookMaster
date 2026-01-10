package com.bookmaster.dao;

import com.bookmaster.models.User;
import com.bookmaster.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, username, password, email, role FROM users")) {
            while (rs.next()) {
                users.add(new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("email"), rs.getString("role")));
            }
        } catch (Exception e) {
            System.err.println("Erreur findAll users: " + e.getMessage());
        }
        return users;
    }

    public User findByUsername(String username) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id, username, password, email, role FROM users WHERE username=?")) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("email"), rs.getString("role"));
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur findByUsername: " + e.getMessage());
        }
        return null;
    }

    public User findById(int id) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id, username, password, email, role FROM users WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("email"), rs.getString("role"));
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur findById user: " + e.getMessage());
        }
        return null;
    }

    public boolean save(User user) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getRole());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        user.setId(keys.getInt(1));
                    }
                }
                return true;
            }
        } catch (Exception e) {
            System.err.println("Erreur save user: " + e.getMessage());
        }
        return false;
    }

    public boolean update(User user) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE users SET username=?, password=?, email=?, role=? WHERE id=?")) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getRole());
            ps.setInt(5, user.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Erreur update user: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM users WHERE id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Erreur delete user: " + e.getMessage());
            return false;
        }
    }
}
