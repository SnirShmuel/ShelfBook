package com.snir.shelfbook.ui.bookedit;

import android.app.ProgressDialog;
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

import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.snir.shelfbook.R;
import com.snir.shelfbook.model.book.Book;
import com.snir.shelfbook.model.book.BookModel;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class bookEditFragment extends Fragment {
    View view;
    Book book;
    EditText bookNameEv;
    EditText bookGenreEv;
    EditText bookConditionEv;
    EditText bookDescriptionEv;
    ImageButton bookImgBtn;
    Button saveBtn;
    Button deleteBtn;
    FloatingActionButton addBookBtn;
    ProgressDialog pd;

    public bookEditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_book_edit, container, false);

        book= bookEditFragmentArgs.fromBundle(getArguments()).getBook();

        bookNameEv = view.findViewById(R.id.bookEdit_nameEv);
        bookGenreEv = view.findViewById(R.id.bookEdit_GenreEv);
        bookConditionEv = view.findViewById(R.id.bookEdit_conditionEv);
        bookDescriptionEv = view.findViewById(R.id.bookEdit_descEv);
        bookImgBtn = view.findViewById(R.id.bookEdit_imgBtn);
        saveBtn = view.findViewById(R.id.bookEdit_saveEditBtn);
        deleteBtn = view.findViewById(R.id.bookEdit_deleteBtn);
        addBookBtn = getActivity().findViewById(R.id.fab);

        addBookBtn.setVisibility(View.INVISIBLE);
        bookNameEv.setText(book.getName());
        bookGenreEv.setText(book.getGenre());
        bookConditionEv.setText(book.getBookCondition());
        bookDescriptionEv.setText(book.getDescription());
        Picasso.get()
                .load(book.getImageUrl())
                .placeholder(R.drawable.jabbascript)
                .error(R.drawable.jabbascript)
                .into(bookImgBtn);

        pd = new ProgressDialog(getContext());

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setMessage("Please wait!");
                pd.show();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        updateBook();
                        pd.dismiss();
                        Snackbar.make(v,book.getName() + " was updated!",Snackbar.LENGTH_LONG).show();
                        Navigation.findNavController(v).popBackStack();
                    }
                }, 2000);   //2 seconds
            }
        });

        bookImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editImage();
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
                        bookImgBtn.setImageBitmap(selectedImage);
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
                                bookImgBtn.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
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

        BitmapDrawable drawable = (BitmapDrawable)bookImgBtn.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

//        //Delete old photo from server
//        BookModel.instance.deleteImage(book.getId(), new BookModel.Listener<Boolean>() {
//            @Override
//            public void onComplete(Boolean data) {
//
//            }
//        });

        //Upload new photo to server
        BookModel.instance.uploadImage(bitmap, book.getId(), new BookModel.Listener<String>() {
            @Override
            public void onComplete(String url) {
                if (url == null)
                    displayFailedError();
                else{
                    book.setImageUrl(url);
                    BookModel.instance.updateBook(book, new BookModel.Listener<Boolean>() {
                        @Override
                        public void onComplete(Boolean data) {
                        }
                    });
                }
            }
        });
    }

    private void editImage() {
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