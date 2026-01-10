package com.bookmaster.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBInitializer {
    public static void initialize() {
        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS categories (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(255) NOT NULL)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS authors (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(255) NOT NULL," +
                    "nationality VARCHAR(255) NOT NULL)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS members (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(255) NOT NULL," +
                    "email VARCHAR(255) NOT NULL," +
                    "phone VARCHAR(50) NOT NULL)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "username VARCHAR(255) NOT NULL UNIQUE," +
                    "password VARCHAR(255) NOT NULL," +
                    "email VARCHAR(255) NOT NULL," +
                    "role VARCHAR(50) NOT NULL)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS books (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "title VARCHAR(255) NOT NULL," +
                    "author_id INT NOT NULL," +
                    "category_id INT NOT NULL," +
                    "isbn VARCHAR(50) NOT NULL," +
                    "quantity INT NOT NULL," +
                    "available INT NOT NULL," +
                    "FOREIGN KEY (author_id) REFERENCES authors(id)," +
                    "FOREIGN KEY (category_id) REFERENCES categories(id))");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS loans (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "book_id INT NOT NULL," +
                    "member_id INT NOT NULL," +
                    "loan_date DATE NOT NULL," +
                    "return_date DATE NULL," +
                    "returned BOOLEAN NOT NULL," +
                    "FOREIGN KEY (book_id) REFERENCES books(id)," +
                    "FOREIGN KEY (member_id) REFERENCES members(id))");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void ensureDefaultAdmin() {
        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement()) {
            try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM users")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    try (PreparedStatement ps = conn.prepareStatement(
                            "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, ?)")) {
                        ps.setString(1, "admin");
                        ps.setString(2, "admin123");
                        ps.setString(3, "admin@bookmaster.com");
                        ps.setString(4, "admin");
                        ps.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
