package com.example.droppy.service;

import atlantafx.base.theme.CupertinoDark;
import atlantafx.base.theme.CupertinoLight;
import javafx.application.Application;
import lombok.Getter;

public class ThemeStyles {

    @Getter
    private static boolean DarkMode = false;

        public static void setDarkMode(boolean enabled) {
            DarkMode = enabled;
        if (DarkMode) {
            Application.setUserAgentStylesheet(
                    new CupertinoDark().getUserAgentStylesheet()
            );
        } else {
            Application.setUserAgentStylesheet(
                    new CupertinoLight().getUserAgentStylesheet()
            );
        }
    }
}
