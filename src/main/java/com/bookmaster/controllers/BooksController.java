package com.bookmaster.controllers;

import com.bookmaster.models.Author;
import com.bookmaster.models.Book;
import com.bookmaster.models.Category;
import com.bookmaster.services.AuthorService;
import com.bookmaster.services.BookService;
import com.bookmaster.services.CategoryService;
import com.bookmaster.utils.AlertHelper;
import com.bookmaster.utils.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;

public class BooksController {

    @FXML
    private TextField titleField;

    @FXML
    private ComboBox<Author> authorCombo;

    @FXML
    private ComboBox<Category> categoryCombo;

    @FXML
    private TextField isbnField;

    @FXML
    private TextField quantityField;

    @FXML
    private TableView<Book> booksTable;

    private BookService bookService;
    private AuthorService authorService;
    private CategoryService categoryService;
    private ObservableList<Book> booksList;

    public BooksController() {
        bookService = new BookService();
        authorService = new AuthorService();
        categoryService = new CategoryService();
    }

    @FXML
    public void initialize() {
        loadAuthors();
        loadCategories();
        loadBooks();

        booksTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                fillFields(newVal);
            }
        });
    }

    private void loadAuthors() {
        List<Author> authors = authorService.getAllAuthors();
        authorCombo.setItems(FXCollections.observableArrayList(authors));
    }

    private void loadCategories() {
        List<Category> categories = categoryService.getAllCategories();
        categoryCombo.setItems(FXCollections.observableArrayList(categories));
    }

    private void loadBooks() {
        List<Book> books = bookService.getAllBooks();
        booksList = FXCollections.observableArrayList(books);
        booksTable.setItems(booksList);
    }

    private void fillFields(Book book) {
        titleField.setText(book.getTitle());
        isbnField.setText(book.getIsbn());
        quantityField.setText(String.valueOf(book.getQuantity()));

        for (Author author : authorCombo.getItems()) {
            if (author.getId() == book.getAuthorId()) {
                authorCombo.setValue(author);
                break;
            }
        }

        for (Category category : categoryCombo.getItems()) {
            if (category.getId() == book.getCategoryId()) {
                categoryCombo.setValue(category);
                break;
            }
        }
    }

    @FXML
    private void handleAdd() {
        if (!validateFields()) {
            AlertHelper.showError("Veuillez remplir tous les champs correctement");
            return;
        }

        Author selectedAuthor = authorCombo.getValue();
        Category selectedCategory = categoryCombo.getValue();
        int quantity = Integer.parseInt(quantityField.getText());

        boolean success = bookService.addBook(
                titleField.getText(),
                selectedAuthor.getId(),
                selectedCategory.getId(),
                isbnField.getText(),
                quantity);

        if (success) {
            AlertHelper.showSuccess("Livre ajouté avec succès");
            handleClear();
            loadBooks();
        } else {
            AlertHelper.showError("Erreur lors de l'ajout du livre");
        }
    }

    @FXML
    private void handleUpdate() {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            AlertHelper.showError("Veuillez sélectionner un livre à modifier");
            return;
        }

        if (!validateFields()) {
            AlertHelper.showError("Veuillez remplir tous les champs correctement");
            return;
        }

        Author selectedAuthor = authorCombo.getValue();
        Category selectedCategory = categoryCombo.getValue();
        int quantity = Integer.parseInt(quantityField.getText());

        boolean success = bookService.updateBook(
                selectedBook.getId(),
                titleField.getText(),
                selectedAuthor.getId(),
                selectedCategory.getId(),
                isbnField.getText(),
                quantity,
                selectedBook.getAvailable());

        if (success) {
            AlertHelper.showSuccess("Livre modifié avec succès");
            handleClear();
            loadBooks();
        } else {
            AlertHelper.showError("Erreur lors de la modification");
        }
    }

    @FXML
    private void handleDelete() {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            AlertHelper.showError("Veuillez sélectionner un livre à supprimer");
            return;
        }

        boolean confirm = AlertHelper.showConfirmation(
                "Confirmation",
                "Voulez-vous vraiment supprimer ce livre ?");

        if (confirm) {
            boolean success = bookService.deleteBook(selectedBook.getId());
            if (success) {
                AlertHelper.showSuccess("Livre supprimé avec succès");
                handleClear();
                loadBooks();
            } else {
                AlertHelper.showError("Erreur lors de la suppression");
            }
        }
    }

    @FXML
    private void handleRefresh() {
        loadBooks();
        loadAuthors();
        loadCategories();
    }

    @FXML
    private void handleClear() {
        titleField.clear();
        isbnField.clear();
        quantityField.clear();
        authorCombo.setValue(null);
        categoryCombo.setValue(null);
        booksTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleBack() {
        SceneManager.goToDashboard();
    }

    @FXML
    private void handleDashboard() {
        SceneManager.goToDashboard();
    }

    @FXML
    private void handleBooks() {
    }

    @FXML
    private void handleAuthors() {
        SceneManager.goToAuthors();
    }

    @FXML
    private void handleMembers() {
        SceneManager.goToMembers();
    }

    @FXML
    private void handleLoans() {
        SceneManager.goToLoans();
    }

    @FXML
    private void handleCategories() {
        SceneManager.goToCategories();
    }

    @FXML
    private void handleLogout() {
        com.bookmaster.utils.SessionManager.logout();
    }

    private boolean validateFields() {
        return !titleField.getText().trim().isEmpty() &&
                !isbnField.getText().trim().isEmpty() &&
                authorCombo.getValue() != null &&
                categoryCombo.getValue() != null &&
                !quantityField.getText().trim().isEmpty() &&
                isNumeric(quantityField.getText());
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
