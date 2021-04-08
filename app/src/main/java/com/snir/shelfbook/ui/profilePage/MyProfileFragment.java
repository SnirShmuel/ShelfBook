package com.snir.shelfbook.ui.profilePage;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.snir.shelfbook.R;
import com.snir.shelfbook.model.book.BookModel;
import com.snir.shelfbook.model.user.User;
import com.snir.shelfbook.model.user.UserModel;

public class MyProfileFragment extends Fragment {
    User user;
    EditText username;
    EditText name;
    EditText email;
    EditText city;
    EditText phone;
    Button editBtn;
    Button logoutBtn;

    public MyProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        username = view.findViewById(R.id.profile_username_edit);
        name = view.findViewById(R.id.profile_name_edit);
        email = view.findViewById(R.id.profile_email_edit);
        city = view.findViewById(R.id.profile_email_edit);
        phone = view.findViewById(R.id.profile_city_edit);
        editBtn = view.findViewById(R.id.profile_edit_button);
        logoutBtn = view.findViewById(R.id.profile_logout_button);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
                Snackbar.make(v,user.getName() + " was updated!",Snackbar.LENGTH_LONG).show();
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = getActivity().getIntent();
                getActivity().finish();
                startActivity(intent);
            }
        });

        return view;
    }

    public void updateProfile(){
        //Check edit text is not empty and update the book values
//        if (!(bookNameEv.getText().toString().isEmpty()) && (!bookNameEv.getText().toString().equals(book.getName())))
//            book.setName(bookNameEv.getText().toString());
        if (!(username.getText().toString().isEmpty()) && (!username.getText().toString().equals(user.getUsername())))
            user.setUsername(username.getText().toString());
        if (!(name.getText().toString().isEmpty()) && (!name.getText().toString().equals(user.getName())))
            user.setName(name.getText().toString());
        if (!(email.getText().toString().isEmpty()) && (!email.getText().toString().equals(user.getEmail())))
            user.setEmail(email.getText().toString());
        if (!(city.getText().toString().isEmpty()) && (!city.getText().toString().equals(user.getCity())))
            user.setCity(city.getText().toString());
        if (!(phone.getText().toString().isEmpty()) && (!phone.getText().toString().equals(user.getUsername())))
            user.setPhone(phone.getText().toString());

        UserModel.instance.updateUserDetails(user, new UserModel.Listener<User>() {
            @Override
            public void onComplete(User data) {

            }
        });
    }
}