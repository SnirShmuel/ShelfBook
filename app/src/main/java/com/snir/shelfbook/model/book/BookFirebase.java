package com.snir.shelfbook.model.book;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BookFirebase {
    private static final String BOOK_COLLECTION = "books";


    public static void getAllBooksSince(long since, final BookModel.Listener<List<Book>> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Timestamp ts = new Timestamp(since,0);
        db.collection(BOOK_COLLECTION).whereGreaterThanOrEqualTo("lastUpdated", ts).whereEqualTo("isGiven",false)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Book> bookData = new LinkedList<Book>();
                if (task.isSuccessful()){
                    for(DocumentSnapshot doc : task.getResult()){
//                        Map<String, Object> json = doc.getData();
//                        Book book = factory(json);
                        Book book = doc.toObject(Book.class);
                        bookData.add(book);
                    }
                }
                listener.onComplete(bookData);
                if (bookData == null)
                    Log.d("TAG","refresh " + 0);
                else
                    Log.d("TAG","refresh " + bookData.size());
            }
        });
    }



    public static void addBook(Book book, BookModel.Listener<Boolean> listener) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection(BOOK_COLLECTION).document(book.getId()).set(toJson(book)).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (listener!=null){
//                    listener.onComplete(task.isSuccessful());
//                    Log.d("TAG", "Book added with ID: " + book.getId());
//                }
//            }
//        });
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(BOOK_COLLECTION).document(book.getId()).set(book.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TAG","Book added successfully!" + book.getId());
                listener.onComplete(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG","Fail adding book");
                listener.onComplete(false);
            }
        });
    }

    public static void deleteBook(String bookId, final BookModel.Listener<Boolean> listener){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(BOOK_COLLECTION).document(bookId).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.onComplete(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onComplete(false);
                    }
                });
    }





}
