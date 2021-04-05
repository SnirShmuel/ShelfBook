package com.snir.shelfbook.model.user;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
//If change something (add or delete) change the version in AppLocalDbRepository
@Entity
public class User {
    @PrimaryKey
    @NonNull
    private String id;
    private String username;
    private String name;
    private String email;
    private long lastUpdated;

    public User() {

    }

    public User(String id, String username, String name, String email,long lastUpdated) {
        this.setId(id);
        this.setUsername(username);
        this.setName(name);
        this.setEmail(email);
        this.setLastUpdated(lastUpdated);
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }
}