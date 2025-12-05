package com.example.droppy.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class I18n {

    private static ResourceBundle bundle;

    public static void setLocale(Locale locale) {
            bundle = ResourceBundle.getBundle(
                    "messages", // base name súborov
                    locale,
                    ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_PROPERTIES)
            );
    }

    public static String get(String key) {
        return bundle.getString(key);
    }
}
