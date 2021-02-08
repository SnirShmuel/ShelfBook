package com.snir.shelfbook.model;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.snir.shelfbook.model.book.Book;
import com.snir.shelfbook.model.book.BookDao;


@Database(entities = {Book.class}, version = 1)
public abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract BookDao bookDao();
}