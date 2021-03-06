package com.snir.shelfbook.ui.bookdetail;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.snir.shelfbook.R;
import com.snir.shelfbook.model.book.Book;

public class BookDetailsFragment extends Fragment {
    Book book;
    TextView booknametv;
    TextView bookgenretv;
    TextView bookconditiontv;
    TextView bookdesctv;
    Button editBtn;
    FloatingActionButton addBookBtn;

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
        editBtn = view.findViewById(R.id.bookDetailes_editBtn);
        book = BookDetailsFragmentArgs.fromBundle(getArguments()).getBook();
        addBookBtn = getActivity().findViewById(R.id.fab);





        addBookBtn.setVisibility(View.INVISIBLE);
        booknametv.setText(book.getName());
        bookgenretv.setText(book.getGenre());
        bookconditiontv.setText(book.getBookCondition());
        bookdesctv.setText(book.getDescription());


        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookDetailsFragmentDirections.ActionBookDetailsFragmentToBookEditFragment actionToEdit = BookDetailsFragmentDirections.actionBookDetailsFragmentToBookEditFragment(book);
                Navigation.findNavController(v).navigate(actionToEdit);
            }
        });

        return view;
    }
}