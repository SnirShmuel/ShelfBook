package com.snir.shelfbook.model.book;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.snir.shelfbook.model.AppLocalDb;

import java.util.List;

public class BookSql {

    public LiveData<List<Book>> getAllBooks(){
        return AppLocalDb.db.bookDao().getAllBooks();
    }

    public static void addBook(final Book book, final BookModel.CompListener listener){
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDb.db.bookDao().insertAll(book);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener != null){
                    listener.onComplete();
                }
            }
        };
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }

    public static void deleteBook(final Book book, final BookModel.CompListener listener){
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDb.db.bookDao().delete(book);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener != null){
                    listener.onComplete();
                }
            }
        };
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }
}
