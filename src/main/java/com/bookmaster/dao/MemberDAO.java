package com.bookmaster.dao;

import com.bookmaster.models.Member;
import com.bookmaster.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {

    public List<Member> findAll() {
        List<Member> members = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, name, email, phone FROM members ORDER BY name")) {
            while (rs.next()) {
                members.add(new Member(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("phone")));
            }
        } catch (Exception e) {
            System.err.println("Erreur findAll members: " + e.getMessage());
        }
        return members;
    }

    public Member findById(int id) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id, name, email, phone FROM members WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Member(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("phone"));
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur findById member: " + e.getMessage());
        }
        return null;
    }

    public boolean save(Member member) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO members (name, email, phone) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, member.getName());
            ps.setString(2, member.getEmail());
            ps.setString(3, member.getPhone());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        member.setId(keys.getInt(1));
                    }
                }
                return true;
            }
        } catch (Exception e) {
            System.err.println("Erreur save member: " + e.getMessage());
        }
        return false;
    }

    public boolean update(Member member) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE members SET name=?, email=?, phone=? WHERE id=?")) {
            ps.setString(1, member.getName());
            ps.setString(2, member.getEmail());
            ps.setString(3, member.getPhone());
            ps.setInt(4, member.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Erreur update member: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM members WHERE id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Erreur delete member: " + e.getMessage());
            return false;
        }
    }
}
