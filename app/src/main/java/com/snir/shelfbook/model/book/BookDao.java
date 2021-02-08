package com.snir.shelfbook.model.book;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BookDao {
    @Query("select * from Book")
    List<Book> getAllBooks();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Book... books);

    @Delete
    void delete(Book book);
}
