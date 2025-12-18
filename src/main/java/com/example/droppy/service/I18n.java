package com.example.droppy.service;

import java.util.Locale;
import java.util.ResourceBundle;

public class I18n {

    //nacitava texty z .properties
    private static ResourceBundle bundle;


    public static void setLocale(Locale locale) {
        bundle = ResourceBundle.getBundle(
                // base name suborov
                "messages",
                //
                locale,
                ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_PROPERTIES)
        );
    }

    public static String get(String key) {
        return bundle.getString(key);
    }

}
