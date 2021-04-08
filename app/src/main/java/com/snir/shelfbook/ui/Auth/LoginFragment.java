package com.snir.shelfbook.ui.Auth;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.snir.shelfbook.R;
import com.snir.shelfbook.model.user.Global_user;
import com.snir.shelfbook.model.user.User;
import com.snir.shelfbook.ui.bookdetail.BookDetailsFragmentDirections;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LoginFragment extends Fragment {

    private EditText email;
    private EditText password;
    private Button login;
    private TextView registerUser;
    ProgressDialog pd;

    private FirebaseAuth mAuth;

    private User userDetails = new User();

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        login = view.findViewById(R.id.login);
        registerUser = view.findViewById(R.id.register_user);
        mAuth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(getContext());

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO : nav to register fragment
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment);
//                BookDetailsFragmentDirections.ActionBookDetailsFragmentToBookEditFragment actionToEdit = BookDetailsFragmentDirections.actionBookDetailsFragmentToBookEditFragment(book);
//                Navigation.findNavController(v).navigate(actionToEdit);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(getContext(), "Empty Credentials!", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(txt_email, txt_password);
                }
            }
        });

        return view;
    }

    private void loginUser(String email, String password) {
        pd.setMessage("Please wait!");
        pd.show();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    pd.dismiss();
                    //nav to home page of application
                    Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_nav_home);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}