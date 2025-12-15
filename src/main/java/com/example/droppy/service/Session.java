package com.example.droppy.service;

import com.example.droppy.domain.entity.User;
import com.example.droppy.domain.enums.Language;
import lombok.Getter;
import lombok.Setter;

public class Session {

    @Setter
    @Getter
    private static volatile User currentUser;
    @Setter
    @Getter
    private static Language currentLanguage = Language.EN;


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
