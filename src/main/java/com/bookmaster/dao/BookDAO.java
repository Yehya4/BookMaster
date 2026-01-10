package com.bookmaster.dao;

import com.bookmaster.models.Book;
import com.bookmaster.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, title, author_id, category_id, isbn, quantity, available FROM books ORDER BY title")) {
            while (rs.next()) {
                books.add(map(rs));
            }
        } catch (Exception e) {
            System.err.println("Erreur findAll books: " + e.getMessage());
        }
        enrich(books);
        return books;
    }

    public Book findById(int id) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id, title, author_id, category_id, isbn, quantity, available FROM books WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Book b = map(rs);
                    enrichOne(b);
                    return b;
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur findById book: " + e.getMessage());
        }
        return null;
    }

    public List<Book> findByAuthorId(int authorId) {
        List<Book> books = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id, title, author_id, category_id, isbn, quantity, available FROM books WHERE author_id=?")) {
            ps.setInt(1, authorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    books.add(map(rs));
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur findByAuthorId: " + e.getMessage());
        }
        enrich(books);
        return books;
    }

    public List<Book> findAvailable() {
        List<Book> books = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id, title, author_id, category_id, isbn, quantity, available FROM books WHERE available > 0")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    books.add(map(rs));
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur findAvailable: " + e.getMessage());
        }
        enrich(books);
        return books;
    }

    public boolean save(Book book) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO books (title, author_id, category_id, isbn, quantity, available) VALUES (?, ?, ?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, book.getTitle());
            ps.setInt(2, book.getAuthorId());
            ps.setInt(3, book.getCategoryId());
            ps.setString(4, book.getIsbn());
            ps.setInt(5, book.getQuantity());
            ps.setInt(6, book.getAvailable());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        book.setId(keys.getInt(1));
                    }
                }
                return true;
            }
        } catch (Exception e) {
            System.err.println("Erreur save book: " + e.getMessage());
        }
        return false;
    }

    public boolean update(Book book) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE books SET title=?, author_id=?, category_id=?, isbn=?, quantity=?, available=? WHERE id=?")) {
            ps.setString(1, book.getTitle());
            ps.setInt(2, book.getAuthorId());
            ps.setInt(3, book.getCategoryId());
            ps.setString(4, book.getIsbn());
            ps.setInt(5, book.getQuantity());
            ps.setInt(6, book.getAvailable());
            ps.setInt(7, book.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Erreur update book: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM books WHERE id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Erreur delete book: " + e.getMessage());
            return false;
        }
    }

    private Book map(ResultSet rs) throws SQLException {
        return new Book(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getInt("author_id"),
                rs.getInt("category_id"),
                rs.getString("isbn"),
                rs.getInt("quantity"),
                rs.getInt("available"));
    }

    private void enrich(List<Book> books) {
        AuthorDAO authorDAO = new AuthorDAO();
        CategoryDAO categoryDAO = new CategoryDAO();
        for (Book book : books) {
            var author = authorDAO.findById(book.getAuthorId());
            if (author != null) book.setAuthorName(author.getName());
            var category = categoryDAO.findById(book.getCategoryId());
            if (category != null) book.setCategoryName(category.getName());
        }
    }

    private void enrichOne(Book book) {
        if (book == null) return;
        AuthorDAO authorDAO = new AuthorDAO();
        CategoryDAO categoryDAO = new CategoryDAO();
        var author = authorDAO.findById(book.getAuthorId());
        if (author != null) book.setAuthorName(author.getName());
        var category = categoryDAO.findById(book.getCategoryId());
        if (category != null) book.setCategoryName(category.getName());
    }
}
