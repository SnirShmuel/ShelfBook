package com.snir.shelfbook.ui.Auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.snir.shelfbook.R;

public class AuthFragment extends Fragment {

    private LinearLayout linearLayout;
    private Button register;
    private Button login;

    public AuthFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_auth, container, false);

        linearLayout = view.findViewById(R.id.linear_layout);
        register = view.findViewById(R.id.register);
        login = view.findViewById(R.id.login);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: nav to register page
                Log.d("shay", String.valueOf(Navigation.findNavController(v)));
//                Navigation.findNavController(v).navigate(R.id.action_authFragment_to_registerFragment);
                //startActivity(new Intent(StartActivity.this , RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: nav to login page
                Navigation.findNavController(v).navigate(R.id.action_authFragment_to_loginFragment);
                //startActivity(new Intent(StartActivity.this , LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            //TODO: nav to home page
            Navigation.findNavController(getView()).navigate(R.id.action_authFragment_to_nav_home);
//            startActivity(new Intent(StartActivity.this , HomeActivity.class));
//            finish();
        }
    }
}