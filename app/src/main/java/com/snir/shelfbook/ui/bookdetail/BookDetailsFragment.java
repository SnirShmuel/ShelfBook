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
import com.snir.shelfbook.model.book.BookFirebase;
import com.snir.shelfbook.model.book.BookModel;
import com.snir.shelfbook.model.user.User;
import com.snir.shelfbook.model.user.UserModel;
import com.squareup.picasso.Picasso;

public class BookDetailsFragment extends Fragment {
    Book book;
    TextView booknametv;
    TextView bookgenretv;
    TextView bookconditiontv;
    TextView bookdesctv;
    TextView ownerName;
    TextView ownerEmail;
    TextView ownerPhone;
    TextView ownerCity;
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
        ownerName = view.findViewById(R.id.bookDetails_owner_name);
        ownerEmail = view.findViewById(R.id.bookDetails_owner_email);
        ownerPhone = view.findViewById(R.id.bookDetails_owner_phone);
        ownerCity = view.findViewById(R.id.bookDetails_owner_city);

        editBtn = view.findViewById(R.id.bookDetailes_editBtn);
        deleteBtn = view.findViewById(R.id.bookDetailes_deleteBook);
        book = BookDetailsFragmentArgs.fromBundle(getArguments()).getBook();
        addBookBtn = getActivity().findViewById(R.id.fab);





        addBookBtn.setVisibility(View.INVISIBLE);
        booknametv.setText(book.getName());
        bookgenretv.setText(book.getGenre());
        bookconditiontv.setText(book.getBookCondition());
        bookdesctv.setText(book.getDescription());

        BookFirebase.getOwnerData(book.getOwnerId(), new UserModel.Listener<User>() {
            @Override
            public void onComplete(User data) {
                ownerName.setText(data.getName());
                ownerCity.setText(data.getCity());
                ownerEmail.setText(data.getEmail());
                ownerPhone.setText(data.getPhone());
            }
        });

        Picasso.get()
                .load(book.getImageUrl())
                .placeholder(R.drawable.jabbascript)
                .error(R.drawable.jabbascript)
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
                Snackbar.make(v,book.getName() + " was deleted!",Snackbar.LENGTH_LONG).show();
                Navigation.findNavController(v).popBackStack();
            }

        });
    }
}