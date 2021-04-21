package com.snir.shelfbook.ui.Auth;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.snir.shelfbook.R;
import com.snir.shelfbook.model.user.LoginUser;
import com.snir.shelfbook.model.user.User;
import com.snir.shelfbook.model.user.UserModel;

public class AuthFragment extends Fragment {

    private LinearLayout linearLayout;
    private Button register;
    private Button login;

    public AuthFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            UserModel.instance.getUser(new UserModel.Listener<User>() {
                @Override
                public void onComplete(User data) {
                    LoginUser.getUser().setUserData(data);
                    //nav to home page of application
                    Navigation.findNavController(getView()).navigate(R.id.action_authFragment_to_nav_books_list);
                }
            });

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_auth, container, false);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            view.findViewById(R.id.auth_layout).setVisibility(View.INVISIBLE);
        } else {
            linearLayout = view.findViewById(R.id.linear_layout);
            register = view.findViewById(R.id.registerBtn);
            login = view.findViewById(R.id.login_btn);

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // nav to register page
                    Navigation.findNavController(v).navigate(R.id.action_authFragment_to_registerFragment);
                }
            });

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(v).navigate(R.id.action_authFragment_to_loginFragment);
                }
            });

        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

//        if (FirebaseAuth.getInstance().getCurrentUser() != null){
//
//            UserModel.instance.getUser(new UserModel.Listener<User>() {
//                @Override
//                public void onComplete(User data) {
//                    LoginUser.getUser().setUserData(data);
//                    //nav to home page of application
//                    Navigation.findNavController(getView()).navigate(R.id.action_authFragment_to_myProfileFragment);
//                }
//            });
//
//        }
    }
}