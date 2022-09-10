package com.sikugeon.damda.core.oauth.application;

public enum Provider {
    GOOGLE("google"), TEST("TEST");

    String name;

    Provider(String name) {
        this.name = name;
    }
}
