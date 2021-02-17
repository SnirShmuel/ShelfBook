package com.snir.shelfbook.model.user;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey
    @NonNull
    public String id;
    public String username;
    public String name;
    public String email;
    public long lastUpdated;

    public User() {
    }

    public User(String id, String username, String name, String email) {
        this.id = id;
        this.username = username;
        this.name =name;
        this.email = email;
    }
}