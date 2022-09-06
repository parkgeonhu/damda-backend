package com.sikugeon.damda.core.oauth.application;

public enum Provider {
    GOOGLE("google");

    String name;

    Provider(String name) {
        this.name = name;
    }
}
