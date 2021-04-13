package com.snir.shelfbook.ui.bookadd;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.snir.shelfbook.R;
import com.snir.shelfbook.model.book.Book;
import com.snir.shelfbook.model.book.BookModel;
import com.snir.shelfbook.model.user.LoginUser;

import java.util.UUID;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class BookAddFragment extends Fragment {
    FloatingActionButton addBookBtn;
    FloatingActionButton addImgBtn;
    EditText bookNameEt;
    EditText bookAuthorEt;
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
        bookAuthorEt = view.findViewById(R.id.bookAdd_bookAuthorEt);
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
                addImage();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bookNameEt.getText().toString().isEmpty() || bookNameEt.getText().toString() == null)
                    bookNameEt.setError("Book name cannot be empty!");
                else if (bookConditionEt.getText().toString().isEmpty() || bookConditionEt.getText().toString() == null)
                    bookConditionEt.setError("Book condition cannot be empty!");
                else
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        bookImg.setImageBitmap(selectedImage);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage =  data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                bookImg.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }

    private void addImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose your profile picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void saveBook(){
        final Book book = new Book();
        book.setId(UUID.randomUUID().toString());
        book.setName(bookNameEt.getText().toString());
        book.setGenre(bookGenreEt.getText().toString());
        book.setBookCondition(bookConditionEt.getText().toString());
        book.setDescription(bookDescEt.getText().toString());
        book.setGiven(false);
        book.setOwnerId(LoginUser.getUser().userData.getId());

        BitmapDrawable drawable = (BitmapDrawable)bookImg.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        BookModel.instance.uploadImage(bitmap, book.getId(), new BookModel.Listener<String>() {
            @Override
            public void onComplete(String url) {
                if (url == null)
                    displayFailedError();
                else {
                    book.setImageUrl(url);
                    BookModel.instance.addBook(book, new BookModel.Listener<Boolean>() {
                        @Override
                        public void onComplete(Boolean data) {
                            Navigation.findNavController(saveBtn).popBackStack();
                        }
                    });
                }
            }
        });
    }

    private void displayFailedError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Operation Failed");
        builder.setMessage("Saving image failed, please try again later...");
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
}