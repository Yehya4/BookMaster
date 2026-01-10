package com.bookmaster.controllers;

import com.bookmaster.models.User;
import com.bookmaster.services.AuthenticationService;
import com.bookmaster.utils.SceneManager;
import com.bookmaster.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private AuthenticationService authService;

    public LoginController() {
        authService = new AuthenticationService();
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = authService.login(username, password);

        if (user != null) {

            SessionManager.setCurrentUser(user);
            SceneManager.goToDashboard();
        } else {

            errorLabel.setText("Nom d'utilisateur ou mot de passe incorrect");
            errorLabel.setVisible(true);
        }
    }

}
