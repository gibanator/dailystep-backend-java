package com.gibanator.dailystepbackendjava.category.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Long id){
        super("Category with id " + id + " not found.");
    }
}
