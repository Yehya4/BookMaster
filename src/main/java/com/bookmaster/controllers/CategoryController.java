package com.bookmaster.controllers;

import com.bookmaster.models.Category;
import com.bookmaster.services.CategoryService;
import com.bookmaster.utils.AlertHelper;
import com.bookmaster.utils.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;

public class CategoryController {

    @FXML
    private TextField nameField;

    @FXML
    private TableView<Category> categoriesTable;

    private CategoryService categoryService;
    private ObservableList<Category> categoriesList;

    public CategoryController() {
        categoryService = new CategoryService();
    }

    @FXML
    public void initialize() {
        loadCategories();

        categoriesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                fillFields(newVal);
            }
        });
    }

    private void loadCategories() {
        List<Category> categories = categoryService.getAllCategories();
        categoriesList = FXCollections.observableArrayList(categories);
        categoriesTable.setItems(categoriesList);
    }

    private void fillFields(Category category) {
        nameField.setText(category.getName());
    }

    @FXML
    private void handleAdd() {
        if (!validateFields()) {
            AlertHelper.showError("Veuillez remplir tous les champs");
            return;
        }

        boolean success = categoryService.addCategory(nameField.getText());

        if (success) {
            AlertHelper.showSuccess("Catégorie ajoutée avec succès");
            handleClear();
            loadCategories();
        } else {
            AlertHelper.showError("Erreur lors de l'ajout de la catégorie");
        }
    }

    @FXML
    private void handleUpdate() {
        Category selectedCategory = categoriesTable.getSelectionModel().getSelectedItem();
        if (selectedCategory == null) {
            AlertHelper.showError("Veuillez sélectionner une catégorie à modifier");
            return;
        }

        if (!validateFields()) {
            AlertHelper.showError("Veuillez remplir tous les champs");
            return;
        }

        boolean success = categoryService.updateCategory(
                selectedCategory.getId(),
                nameField.getText());

        if (success) {
            AlertHelper.showSuccess("Catégorie modifiée avec succès");
            handleClear();
            loadCategories();
        } else {
            AlertHelper.showError("Erreur lors de la modification");
        }
    }

    @FXML
    private void handleDelete() {
        Category selectedCategory = categoriesTable.getSelectionModel().getSelectedItem();
        if (selectedCategory == null) {
            AlertHelper.showError("Veuillez sélectionner une catégorie à supprimer");
            return;
        }

        boolean confirm = AlertHelper.showConfirmation(
                "Confirmation",
                "Voulez-vous vraiment supprimer cette catégorie ?");

        if (confirm) {
            boolean success = categoryService.deleteCategory(selectedCategory.getId());
            if (success) {
                AlertHelper.showSuccess("Catégorie supprimée avec succès");
                handleClear();
                loadCategories();
            } else {
                AlertHelper.showError("Erreur lors de la suppression");
            }
        }
    }

    @FXML
    private void handleRefresh() {
        loadCategories();
    }

    @FXML
    private void handleClear() {
        nameField.clear();
        categoriesTable.getSelectionModel().clearSelection();
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
    }

    @FXML
    private void handleLogout() {
        com.bookmaster.utils.SessionManager.logout();
    }

    private boolean validateFields() {
        return !nameField.getText().trim().isEmpty();
    }
}
