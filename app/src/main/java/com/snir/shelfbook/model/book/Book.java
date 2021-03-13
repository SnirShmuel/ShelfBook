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
    private String genre;
    private String imageUrl;
    private String description;
    private String ownerId;
    private Boolean isGiven;
    private long lastUpdated;

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", this.getId());
        result.put("name", this.getName());
        result.put("bookCondition", this.getBookCondition());
        result.put("genre", this.getGenre());
        result.put("imageUrl", this.getImageUrl());
        result.put("description", this.getDescription());
        result.put("ownerId", this.getOwnerId());
        result.put("isGiven", this.getGiven());
        result.put("lastUpdated", FieldValue.serverTimestamp());
        return result;
    }

    public void fromMap(Map<String, Object> map) {
        Book bk = new Book();
        bk.setId((String) Objects.requireNonNull(map.get("id")));
        bk.setName((String)map.get("name"));
        bk.setBookCondition((String)map.get("bookCondition"));
        bk.setGenre((String)map.get("genre"));
        bk.setImageUrl((String)map.get("imageUrl"));
        bk.setDescription((String)map.get("description"));
        bk.setOwnerId((String)map.get("ownerId"));
        bk.setGiven((Boolean)map.get("isGiven"));
        Timestamp ts = (Timestamp)map.get("lastUpdated");
        if (bk != null) bk.setLastUpdated(ts.getSeconds());
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
