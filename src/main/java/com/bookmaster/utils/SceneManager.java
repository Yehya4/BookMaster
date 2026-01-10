package com.bookmaster.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {

    private static Stage primaryStage;

    public static void initialize(Stage stage) {
        primaryStage = stage;
    }

    public static void switchScene(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/views/" + fxmlFile));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle(title);
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la scène: " + fxmlFile);
            e.printStackTrace();
            AlertHelper.showError("Erreur", "Impossible de charger la page: " + fxmlFile);
        }
    }

    public static void goToDashboard() {
        switchScene("dashboard.fxml", "BookMaster - Tableau de Bord");
    }

    public static void goToLogin() {
        switchScene("login.fxml", "BookMaster - Connexion");
    }

    public static void goToBooks() {
        switchScene("books.fxml", "BookMaster - Gestion des Livres");
    }

    public static void goToAuthors() {
        switchScene("authors.fxml", "BookMaster - Gestion des Auteurs");
    }

    public static void goToMembers() {
        switchScene("members.fxml", "BookMaster - Gestion des Membres");
    }

    public static void goToLoans() {
        switchScene("loans.fxml", "BookMaster - Gestion des Emprunts");
    }

    public static void goToCategories() {
        switchScene("categories.fxml", "BookMaster - Gestion des Catégories");
    }
}
