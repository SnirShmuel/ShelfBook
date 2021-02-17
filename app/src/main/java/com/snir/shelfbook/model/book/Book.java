package com.snir.shelfbook.model.book;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Book {
    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private String bookCondition;
    private String genre;
    private String imageUrl;
    private String description;
    private String ownerId;
    private Boolean isGiven;
    private long lastUpdated;


    @NonNull
    public String getId() {
        return id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public Boolean getGiven() {
        return isGiven;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void setGiven(Boolean given) {
        isGiven = given;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBookCondition() {
        return bookCondition;
    }

    public void setBookCondition(String bookCondition) {
        this.bookCondition = bookCondition;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
