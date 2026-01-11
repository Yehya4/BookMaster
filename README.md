# BookMaster

BookMaster est une application de gestion de bibliothèque moderne et intuitive développée en Java avec JavaFX. Elle permet aux bibliothécaires de gérer facilement les livres, les membres, les prêts et les catégories via une interface utilisateur graphique élégante.

## Fonctionnalités

*   **Authentification Sécurisée** : Système de connexion pour les administrateurs.
*   **Gestion des Livres** : Ajout, modification, suppression et listage des livres.
*   **Gestion des Catégories** : Organisation des livres par catégories.
*   **Gestion des Membres** : Suivi des informations des membres de la bibliothèque.
*   **Système de Prêts** : Enregistrement des emprunts et des retours de livres.
*   **Dashboard** : Vue d'ensemble des statistiques de la bibliothèque.
*   **Persistance des Données** : Support pour base de données MySQL et gestion de fichiers CSV.

## Technologies Utilisées

*   **Langage** : Java 17
*   **Interface Graphique** : JavaFX 17.0.2
*   **Base de Données** : MySQL 8.4.0
*   **Gestion de Dépendances** : Maven
*   **Architecture** : MVC (Modèle-Vue-Contrôleur)

## Prérequis

*   [Java JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) ou supérieur.
*   [Maven](https://maven.apache.org/) (pour la gestion du projet).
*   [MySQL Server](https://dev.mysql.com/downloads/mysql/) (optionnel si le mode CSV est utilisé, mais recommandé).

## Connexion Administrateur

Un compte administrateur par défaut est créé automatiquement au premier lancement :

*   **Identifiant** : `admin`
*   **Mot de passe** : `admin123`

## Structure du Projet

*   `src/main/java/com/bookmaster` : Code source Java.
    *   `controllers` : Contrôleurs JavaFX.
    *   `models` : Classes modèles (Book, Member, etc.).
    *   `dao` : Objets d'accès aux données.
    *   `services` : Logique métier.
    *   `utils` : Utilitaires (Connexion DB, Export CSV).
*   `src/main/resources` : Ressources (Vues FXML, CSS, Images).
