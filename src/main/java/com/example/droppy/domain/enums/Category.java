package com.example.droppy.domain.enums;

import java.util.List;

public enum Category {
    PIZZA,
    SUSHI,
    DESSERT,
    DRINKS,
    RESTAURANT,
    GROCERY,
    PHARMACY,
    OTHER;

    public static List<Category> getAllCategories() {
        return List.of(Category.values());
    }
}
