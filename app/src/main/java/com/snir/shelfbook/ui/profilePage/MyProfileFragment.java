package com.snir.shelfbook.ui.profilePage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.snir.shelfbook.R;
import com.snir.shelfbook.model.book.BookModel;
import com.snir.shelfbook.model.user.LoginUser;
import com.snir.shelfbook.model.user.User;
import com.snir.shelfbook.model.user.UserModel;



public class MyProfileFragment extends Fragment {
    User user;
    EditText username;
    EditText password;
    EditText name;
    EditText email;
    EditText city;
    EditText phone;
    Button editBtn;
    Button logoutBtn;
    ProgressDialog pd;
    public MyProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        user = LoginUser.getUser().userData;

        username = view.findViewById(R.id.profile_username_edit);
        username.setText(user.getUsername());

        password = view.findViewById(R.id.profile_password_edit);
        password.setText(user.getPassword());

        name = view.findViewById(R.id.profile_name_edit);
        name.setText(user.getName());
        email = view.findViewById(R.id.profile_email_edit);
        email.setText(user.getEmail());
        city = view.findViewById(R.id.profile_city_edit);
        city.setText(user.getCity());
        phone = view.findViewById(R.id.profile_phone_edit);
        phone.setText(user.getPhone());

        editBtn = view.findViewById(R.id.profile_edit_button);
        logoutBtn = view.findViewById(R.id.profile_logout_button);

        pd = new ProgressDialog(getContext());



        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("email",email.getText().toString());
                if (TextUtils.isEmpty(username.getText().toString()) || TextUtils.isEmpty(name.getText().toString()) ||
                        TextUtils.isEmpty(email.getText().toString()) || TextUtils.isEmpty(password.getText().toString()) ||
                        TextUtils.isEmpty(city.getText().toString() )|| TextUtils.isEmpty(phone.getText().toString())) {
                    Toast.makeText(getContext(), "Empty credentials!", Toast.LENGTH_SHORT).show();
                } else if (password.getText().length() < 6) {
                    Toast.makeText(getContext(), "Password have to be at least 6 characters", Toast.LENGTH_SHORT).show();

                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
                    Toast.makeText(getContext(), "Email address is invalid!", Toast.LENGTH_SHORT).show();
                } else if(phone.getText().length() != 10){
                    Toast.makeText(getContext(), "Phone number have to be 10 digits", Toast.LENGTH_SHORT).show();
                }else {
                    pd.setMessage("Update Details...");
                    pd.show();
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            updateProfile();
                            pd.dismiss();
                            Snackbar.make(v,user.getName() + " was updated!",Snackbar.LENGTH_LONG).show();
                        }
                    }, 1000);   //1 seconds
                }
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
        //Check edit text is not empty and update the profile values
        if (!username.getText().toString().equals(user.getUsername()))
            user.setUsername(username.getText().toString());
        if (!password.getText().toString().equals(user.getPassword()))
            user.setPassword(password.getText().toString());
        if (!name.getText().toString().equals(user.getName()))
            user.setName(name.getText().toString());
        if (!email.getText().toString().equals(user.getEmail()))
            user.setEmail(email.getText().toString());
        if (!city.getText().toString().equals(user.getCity()))
            user.setCity(city.getText().toString());
        if (!phone.getText().toString().equals(user.getUsername()))
            user.setPhone(phone.getText().toString());

        UserModel.instance.updateUserDetails(user, new UserModel.Listener<User>() {
            @Override
            public void onComplete(User data) {
                LoginUser.getUser().setUserData(data);
            }
        });
    }
}