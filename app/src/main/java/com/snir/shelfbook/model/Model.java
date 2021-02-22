package com.snir.shelfbook.model;

import com.snir.shelfbook.model.book.Book;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Model {
    public final static Model instance = new Model();

    private Model(){
        List<String> condition = Arrays.asList("new","used","like new", "old");

        for(int i=0;i<10;i++) {
            Book book = new Book();
            book.setId("" + i);
            book.setName("Harry Potter" + i);
            book.setGenre("Genre " + i);
            //book.setBookCondition("new");
            book.setBookCondition(condition.get((int) (Math.random() * condition.size())));
            book.setDescription(i + " Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut porttitor leo a diam sollicitudin.");
            data.add(book);
        }
    }

    List<Book> data = new LinkedList<Book>();

    public List<Book> getAllBooks() {
        return data;
    }
}
