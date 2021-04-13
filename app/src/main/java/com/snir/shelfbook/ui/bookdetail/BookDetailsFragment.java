package com.snir.shelfbook.ui.bookdetail;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.snir.shelfbook.R;
import com.snir.shelfbook.model.book.Book;
import com.snir.shelfbook.model.book.BookFirebase;
import com.snir.shelfbook.model.book.BookModel;
import com.snir.shelfbook.model.user.LoginUser;
import com.snir.shelfbook.model.user.User;
import com.snir.shelfbook.model.user.UserModel;
import com.squareup.picasso.Picasso;

public class BookDetailsFragment extends Fragment {
    Book book;
    TextView booknametv;
    TextView bookgenretv;
    TextView bookconditiontv;
    TextView bookdesctv;
    TextView bookAuthorTv;
    TextView ownerName;
    TextView ownerEmail;
    TextView ownerPhone;
    TextView ownerCity;
    ImageView bookImgv;
    Button editBtn;
    Button deleteBtn;
    FloatingActionButton addBookBtn;
    ProgressDialog pd;

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
        bookAuthorTv = view.findViewById(R.id.bookDetailes_authorTv);
        bookImgv = view.findViewById(R.id.bookDetailes_imgIv);
        ownerName = view.findViewById(R.id.bookDetails_owner_name);
        ownerEmail = view.findViewById(R.id.bookDetails_owner_email);
        ownerPhone = view.findViewById(R.id.bookDetails_owner_phone);
        ownerCity = view.findViewById(R.id.bookDetails_owner_city);

        editBtn = view.findViewById(R.id.bookDetailes_editBtn);
        deleteBtn = view.findViewById(R.id.bookDetailes_deleteBook);
        book = BookDetailsFragmentArgs.fromBundle(getArguments()).getBook();
        addBookBtn = getActivity().findViewById(R.id.fab);

        pd = new ProgressDialog(getContext());

        addBookBtn.setVisibility(View.INVISIBLE);
        if (book.getName() == null || book.getName().isEmpty())
            booknametv.setText("N/A");
        else
            booknametv.setText(book.getName());
        if (book.getGenre() == null || book.getGenre().isEmpty())
            bookgenretv.setText("N/A");
        else
            bookgenretv.setText(book.getGenre());
        if (book.getBookCondition() == null || book.getBookCondition().isEmpty())
            bookconditiontv.setText("N/A");
        else
            bookconditiontv.setText(book.getBookCondition());
        if (book.getDescription() == null || book.getDescription().isEmpty())
            bookdesctv.setText("N/A");
        else
            bookdesctv.setText(book.getDescription());
        if (book.getAuthor() == null || book.getAuthor().isEmpty())
            bookAuthorTv.setText("N/A");
        else
            bookAuthorTv.setText(book.getAuthor());


        BookFirebase.getOwnerData(book.getOwnerId(), new UserModel.Listener<User>() {
            @Override
            public void onComplete(User data) {
                if (data.getName() == null || data.getName().isEmpty())
                    ownerName.setText("N/A");
                else
                    ownerName.setText(data.getName());
                if (data.getCity() == null || data.getCity().isEmpty())
                    ownerCity.setText("N/A");
                else
                    ownerCity.setText(data.getCity());
                if (data.getEmail() == null || data.getEmail().isEmpty())
                    ownerEmail.setText("N/A");
                else
                    ownerEmail.setText(data.getEmail());
                if (data.getPhone() == null || data.getPhone().isEmpty())
                    ownerPhone.setText("N/A");
                else
                    ownerPhone.setText(data.getPhone());
            }
        });

        Picasso.get()
                .load(book.getImageUrl())
                .placeholder(R.drawable.jabbascript)
                .error(R.drawable.jabbascript)
                .into(bookImgv);


        if (LoginUser.getUser().userData.getId() != book.getOwnerId()) {
            editBtn.setVisibility(View.INVISIBLE);
            deleteBtn.setVisibility(View.INVISIBLE);
        } else {
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
                    pd.setMessage("Delete book...");
                    pd.show();
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            deleteBook(book, v);
                        }
                    }, 1000);   //1 seconds

                }
            });
        }


        return view;
    }

    private void deleteBook(Book book, View v) {

        BookModel.instance.deleteBook(book, new BookModel.Listener<Boolean>() {
            @Override
            public void onComplete(Boolean data) {
                pd.dismiss();
                Snackbar.make(v, book.getName() + " was deleted!", Snackbar.LENGTH_LONG).show();
                Navigation.findNavController(v).popBackStack();
            }

        });
    }
}