package com.snir.shelfbook.ui.bookdetail;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.snir.shelfbook.R;
import com.snir.shelfbook.model.book.Book;
import com.snir.shelfbook.model.book.BookModel;
import com.squareup.picasso.Picasso;

public class BookDetailsFragment extends Fragment {
    Book book;
    TextView booknametv;
    TextView bookgenretv;
    TextView bookconditiontv;
    TextView bookdesctv;
    ImageView bookImgv;
    Button editBtn;
    Button deleteBtn;
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
        bookImgv = view.findViewById(R.id.bookDetailes_imgIv);
        editBtn = view.findViewById(R.id.bookDetailes_editBtn);
        deleteBtn = view.findViewById(R.id.bookDetailes_deleteBook);
        book = BookDetailsFragmentArgs.fromBundle(getArguments()).getBook();
        addBookBtn = getActivity().findViewById(R.id.fab);





        addBookBtn.setVisibility(View.INVISIBLE);
        booknametv.setText(book.getName());
        bookgenretv.setText(book.getGenre());
        bookconditiontv.setText(book.getBookCondition());
        bookdesctv.setText(book.getDescription());
        Picasso.get()
                .load(book.getImageUrl())
                .placeholder(R.drawable.harry_potter_and_the_philosophers_stone)
                .error(R.drawable.harry_potter_and_the_philosophers_stone)
                .into(bookImgv);



        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookDetailsFragmentDirections.ActionBookDetailsFragmentToBookEditFragment actionToEdit = BookDetailsFragmentDirections.actionBookDetailsFragmentToBookEditFragment(book);
                Navigation.findNavController(v).navigate(actionToEdit);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBook(book,v);
            }
        });

        return view;
    }

    private void deleteBook(Book book, View v) {

        BookModel.instance.deleteBook(book, new BookModel.Listener<Boolean>() {
            @Override
            public void onComplete(Boolean data) {
                Snackbar.make(v,book.getName() + "was deleted!",Snackbar.LENGTH_LONG).show();
                Navigation.findNavController(v).popBackStack();
            }

        });
    }
}