package com.example.droppy.domain.enums;

import lombok.Getter;

public enum Language {
    EN("en","English"),
    SK("sk","Slovenčina");

    @Getter
    private String code;
    private String displayName;

    Language(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String toString() {
        return displayName;
    }

}


