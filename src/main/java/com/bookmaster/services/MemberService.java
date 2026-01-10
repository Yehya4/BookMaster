package com.bookmaster.services;

import com.bookmaster.dao.MemberDAO;
import com.bookmaster.models.Member;

import java.util.List;

public class MemberService {

    private MemberDAO memberDAO;

    public MemberService() {
        this.memberDAO = new MemberDAO();
    }

    public List<Member> getAllMembers() {
        return memberDAO.findAll();
    }

    public Member getMemberById(int id) {
        return memberDAO.findById(id);
    }

    public boolean addMember(String name, String email, String phone) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            return false;
        }

        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }

        Member member = new Member(name, email, phone);
        return memberDAO.save(member);
    }

    public boolean updateMember(int id, String name, String email, String phone) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            return false;
        }

        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }

        Member member = memberDAO.findById(id);
        if (member == null) {
            return false;
        }

        member.setName(name);
        member.setEmail(email);
        member.setPhone(phone);

        return memberDAO.update(member);
    }

    public boolean deleteMember(int id) {
        return memberDAO.delete(id);
    }
}
