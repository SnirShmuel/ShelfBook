package com.snir.shelfbook.ui.booklist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.snir.shelfbook.model.book.Book;
import com.snir.shelfbook.model.book.BookModel;

import java.util.LinkedList;
import java.util.List;

public class BookListViewModel extends ViewModel {
    private LiveData<List<Book>> liveData;

    public BookListViewModel(){

    }

    public LiveData<List<Book>> getData(){
        if (liveData == null){
            liveData = BookModel.instance.getAllBooks();
        }
        return liveData;
    }





    public void refresh(BookModel.CompListener listener){
        BookModel.instance.refreshBookList(listener);
    }
}
