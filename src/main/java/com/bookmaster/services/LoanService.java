package com.bookmaster.services;

import com.bookmaster.dao.BookDAO;
import com.bookmaster.dao.LoanDAO;
import com.bookmaster.dao.MemberDAO;
import com.bookmaster.models.Book;
import com.bookmaster.models.Loan;
import com.bookmaster.models.Member;

import java.time.LocalDate;
import java.util.List;

public class LoanService {

    private LoanDAO loanDAO;
    private BookDAO bookDAO;
    private MemberDAO memberDAO;
    private BookService bookService;

    public LoanService() {
        this.loanDAO = new LoanDAO();
        this.bookDAO = new BookDAO();
        this.memberDAO = new MemberDAO();
        this.bookService = new BookService();
    }

    public List<Loan> getAllLoans() {
        List<Loan> loans = loanDAO.findAll();
        enrichLoansWithNames(loans);
        return loans;
    }

    public List<Loan> getActiveLoans() {
        List<Loan> loans = loanDAO.findActive();
        enrichLoansWithNames(loans);
        return loans;
    }

    public boolean borrowBook(int bookId, int memberId, int loanDays) {
        Book book = bookDAO.findById(bookId);
        if (book == null || !book.isAvailable()) {
            return false;
        }

        Member member = memberDAO.findById(memberId);
        if (member == null) {
            return false;
        }

        LocalDate loanDate = LocalDate.now();
        LocalDate returnDate = loanDate.plusDays(loanDays);

        Loan loan = new Loan(bookId, memberId, loanDate, returnDate);

        if (loanDAO.save(loan)) {
            return bookService.decrementAvailability(bookId);
        }

        return false;
    }

    public boolean returnBook(int loanId) {
        Loan loan = loanDAO.findById(loanId);
        if (loan == null || loan.isReturned()) {
            return false;
        }

        loan.setReturned(true);

        if (loanDAO.update(loan)) {
            return bookService.incrementAvailability(loan.getBookId());
        }

        return false;
    }

    public boolean deleteLoan(int id) {
        Loan loan = loanDAO.findById(id);
        if (loan == null) {
            return false;
        }

        if (!loan.isReturned()) {
            bookService.incrementAvailability(loan.getBookId());
        }

        return loanDAO.delete(id);
    }

    private void enrichLoansWithNames(List<Loan> loans) {
        for (Loan loan : loans) {
            Book book = bookDAO.findById(loan.getBookId());
            if (book != null) {
                loan.setBookTitle(book.getTitle());
            }

            Member member = memberDAO.findById(loan.getMemberId());
            if (member != null) {
                loan.setMemberName(member.getName());
            }
        }
    }
}
