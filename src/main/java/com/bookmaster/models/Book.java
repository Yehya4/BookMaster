package com.bookmaster.models;

public class Book {
    private int id;
    private String title;
    private int authorId;
    private int categoryId;
    private String isbn;
    private int quantity;
    private int available;

    private String authorName;
    private String categoryName;

    public Book(int id, String title, int authorId, int categoryId, String isbn, int quantity, int available) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.categoryId = categoryId;
        this.isbn = isbn;
        this.quantity = quantity;
        this.available = available;
    }

    public Book(String title, int authorId, int categoryId, String isbn, int quantity, int available) {
        this.title = title;
        this.authorId = authorId;
        this.categoryId = categoryId;
        this.isbn = isbn;
        this.quantity = quantity;
        this.available = available;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {

        this.isbn = isbn;
    }

    public int getQuantity() {

        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public boolean isAvailable() {
        return available > 0;
    }

    @Override
    public String toString() {
        return title;
    }
}
