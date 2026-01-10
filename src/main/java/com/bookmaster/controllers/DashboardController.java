package com.bookmaster.controllers;

import com.bookmaster.utils.SceneManager;
import com.bookmaster.utils.SessionManager;
import javafx.fxml.FXML;

public class DashboardController {

    @FXML
    public void initialize() {
        // Initialisation si nécessaire
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
        SceneManager.goToCategories();
    }

    @FXML
    private void handleLogout() {
        SessionManager.logout();
    }
}
