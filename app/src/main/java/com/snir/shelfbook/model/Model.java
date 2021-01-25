package com.snir.shelfbook.model;

import java.util.LinkedList;
import java.util.List;

public class Model {
    public final static Model instance = new Model();

    private Model(){
        for(int i=0;i<100;i++) {
            Book book = new Book();
            book.setId("" + i);
            book.setName("Harry Potter" + i);
            data.add(book);
        }
    }

    List<Book> data = new LinkedList<Book>();

    public List<Book> getAllBooks() {
        return data;
    }
}
