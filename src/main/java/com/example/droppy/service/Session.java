package com.example.droppy.service;

import com.example.droppy.domain.entity.User;
import lombok.Getter;
import lombok.Setter;

public class Session {

    @Setter
    @Getter
    private static volatile User currentUser;

    public static void setLoggedUser(User user) {
        currentUser = user;
    }

    public static User getLoggedUser() {
        return currentUser;
    }

    public static void logout() {
        currentUser = null;
    }

}
