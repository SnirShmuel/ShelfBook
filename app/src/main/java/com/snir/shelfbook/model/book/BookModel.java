package com.snir.shelfbook.model.book;


import android.graphics.Bitmap;


import androidx.lifecycle.LiveData;


import com.snir.shelfbook.model.user.LoginUser;

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

        liveData = BookSql.getAllBooks();
        refreshBookList(null);
        return liveData;
    }

    public LiveData<List<Book>> getAllBooksByUser(String UserId){
        liveData = BookSql.getBookByUser(UserId);
        refreshMyBookList(UserId,null);
        return liveData;
    }

    public void addBook(Book book, Listener<Boolean> listener){
        BookFirebase.addBook(book, listener);
        BookSql.addBook(book, null);
    }

    public void deleteBook(Book book, Listener<Boolean> listener){
        book.setDeleted(true);
        deleteImage(book.getId(), new Listener<Boolean>() {
            @Override
            public void onComplete(Boolean data) {

            }
        });
        BookFirebase.addBook(book,listener);
        BookSql.deleteBook(book,null);
    }

    public void updateBook(Book book, Listener<Boolean> listener){
        BookFirebase.addBook(book, listener);
        BookSql.addBook(book,null);
    }

    public void refreshBookList(final CompListener listener){
        //1. Get all new ungiven and undeleted books
        BookFirebase.getAllBooks(new Listener<List<Book>>() {
            @Override
            public void onComplete(List<Book> data) {
                for (Book book:data){
                    BookSql.addBook(book,null);
                }
                if (listener != null)
                    listener.onComplete();
            }
        });
        //2. Delete 'deleted books' from localDB
        BookFirebase.getDeletedBooks(new Listener<List<Book>>() {
            @Override
            public void onComplete(List<Book> data) {
                if (!data.isEmpty()){
                    for (Book book:data){
                        BookSql.deleteBook(book,null);
                    }
                }
                if (listener != null)
                    listener.onComplete();
            }
        });
    }

    public void refreshMyBookList(String userId,final CompListener listener){
        BookFirebase.getBooksByOwnerId(userId, new Listener<List<Book>>() {
            @Override
            public void onComplete(List<Book> data) {
                for (Book book: data){
                    BookSql.addBook(book,null);
                }
                if (listener != null)
                    listener.onComplete();
            }
        });
    }

    public void uploadImage(Bitmap imageBmp, String name, final BookModel.Listener<String> listener){
        BookFirebase.uploadImage(imageBmp,name,listener);
    }

    public void deleteImage(String name,final BookModel.Listener<Boolean> listener){
        BookFirebase.deleteImage(name,listener);
    }
    public void getUserBooks(Listener<List<Book>> listener){
        BookFirebase.getBooksByOwnerId(LoginUser.getUser().userData.getId(), new Listener<List<Book>>(){

            @Override
            public void onComplete(List<Book> data) {
                listener.onComplete(data);
            }
        });
    }
}
