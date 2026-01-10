package com.bookmaster.services;

import com.bookmaster.dao.AuthorDAO;
import com.bookmaster.dao.BookDAO;
import com.bookmaster.dao.CategoryDAO;
import com.bookmaster.models.Author;
import com.bookmaster.models.Book;
import com.bookmaster.models.Category;

import java.util.List;

public class BookService {

    private BookDAO bookDAO;
    private AuthorDAO authorDAO;
    private CategoryDAO categoryDAO;

    public BookService() {
        this.bookDAO = new BookDAO();
        this.authorDAO = new AuthorDAO();
        this.categoryDAO = new CategoryDAO();
    }

    public List<Book> getAllBooks() {
        List<Book> books = bookDAO.findAll();

        for (Book book : books) {
            Author author = authorDAO.findById(book.getAuthorId());
            if (author != null) {
                book.setAuthorName(author.getName());
            }

            Category category = categoryDAO.findById(book.getCategoryId());
            if (category != null) {
                book.setCategoryName(category.getName());
            }
        }

        return books;
    }

    public List<Book> getAvailableBooks() {
        List<Book> books = bookDAO.findAvailable();

        for (Book book : books) {
            Author author = authorDAO.findById(book.getAuthorId());
            if (author != null) {
                book.setAuthorName(author.getName());
            }

            Category category = categoryDAO.findById(book.getCategoryId());
            if (category != null) {
                book.setCategoryName(category.getName());
            }
        }

        return books;
    }

    public Book getBookById(int id) {
        Book book = bookDAO.findById(id);
        if (book != null) {
            Author author = authorDAO.findById(book.getAuthorId());
            if (author != null) {
                book.setAuthorName(author.getName());
            }

            Category category = categoryDAO.findById(book.getCategoryId());
            if (category != null) {
                book.setCategoryName(category.getName());
            }
        }
        return book;
    }

    public boolean addBook(String title, int authorId, int categoryId, String isbn, int quantity) {
        if (title == null || title.trim().isEmpty()) {
            return false;
        }

        if (isbn == null || isbn.trim().isEmpty()) {
            return false;
        }

        if (quantity < 0) {
            return false;
        }

        if (authorDAO.findById(authorId) == null) {
            return false;
        }

        if (categoryDAO.findById(categoryId) == null) {
            return false;
        }

        Book book = new Book(title, authorId, categoryId, isbn, quantity, quantity);
        return bookDAO.save(book);
    }

    public boolean updateBook(int id, String title, int authorId, int categoryId, String isbn, int quantity,
            int available) {
        if (title == null || title.trim().isEmpty()) {
            return false;
        }

        if (isbn == null || isbn.trim().isEmpty()) {
            return false;
        }

        if (quantity < 0 || available < 0 || available > quantity) {
            return false;
        }

        if (authorDAO.findById(authorId) == null) {
            return false;
        }

        if (categoryDAO.findById(categoryId) == null) {
            return false;
        }

        Book book = bookDAO.findById(id);
        if (book == null) {
            return false;
        }

        book.setTitle(title);
        book.setAuthorId(authorId);
        book.setCategoryId(categoryId);
        book.setIsbn(isbn);
        book.setQuantity(quantity);
        book.setAvailable(available);

        return bookDAO.update(book);
    }

    public boolean deleteBook(int id) {
        return bookDAO.delete(id);
    }

    public boolean decrementAvailability(int bookId) {
        Book book = bookDAO.findById(bookId);
        if (book == null || book.getAvailable() <= 0) {
            return false;
        }

        book.setAvailable(book.getAvailable() - 1);
        return bookDAO.update(book);
    }

    public boolean incrementAvailability(int bookId) {
        Book book = bookDAO.findById(bookId);
        if (book == null || book.getAvailable() >= book.getQuantity()) {
            return false;
        }

        book.setAvailable(book.getAvailable() + 1);
        return bookDAO.update(book);
    }
}
