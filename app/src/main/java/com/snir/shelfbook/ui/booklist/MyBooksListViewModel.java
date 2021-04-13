package com.snir.shelfbook.ui.booklist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.snir.shelfbook.model.book.Book;
import com.snir.shelfbook.model.book.BookModel;
import com.snir.shelfbook.model.user.LoginUser;

import java.util.List;

public class MyBooksListViewModel extends ViewModel {
    private LiveData<List<Book>> liveData;

    public MyBooksListViewModel(){

    }

    public LiveData<List<Book>> getData(){
        if (liveData == null){
            liveData = BookModel.instance.getAllBooksOfUser(LoginUser.getUser().userData.getId());
        }
        return liveData;
    }

    public void refresh(BookModel.CompListener listener){
        BookModel.instance.refreshBookList(listener);
    }
}