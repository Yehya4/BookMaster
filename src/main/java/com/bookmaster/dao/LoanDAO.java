package com.bookmaster.dao;

import com.bookmaster.models.Loan;
import com.bookmaster.utils.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {

    public List<Loan> findAll() {
        List<Loan> loans = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, book_id, member_id, loan_date, return_date, returned FROM loans")) {
            while (rs.next()) {
                loans.add(map(rs));
            }
        } catch (Exception e) {
            System.err.println("Erreur findAll loans: " + e.getMessage());
        }
        enrich(loans);
        return loans;
    }

    public Loan findById(int id) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id, book_id, member_id, loan_date, return_date, returned FROM loans WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Loan l = map(rs);
                    enrichOne(l);
                    return l;
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur findById loan: " + e.getMessage());
        }
        return null;
    }

    public List<Loan> findActive() {
        List<Loan> loans = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id, book_id, member_id, loan_date, return_date, returned FROM loans WHERE returned = FALSE")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    loans.add(map(rs));
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur findActive loans: " + e.getMessage());
        }
        enrich(loans);
        return loans;
    }

    public List<Loan> findByMemberId(int memberId) {
        List<Loan> loans = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id, book_id, member_id, loan_date, return_date, returned FROM loans WHERE member_id=?")) {
            ps.setInt(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    loans.add(map(rs));
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur findByMemberId loans: " + e.getMessage());
        }
        enrich(loans);
        return loans;
    }

    public List<Loan> findByBookId(int bookId) {
        List<Loan> loans = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id, book_id, member_id, loan_date, return_date, returned FROM loans WHERE book_id=?")) {
            ps.setInt(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    loans.add(map(rs));
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur findByBookId loans: " + e.getMessage());
        }
        enrich(loans);
        return loans;
    }

    public boolean save(Loan loan) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO loans (book_id, member_id, loan_date, return_date, returned) VALUES (?, ?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, loan.getBookId());
            ps.setInt(2, loan.getMemberId());
            ps.setDate(3, java.sql.Date.valueOf(loan.getLoanDate()));
            if (loan.getReturnDate() == null) {
                ps.setNull(4, java.sql.Types.DATE);
            } else {
                ps.setDate(4, java.sql.Date.valueOf(loan.getReturnDate()));
            }
            ps.setBoolean(5, loan.isReturned());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        loan.setId(keys.getInt(1));
                    }
                }
                return true;
            }
        } catch (Exception e) {
            System.err.println("Erreur save loan: " + e.getMessage());
        }
        return false;
    }

    public boolean update(Loan loan) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE loans SET book_id=?, member_id=?, loan_date=?, return_date=?, returned=? WHERE id=?")) {
            ps.setInt(1, loan.getBookId());
            ps.setInt(2, loan.getMemberId());
            ps.setDate(3, java.sql.Date.valueOf(loan.getLoanDate()));
            if (loan.getReturnDate() == null) {
                ps.setNull(4, java.sql.Types.DATE);
            } else {
                ps.setDate(4, java.sql.Date.valueOf(loan.getReturnDate()));
            }
            ps.setBoolean(5, loan.isReturned());
            ps.setInt(6, loan.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Erreur update loan: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM loans WHERE id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Erreur delete loan: " + e.getMessage());
            return false;
        }
    }

    public boolean markAsReturned(int id) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE loans SET return_date=?, returned=? WHERE id=?")) {
            ps.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            ps.setBoolean(2, true);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Erreur markAsReturned: " + e.getMessage());
            return false;
        }
    }

    private Loan map(ResultSet rs) throws SQLException {
        Loan loan = new Loan(
                rs.getInt("id"),
                rs.getInt("book_id"),
                rs.getInt("member_id"),
                rs.getDate("loan_date").toLocalDate(),
                rs.getDate("return_date") == null ? null : rs.getDate("return_date").toLocalDate(),
                rs.getBoolean("returned"));
        return loan;
    }

    private void enrich(List<Loan> loans) {
        BookDAO bookDAO = new BookDAO();
        MemberDAO memberDAO = new MemberDAO();
        for (Loan loan : loans) {
            var book = bookDAO.findById(loan.getBookId());
            if (book != null) loan.setBookTitle(book.getTitle());
            var member = memberDAO.findById(loan.getMemberId());
            if (member != null) loan.setMemberName(member.getName());
        }
    }

    private void enrichOne(Loan loan) {
        if (loan == null) return;
        BookDAO bookDAO = new BookDAO();
        MemberDAO memberDAO = new MemberDAO();
        var book = bookDAO.findById(loan.getBookId());
        if (book != null) loan.setBookTitle(book.getTitle());
        var member = memberDAO.findById(loan.getMemberId());
        if (member != null) loan.setMemberName(member.getName());
    }
}
