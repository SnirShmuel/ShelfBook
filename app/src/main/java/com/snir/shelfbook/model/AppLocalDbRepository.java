package com.snir.shelfbook.model;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.snir.shelfbook.model.book.Book;
import com.snir.shelfbook.model.book.BookDao;
import com.snir.shelfbook.model.user.UserDao;


@Database(entities = {Book.class}, version = 2)
public abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract BookDao bookDao();
    public abstract UserDao userDao();
}