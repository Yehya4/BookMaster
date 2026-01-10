package com.bookmaster.services;

import com.bookmaster.dao.CategoryDAO;
import com.bookmaster.models.Category;

import java.util.List;

public class CategoryService {

    private CategoryDAO categoryDAO;

    public CategoryService() {
        this.categoryDAO = new CategoryDAO();
    }

    public List<Category> getAllCategories() {
        return categoryDAO.findAll();
    }

    public Category getCategoryById(int id) {
        return categoryDAO.findById(id);
    }

    public boolean addCategory(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        Category category = new Category(0, name);
        return categoryDAO.save(category);
    }

    public boolean updateCategory(int id, String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        Category category = categoryDAO.findById(id);
        if (category == null) {
            return false;
        }

        category.setName(name);

        return categoryDAO.update(category);
    }

    public boolean deleteCategory(int id) {
        return categoryDAO.delete(id);
    }
}
