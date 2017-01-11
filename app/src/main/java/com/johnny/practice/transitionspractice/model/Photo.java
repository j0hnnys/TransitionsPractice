package com.johnny.practice.transitionspractice.model;

import java.util.UUID;

public class Photo {

    public final String id;

    public Photo() {
        id = UUID.randomUUID().toString();
    }
}
