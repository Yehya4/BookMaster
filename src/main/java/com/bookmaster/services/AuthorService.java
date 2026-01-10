package com.bookmaster.services;

import com.bookmaster.dao.AuthorDAO;
import com.bookmaster.models.Author;

import java.util.List;

public class AuthorService {

    private AuthorDAO authorDAO;

    public AuthorService() {
        this.authorDAO = new AuthorDAO();
    }

    public List<Author> getAllAuthors() {
        return authorDAO.findAll();
    }

    public Author getAuthorById(int id) {
        return authorDAO.findById(id);
    }

    public boolean addAuthor(String name, String nationality) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        if (nationality == null || nationality.trim().isEmpty()) {
            return false;
        }

        Author author = new Author(name, nationality);
        return authorDAO.save(author);
    }

    public boolean updateAuthor(int id, String name, String nationality) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        if (nationality == null || nationality.trim().isEmpty()) {
            return false;
        }

        Author author = authorDAO.findById(id);
        if (author == null) {
            return false;
        }

        author.setName(name);
        author.setNationality(nationality);

        return authorDAO.update(author);
    }

    public boolean deleteAuthor(int id) {
        return authorDAO.delete(id);
    }
}
