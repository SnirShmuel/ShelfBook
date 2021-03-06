package com.snir.shelfbook.ui.booklist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.snir.shelfbook.model.book.Book;
import com.snir.shelfbook.model.book.BookModel;

import java.util.List;

public class BookListViewModel extends ViewModel {
    LiveData<List<Book>> liveData;

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
