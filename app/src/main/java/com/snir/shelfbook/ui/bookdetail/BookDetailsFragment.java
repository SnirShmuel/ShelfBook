package com.snir.shelfbook.ui.bookdetail;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.snir.shelfbook.R;
import com.snir.shelfbook.model.book.Book;

public class BookDetailsFragment extends Fragment {
    Book book;
    TextView booknametv;
    TextView bookgenretv;
    TextView bookconditiontv;
    TextView bookdesctv;
    TextView bookidtv;

    public BookDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_details, container, false);
        booknametv = view.findViewById(R.id.bookDetailes_nameTv);
        bookgenretv = view.findViewById(R.id.bookDetailes_genreTv);
        bookconditiontv = view.findViewById(R.id.bookDetailes_conditionTv);
        bookdesctv = view.findViewById(R.id.bookDetailes_descriptionTv);
        bookidtv = view.findViewById(R.id.bookDetailes_idTv);
        book = BookDetailsFragmentArgs.fromBundle(getArguments()).getBook();

        booknametv.setText(book.getName());
        bookgenretv.setText(book.getGenre());
        bookconditiontv.setText(book.getBookCondition());
        bookdesctv.setText(book.getDescription());
        bookidtv.setText(book.getId());


        return view;
    }
}