package com.bookmaster.controllers;

import com.bookmaster.models.Book;
import com.bookmaster.models.Loan;
import com.bookmaster.models.Member;
import com.bookmaster.services.BookService;
import com.bookmaster.services.LoanService;
import com.bookmaster.services.MemberService;
import com.bookmaster.utils.AlertHelper;
import com.bookmaster.utils.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;

public class LoansController {

    @FXML
    private ComboBox<Book> bookCombo;

    @FXML
    private ComboBox<Member> memberCombo;

    @FXML
    private TextField daysField;

    @FXML
    private TableView<Loan> loansTable;

    @FXML
    private CheckBox activeOnlyCheckbox;

    private LoanService loanService;
    private BookService bookService;
    private MemberService memberService;
    private ObservableList<Loan> loansList;

    public LoansController() {
        loanService = new LoanService();
        bookService = new BookService();
        memberService = new MemberService();
    }

    @FXML
    public void initialize() {
        loadBooks();
        loadMembers();
        loadLoans();
    }

    private void loadBooks() {
        List<Book> books = bookService.getAvailableBooks();
        bookCombo.setItems(FXCollections.observableArrayList(books));
    }

    private void loadMembers() {
        List<Member> members = memberService.getAllMembers();
        memberCombo.setItems(FXCollections.observableArrayList(members));
    }

    private void loadLoans() {
        List<Loan> loans;

        if (activeOnlyCheckbox.isSelected()) {
            loans = loanService.getActiveLoans();
        } else {
            loans = loanService.getAllLoans();
        }

        loansList = FXCollections.observableArrayList(loans);
        loansTable.setItems(loansList);
    }

    @FXML
    private void handleBorrow() {
        if (!validateFields()) {
            AlertHelper.showError("Veuillez sélectionner un livre et un membre");
            return;
        }

        Book selectedBook = bookCombo.getValue();
        Member selectedMember = memberCombo.getValue();
        int days;

        try {
            days = Integer.parseInt(daysField.getText());
            if (days <= 0) {
                AlertHelper.showError("La durée doit être positive");
                return;
            }
        } catch (NumberFormatException e) {
            AlertHelper.showError("Durée invalide");
            return;
        }

        boolean success = loanService.borrowBook(
                selectedBook.getId(),
                selectedMember.getId(),
                days);

        if (success) {
            AlertHelper.showSuccess("Livre emprunté avec succès");
            loadBooks();
            loadLoans();
            clearFields();
        } else {
            AlertHelper.showError("Erreur lors de l'emprunt");
        }
    }

    @FXML
    private void handleReturn() {
        Loan selectedLoan = loansTable.getSelectionModel().getSelectedItem();
        if (selectedLoan == null) {
            AlertHelper.showError("Veuillez sélectionner un emprunt");
            return;
        }

        if (selectedLoan.isReturned()) {
            AlertHelper.showError("Ce livre a déjà été retourné");
            return;
        }

        boolean success = loanService.returnBook(selectedLoan.getId());

        if (success) {
            AlertHelper.showSuccess("Livre retourné avec succès");
            loadBooks();
            loadLoans();
        } else {
            AlertHelper.showError("Erreur lors du retour");
        }
    }

    @FXML
    private void handleDelete() {
        Loan selectedLoan = loansTable.getSelectionModel().getSelectedItem();
        if (selectedLoan == null) {
            AlertHelper.showError("Veuillez sélectionner un emprunt à supprimer");
            return;
        }

        boolean confirm = AlertHelper.showConfirmation(
                "Confirmation",
                "Voulez-vous vraiment supprimer cet emprunt ?");

        if (confirm) {
            boolean success = loanService.deleteLoan(selectedLoan.getId());
            if (success) {
                AlertHelper.showSuccess("Emprunt supprimé avec succès");
                loadBooks();
                loadLoans();
            } else {
                AlertHelper.showError("Erreur lors de la suppression");
            }
        }
    }

    @FXML
    private void handleRefresh() {
        loadBooks();
        loadMembers();
        loadLoans();
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
        return bookCombo.getValue() != null &&
                memberCombo.getValue() != null &&
                !daysField.getText().trim().isEmpty();
    }

    private void clearFields() {
        bookCombo.setValue(null);
        memberCombo.setValue(null);
        daysField.setText("14");
    }
}
