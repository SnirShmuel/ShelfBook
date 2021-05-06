package com.snir.shelfbook.model.book;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

//If change something (add or delete) change the version in AppLocalDbRepository
@Entity
@Keep
public class Book implements Serializable {
    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private String bookCondition;
    private String author;
    private String genre;
    private String imageUrl;
    private String description;
    private String ownerId;
    private Boolean isGiven;
    private Boolean isDeleted;
    private Long lastUpdated;


    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", this.getId());
        result.put("name", this.getName());
        result.put("bookCondition", this.getBookCondition());
        result.put("author",this.getAuthor());
        result.put("genre", this.getGenre());
        result.put("imageUrl", this.getImageUrl());
        result.put("description", this.getDescription());
        result.put("ownerId", this.getOwnerId());
        result.put("isGiven", this.getGiven());
        result.put("isDeleted", this.getDeleted());
        result.put("lastUpdated", FieldValue.serverTimestamp());
        return result;
    }

    public void fromMap(Map<String, Object> map) {
        this.setId((String) Objects.requireNonNull(map.get("id")));
        this.setName((String)map.get("name"));
        this.setBookCondition((String)map.get("bookCondition"));
        this.setAuthor((String)map.get("author"));
        this.setGenre((String)map.get("genre"));
        this.setImageUrl((String)map.get("imageUrl"));
        this.setDescription((String)map.get("description"));
        this.setOwnerId((String)map.get("ownerId"));
        this.setGiven((Boolean)map.get("isGiven"));
        this.setDeleted((Boolean)map.get("isDeleted"));
        Timestamp ts = (Timestamp)map.get("lastUpdated");
        this.setLastUpdated(ts.getSeconds());
    }

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

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void setGiven(Boolean given) {
        isGiven = given;
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

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
