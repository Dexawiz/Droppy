package com.example.droppy.domain.enums;

public enum Language {
    EN("en","English"),
    SK("sk","Slovenčina");

    private String code;
    private String displayName;

    Language(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }


    public String getCode() {
        return code;
    }

    public String toString() {
        return displayName;
    }

}


