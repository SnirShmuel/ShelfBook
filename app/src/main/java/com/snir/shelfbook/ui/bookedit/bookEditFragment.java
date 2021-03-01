package com.snir.shelfbook.ui.bookedit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.snir.shelfbook.R;
import com.snir.shelfbook.model.book.Book;

public class bookEditFragment extends Fragment {
    Book book;
    EditText bookNameEv;
    EditText bookGenreEv;
    EditText bookConditionEv;
    EditText bookDescriptionEv;
    Button saveBtn;
    Button deleteBtn;

    public bookEditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_edit, container, false);

        book= bookEditFragmentArgs.fromBundle(getArguments()).getBook();

        bookNameEv = view.findViewById(R.id.bookEdit_nameEv);
        bookGenreEv = view.findViewById(R.id.bookEdit_GenreEv);
        bookConditionEv = view.findViewById(R.id.bookEdit_conditionEv);
        bookDescriptionEv = view.findViewById(R.id.bookEdit_descEv);
        saveBtn = view.findViewById(R.id.bookEdit_saveEditBtn);
        deleteBtn = view.findViewById(R.id.bookEdit_deleteBtn);

        bookNameEv.setText(book.getName());
        bookGenreEv.setText(book.getGenre());
        bookConditionEv.setText(book.getBookCondition());
        bookDescriptionEv.setText(book.getDescription());

        return view;
    }
}