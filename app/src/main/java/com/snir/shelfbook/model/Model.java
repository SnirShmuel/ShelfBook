package com.snir.shelfbook.model;

import com.snir.shelfbook.model.book.Book;

import java.util.LinkedList;
import java.util.List;

public class Model {
    public final static Model instance = new Model();

    private Model(){
        for(int i=0;i<10;i++) {
            Book book = new Book();
            book.setId("" + i);
            book.setName("Harry Potter" + i);
            book.setDescription("This is harry potter the " + i);
            data.add(book);
        }
    }

    List<Book> data = new LinkedList<Book>();

    public List<Book> getAllBooks() {
        return data;
    }
}
