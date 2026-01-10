package com.bookmaster;

import com.bookmaster.utils.CSVHelper;
import com.bookmaster.utils.DBInitializer;
import com.bookmaster.utils.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            SceneManager.initialize(primaryStage);

            CSVHelper.initializeDataDirectory();
            System.out.println("Mode CSV activé.");
            DBInitializer.initialize();
            System.out.println("Base MySQL initialisée.");
            DBInitializer.ensureDefaultAdmin();
            System.out.println("Admin par défaut vérifié/créé.");

            SceneManager.goToLogin();

            primaryStage.setWidth(900);
            primaryStage.setHeight(700);
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Erreur lors du démarrage de l'application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
