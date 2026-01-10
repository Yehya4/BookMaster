package com.bookmaster.dao;

import com.bookmaster.models.Author;
import com.bookmaster.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorDAO {

    public List<Author> findAll() {
        List<Author> authors = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, name, nationality FROM authors ORDER BY name")) {
            while (rs.next()) {
                authors.add(new Author(rs.getInt("id"), rs.getString("name"), rs.getString("nationality")));
            }
        } catch (Exception e) {
            System.err.println("Erreur findAll authors: " + e.getMessage());
        }
        return authors;
    }

    public Author findById(int id) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id, name, nationality FROM authors WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Author(rs.getInt("id"), rs.getString("name"), rs.getString("nationality"));
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur findById author: " + e.getMessage());
        }
        return null;
    }

    public boolean save(Author author) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO authors (name, nationality) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, author.getName());
            ps.setString(2, author.getNationality());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        author.setId(keys.getInt(1));
                    }
                }
                return true;
            }
        } catch (Exception e) {
            System.err.println("Erreur save author: " + e.getMessage());
        }
        return false;
    }

    public boolean update(Author author) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE authors SET name=?, nationality=? WHERE id=?")) {
            ps.setString(1, author.getName());
            ps.setString(2, author.getNationality());
            ps.setInt(3, author.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Erreur update author: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM authors WHERE id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Erreur delete author: " + e.getMessage());
            return false;
        }
    }
}
