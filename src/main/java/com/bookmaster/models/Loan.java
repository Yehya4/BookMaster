package com.bookmaster.models;

import java.time.LocalDate;

public class Loan {
    private int id;
    private int bookId;
    private int memberId;
    private LocalDate loanDate;
    private LocalDate returnDate;
    private boolean returned;

    private String bookTitle;
    private String memberName;

    public Loan(int id, int bookId, int memberId, LocalDate loanDate, LocalDate returnDate, boolean returned) {
        this.id = id;
        this.bookId = bookId;
        this.memberId = memberId;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.returned = returned;
    }

    public Loan(int bookId, int memberId, LocalDate loanDate, LocalDate returnDate) {
        this.bookId = bookId;
        this.memberId = memberId;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.returned = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public boolean isOverdue() {
        return !returned && LocalDate.now().isAfter(returnDate);
    }

    public String getStatus() {
        if (returned) {
            return "Retourné";
        } else if (isOverdue()) {
            return "En retard";
        } else {
            return "En cours";
        }
    }
}
