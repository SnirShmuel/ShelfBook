package com.snir.shelfbook.ui.bookedit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.snir.shelfbook.R;
import com.snir.shelfbook.model.book.Book;
import com.snir.shelfbook.model.book.BookModel;

public class bookEditFragment extends Fragment {
    Book book;
    EditText bookNameEv;
    EditText bookGenreEv;
    EditText bookConditionEv;
    EditText bookDescriptionEv;
    Button saveBtn;
    Button deleteBtn;
    FloatingActionButton addBookBtn;

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
        addBookBtn = getActivity().findViewById(R.id.fab);

        addBookBtn.setVisibility(View.INVISIBLE);
        bookNameEv.setText(book.getName());
        bookGenreEv.setText(book.getGenre());
        bookConditionEv.setText(book.getBookCondition());
        bookDescriptionEv.setText(book.getDescription());

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBook();
                Snackbar.make(v,book.getName() + " was updated!",Snackbar.LENGTH_LONG).show();
                Navigation.findNavController(v).popBackStack();
//                Navigation.findNavController(v).popBackStack();
            }
        });

        return view;
    }

    private void updateBook() {

        //Check edit text is not empty and update the book values
        if (!(bookNameEv.getText().toString().isEmpty()) && (!bookNameEv.getText().toString().equals(book.getName())))
            book.setName(bookNameEv.getText().toString());
        if (!(bookGenreEv.getText().toString().isEmpty()) && (!bookGenreEv.getText().toString().equals(book.getGenre())))
            book.setGenre(bookGenreEv.getText().toString());
        if (!(bookConditionEv.getText().toString().isEmpty()) && (!bookConditionEv.getText().toString().equals(book.getBookCondition())))
            book.setBookCondition(bookConditionEv.getText().toString());
        book.setDescription(bookDescriptionEv.getText().toString());

        BookModel.instance.updateBook(book, new BookModel.Listener<Boolean>() {
            @Override
            public void onComplete(Boolean data) {
            }
        });
    }


}