package com.bookmaster.controllers;

import com.bookmaster.models.Author;
import com.bookmaster.services.AuthorService;
import com.bookmaster.utils.AlertHelper;
import com.bookmaster.utils.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;

public class AuthorsController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField nationalityField;

    @FXML
    private TableView<Author> authorsTable;

    private AuthorService authorService;
    private ObservableList<Author> authorsList;

    public AuthorsController() {
        authorService = new AuthorService();
    }

    @FXML
    public void initialize() {
        loadAuthors();

        authorsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                fillFields(newVal);
            }
        });
    }

    private void loadAuthors() {
        List<Author> authors = authorService.getAllAuthors();
        authorsList = FXCollections.observableArrayList(authors);
        authorsTable.setItems(authorsList);
    }

    private void fillFields(Author author) {
        nameField.setText(author.getName());
        nationalityField.setText(author.getNationality());
    }

    @FXML
    private void handleAdd() {
        if (!validateFields()) {
            AlertHelper.showError("Veuillez remplir tous les champs");
            return;
        }

        boolean success = authorService.addAuthor(
                nameField.getText(),
                nationalityField.getText());

        if (success) {
            AlertHelper.showSuccess("Auteur ajouté avec succès");
            handleClear();
            loadAuthors();
        } else {
            AlertHelper.showError("Erreur lors de l'ajout de l'auteur");
        }
    }

    @FXML
    private void handleUpdate() {
        Author selectedAuthor = authorsTable.getSelectionModel().getSelectedItem();
        if (selectedAuthor == null) {
            AlertHelper.showError("Veuillez sélectionner un auteur à modifier");
            return;
        }

        if (!validateFields()) {
            AlertHelper.showError("Veuillez remplir tous les champs");
            return;
        }

        boolean success = authorService.updateAuthor(
                selectedAuthor.getId(),
                nameField.getText(),
                nationalityField.getText());

        if (success) {
            AlertHelper.showSuccess("Auteur modifié avec succès");
            handleClear();
            loadAuthors();
        } else {
            AlertHelper.showError("Erreur lors de la modification");
        }
    }

    @FXML
    private void handleDelete() {
        Author selectedAuthor = authorsTable.getSelectionModel().getSelectedItem();
        if (selectedAuthor == null) {
            AlertHelper.showError("Veuillez sélectionner un auteur à supprimer");
            return;
        }

        boolean confirm = AlertHelper.showConfirmation(
                "Confirmation",
                "Voulez-vous vraiment supprimer cet auteur ?");

        if (confirm) {
            boolean success = authorService.deleteAuthor(selectedAuthor.getId());
            if (success) {
                AlertHelper.showSuccess("Auteur supprimé avec succès");
                handleClear();
                loadAuthors();
            } else {
                AlertHelper.showError("Erreur lors de la suppression");
            }
        }
    }

    @FXML
    private void handleRefresh() {
        loadAuthors();
    }

    @FXML
    private void handleClear() {
        nameField.clear();
        nationalityField.clear();
        authorsTable.getSelectionModel().clearSelection();
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
        SceneManager.goToBooks();
    }

    @FXML
    private void handleAuthors() {
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
        return !nameField.getText().trim().isEmpty() &&
                !nationalityField.getText().trim().isEmpty();
    }
}
