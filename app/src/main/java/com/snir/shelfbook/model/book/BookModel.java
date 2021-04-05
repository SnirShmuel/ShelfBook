package com.snir.shelfbook.model.book;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.snir.shelfbook.MyApplication;
import com.snir.shelfbook.model.AppLocalDb;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

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

    @SuppressLint("StaticFieldLeak")
    public void addBook(Book book, Listener<Boolean> listener){
        BookFirebase.addBook(book, listener);
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                AppLocalDb.db.bookDao().insertAll(book);
                return "";
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void deleteBook(Book book, Listener<Boolean> listener){
        BookFirebase.deleteImage(book.getId(), new Listener<Boolean>() {
            @Override
            public void onComplete(Boolean data) {

            }
        });
        BookFirebase.deleteBook(book.getId(),listener);
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                AppLocalDb.db.bookDao().delete(book);
                return "";
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void updateBook(Book book, Listener<Boolean> listener){
        BookFirebase.addBook(book, listener);
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                AppLocalDb.db.bookDao().insertAll(book);
                return "";
            }
        }.execute();
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
//        long lastUpdated = MyApplication.context.getSharedPreferences("TAG",MODE_PRIVATE).getLong("BooksLastUpdateDate",0);
//        BookFirebase.getAllBooksSince(lastUpdated,new Listener<List<Book>>() {
//            @SuppressLint("StaticFieldLeak")
//            @Override
//            public void onComplete(final List<Book> data) {
//                new AsyncTask<String, String, String>(){
//                    @Override
//                    protected String doInBackground(String... strings) {
//                        long lastUpdated = 0;
//                        for(Book b : data){
//                            AppLocalDb.db.bookDao().insertAll(b);
//                            if (b.getLastUpdated() > lastUpdated) b.setLastUpdated(lastUpdated);
//                        }
//                        SharedPreferences.Editor edit = MyApplication.context.getSharedPreferences("TAG", MODE_PRIVATE).edit();
//                        edit.putLong("BooksLastUpdateDate",lastUpdated);
//                        edit.apply();
//                        return "";
//                    }
//                    @Override
//                    protected void onPostExecute(String s) {
//                        super.onPostExecute(s);
//                        if (listener!=null)  listener.onComplete();
//                    }
//                }.execute("");
//            }
//        });
    }

    public void uploadImage(Bitmap imageBmp, String name, final BookModel.Listener<String> listener){
        BookFirebase.uploadImage(imageBmp,name,listener);
    }

    public void deleteImage(String name,final BookModel.Listener<Boolean> listener){
        BookFirebase.deleteImage(name,listener);
    }

}
