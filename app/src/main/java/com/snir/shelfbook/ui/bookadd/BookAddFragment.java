package com.snir.shelfbook.ui.bookadd;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.snir.shelfbook.R;
import com.snir.shelfbook.model.book.Book;
import com.snir.shelfbook.model.book.BookModel;

import java.util.UUID;

public class BookAddFragment extends Fragment {
    FloatingActionButton addBookBtn;
    FloatingActionButton addImgBtn;
    EditText bookNameEt;
    EditText bookGenreEt;
    EditText bookConditionEt;
    EditText bookDescEt;
    ImageView bookImg;
    Button saveBtn;
    Button cancelBtn;


    public BookAddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_add, container, false);
        
        addBookBtn = getActivity().findViewById(R.id.fab);
        addImgBtn = view.findViewById(R.id.bookAdd_floatinAddPhoto);
        bookNameEt = view.findViewById(R.id.bookAdd_bookNameEt);
        bookGenreEt = view.findViewById(R.id.bookAdd_GenreEt);
        bookConditionEt = view.findViewById(R.id.bookAdd_CondEt);
        bookDescEt = view.findViewById(R.id.bookAdd_descEt);
        bookImg = view.findViewById(R.id.bookAdd_imgView);
        saveBtn = view.findViewById(R.id.bookAdd_saveBtn);
        cancelBtn = view.findViewById(R.id.bookAdd_cancelBtn);

        addBookBtn.setVisibility(View.INVISIBLE);

        addImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TO-DO : add image
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBook();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();
            }
        });

        return view;
    }

    private void saveBook(){
        final Book book = new Book();
        book.setId(UUID.randomUUID().toString());
        book.setName(bookNameEt.getText().toString());
        book.setGenre(bookGenreEt.getText().toString());
        book.setBookCondition(bookConditionEt.getText().toString());
        book.setDescription(bookDescEt.getText().toString());
        book.setGiven(false);


        BookModel.instance.addBook(book, new BookModel.Listener<Boolean>() {
            @Override
            public void onComplete(Boolean data) {
                Navigation.findNavController(saveBtn).popBackStack();
            }
        });
    }
}