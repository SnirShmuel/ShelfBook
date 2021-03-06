package com.snir.shelfbook;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class BookAddFragment extends Fragment {
    FloatingActionButton addBookBtn;


    public BookAddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_add, container, false);
        
        addBookBtn = getActivity().findViewById(R.id.fab);


        addBookBtn.setVisibility(View.INVISIBLE);

        return view;
    }
}