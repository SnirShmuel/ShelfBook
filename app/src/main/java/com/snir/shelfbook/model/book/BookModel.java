package com.snir.shelfbook.model.book;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;

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
        book.setId("" + (liveData.getValue().size() + 1));

        BookFirebase.addBook(book, listener);
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                AppLocalDb.db.bookDao().insertAll(book);
                return "";
            }
        }.execute();

    }

    public void deleteBook(Book book, Listener<Boolean> listener){
        BookFirebase.deleteBook(book.getId(),listener);
        AppLocalDb.db.bookDao().delete(book);
    }

    public void updateBook(Book book, Listener<Boolean> listener){
        BookFirebase.addBook(book, listener);
        AppLocalDb.db.bookDao().insertAll(book);
    }

    public void refreshBookList(final CompListener listener){
        long lastUpdated = MyApplication.context.getSharedPreferences("TAG",MODE_PRIVATE).getLong("BooksLastUpdateDate",0);
        BookFirebase.getAllBooksSince(lastUpdated,new Listener<List<Book>>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onComplete(final List<Book> data) {
                new AsyncTask<String, String, String>(){
                    @Override
                    protected String doInBackground(String... strings) {
                        long lastUpdated = 0;
                        for(Book b : data){
                            AppLocalDb.db.bookDao().insertAll(b);
                            if (b.getLastUpdated() > lastUpdated) b.setLastUpdated(lastUpdated);
                        }
                        SharedPreferences.Editor edit = MyApplication.context.getSharedPreferences("TAG", MODE_PRIVATE).edit();
                        edit.putLong("BooksLastUpdateDate",lastUpdated);
                        edit.apply();
                        return "";
                    }
                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if (listener!=null)  listener.onComplete();
                    }
                }.execute("");
            }
        });
    }
}
