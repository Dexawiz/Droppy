package com.example.droppy.service;

import atlantafx.base.theme.CupertinoDark;
import atlantafx.base.theme.CupertinoLight;
import javafx.application.Application;
import lombok.Getter;

public class ThemeStyles {

//    styly su ziskane z https://github.com/mkpaz/atlantafx

    //premenna kde sa uchovava ci toggle button bol zakliknuty alebo nie
    @Getter
    private static boolean DarkMode = false;

    //aktualizuje styl aplikacie
    public static void setDarkMode(boolean enabled) {
        DarkMode = enabled;
        //true == dark mode, false==light mode
        //user agent style automaticky aktualizuje UI komponenty v celej aplikacii
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
