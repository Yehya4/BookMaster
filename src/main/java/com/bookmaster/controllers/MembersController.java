package com.bookmaster.controllers;

import com.bookmaster.models.Member;
import com.bookmaster.services.MemberService;
import com.bookmaster.utils.AlertHelper;
import com.bookmaster.utils.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;

public class MembersController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private TableView<Member> membersTable;

    private MemberService memberService;
    private ObservableList<Member> membersList;

    public MembersController() {
        memberService = new MemberService();
    }

    @FXML
    public void initialize() {
        loadMembers();

        membersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                fillFields(newVal);
            }
        });
    }

    private void loadMembers() {
        List<Member> members = memberService.getAllMembers();
        membersList = FXCollections.observableArrayList(members);
        membersTable.setItems(membersList);
    }

    private void fillFields(Member member) {
        nameField.setText(member.getName());
        emailField.setText(member.getEmail());
        phoneField.setText(member.getPhone());
    }

    @FXML
    private void handleAdd() {
        if (!validateFields()) {
            AlertHelper.showError("Veuillez remplir tous les champs correctement");
            return;
        }

        boolean success = memberService.addMember(
                nameField.getText(),
                emailField.getText(),
                phoneField.getText());

        if (success) {
            AlertHelper.showSuccess("Membre ajouté avec succès");
            handleClear();
            loadMembers();
        } else {
            AlertHelper.showError("Erreur lors de l'ajout du membre");
        }
    }

    @FXML
    private void handleUpdate() {
        Member selectedMember = membersTable.getSelectionModel().getSelectedItem();
        if (selectedMember == null) {
            AlertHelper.showError("Veuillez sélectionner un membre à modifier");
            return;
        }

        if (!validateFields()) {
            AlertHelper.showError("Veuillez remplir tous les champs correctement");
            return;
        }

        boolean success = memberService.updateMember(
                selectedMember.getId(),
                nameField.getText(),
                emailField.getText(),
                phoneField.getText());

        if (success) {
            AlertHelper.showSuccess("Membre modifié avec succès");
            handleClear();
            loadMembers();
        } else {
            AlertHelper.showError("Erreur lors de la modification");
        }
    }

    @FXML
    private void handleDelete() {
        Member selectedMember = membersTable.getSelectionModel().getSelectedItem();
        if (selectedMember == null) {
            AlertHelper.showError("Veuillez sélectionner un membre à supprimer");
            return;
        }

        boolean confirm = AlertHelper.showConfirmation(
                "Confirmation",
                "Voulez-vous vraiment supprimer ce membre ?");

        if (confirm) {
            boolean success = memberService.deleteMember(selectedMember.getId());
            if (success) {
                AlertHelper.showSuccess("Membre supprimé avec succès");
                handleClear();
                loadMembers();
            } else {
                AlertHelper.showError("Erreur lors de la suppression");
            }
        }
    }

    @FXML
    private void handleRefresh() {
        loadMembers();
    }

    @FXML
    private void handleClear() {
        nameField.clear();
        emailField.clear();
        phoneField.clear();
        membersTable.getSelectionModel().clearSelection();
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
                !emailField.getText().trim().isEmpty() &&
                emailField.getText().contains("@") &&
                !phoneField.getText().trim().isEmpty();
    }
}
