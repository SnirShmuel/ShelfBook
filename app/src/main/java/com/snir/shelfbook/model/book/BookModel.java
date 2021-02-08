package com.snir.shelfbook.model.book;

import android.os.AsyncTask;

import com.snir.shelfbook.model.AppLocalDb;

import java.util.List;

public class BookModel {
    List<Book> data;

    public final static BookModel instance = new BookModel();
    public interface Listener<T>{
        void onComplete(T data);
    }
    public interface CompListener{
        void onComplete();
    }
    private BookModel(){

    }

    public List<Book> getAllBooks(){
        data = AppLocalDb.db.bookDao().getAllBooks();
        return data;
    }

    public void addBook(Book book){
        AppLocalDb.db.bookDao().insertAll(book);
    }

    public void deleteBook(Book book){}

    public void updateBook(Book book){}
}
