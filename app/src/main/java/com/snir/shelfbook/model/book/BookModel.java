package com.snir.shelfbook.model.book;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;


import androidx.lifecycle.LiveData;


import com.snir.shelfbook.MyApplication;
import com.snir.shelfbook.model.AppLocalDb;

import java.util.List;


public class BookModel {
    LiveData<List<Book>> liveData;


    public final static BookModel instance = new BookModel();
    public interface Listener<T>{
        void onComplete(T data);
    }
    public interface CompListener{
        void onComplete();
    }
    private BookModel(){

    }

    public LiveData<List<Book>> getAllBooks(){
        liveData = AppLocalDb.db.bookDao().getAllBooks();
        refreshBookList(null);
        return liveData;
    }

    public void addBook(Book book, Listener<Boolean> listener){
        BookFirebase.addBook(book, listener);
        BookSql.addBook(book, null);
    }

    public void deleteBook(Book book, Listener<Boolean> listener){
        deleteImage(book.getId(), new Listener<Boolean>() {
            @Override
            public void onComplete(Boolean data) {

            }
        });
        BookFirebase.deleteBook(book.getId(),listener);
        BookSql.deleteBook(book,null);
    }

    public void updateBook(Book book, Listener<Boolean> listener){
        BookFirebase.addBook(book, listener);
        BookSql.addBook(book,null);
    }

    public void refreshBookList(final CompListener listener){
        //1. get local last update date
        final SharedPreferences sp = MyApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);
        Long lastUpdated = sp.getLong("lastUpdated",0);
        //2. get all updated record from firebase from the last update date
        BookFirebase.getAllBooks(lastUpdated, new Listener<List<Book>>() {
                    @Override
                    public void onComplete(List<Book> data) {
                        //3. insert the new updates to the local db
                        Long lastU = Long.valueOf(0);
                        for (Book book: data) {
                            BookSql.addBook(book,null);
                            if (book.getLastUpdated()>lastU){
                                lastU = book.getLastUpdated();
                            }
                        }
                        //4. update the local last update date
                        sp.edit().putLong("lastUpdated", lastU).commit();
                        //5. return the updates data to the listeners
                        if(listener != null){
                            listener.onComplete();
                        }
                    }
                });
    }

    public void uploadImage(Bitmap imageBmp, String name, final BookModel.Listener<String> listener){
        BookFirebase.uploadImage(imageBmp,name,listener);
    }

    public void deleteImage(String name,final BookModel.Listener<Boolean> listener){
        BookFirebase.deleteImage(name,listener);
    }

}
