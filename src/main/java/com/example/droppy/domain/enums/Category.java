package com.example.droppy.domain.enums;

import com.example.droppy.service.I18n;

import java.util.List;

public enum Category {
    PIZZA("category.PIZZA"),
    SUSHI("category.SUSHI"),
    DESSERT("category.DESSERT"),
    DRINKS("category.DRINKS"),
    RESTAURANT("category.RESTAURANT"),
    GROCERY("category.GROCERY"),
    PHARMACY("category.PHARMACY"),
    OTHER("category.OTHER");

    private final String key;

    Category(String key) {
        this.key = key;
    }

    public String getTranslated() {
        String value = I18n.get(key);
        if (value.isEmpty()) return this.name();
        return value;
    }

    public static List<Category> getAllCategories() {
        return List.of(Category.values());
    }
}
