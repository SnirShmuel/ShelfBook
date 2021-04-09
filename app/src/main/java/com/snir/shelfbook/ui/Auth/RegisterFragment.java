package com.snir.shelfbook.ui.Auth;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.snir.shelfbook.R;
import com.snir.shelfbook.model.user.LoginUser;
import com.snir.shelfbook.model.user.User;
import com.snir.shelfbook.model.user.UserModel;

import java.util.HashMap;

public class RegisterFragment extends Fragment {

    private EditText username;
    private EditText name;
    private EditText email;
    private EditText password;
    private EditText city;
    private EditText phone;
    private Button registerBtn;
    private TextView loginUserLink;

//    private DatabaseReference mRootRef;

    private FirebaseAuth mAuth;

    ProgressDialog pd;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        username = view.findViewById(R.id.register_username);
        name = view.findViewById(R.id.register_name);
        email = view.findViewById(R.id.register_email);
        password = view.findViewById(R.id.register_password);
        city = view.findViewById(R.id.register_city);
        phone = view.findViewById(R.id.register_phone);
        registerBtn = view.findViewById(R.id.registerBtn);
        loginUserLink = view.findViewById(R.id.login_user_link);
        mAuth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(getContext());

        loginUserLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //nav to login page
                Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_loginFragment);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtUsername = username.getText().toString();
                String txtName = name.getText().toString();
                String txtEmail = email.getText().toString();
                String txtPassword = password.getText().toString();
                String txtCity = city.getText().toString();
                String txtPhone = phone.getText().toString();

                if (TextUtils.isEmpty(txtUsername) || TextUtils.isEmpty(txtName) ||
                        TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword) ||
                        TextUtils.isEmpty(txtCity) || TextUtils.isEmpty(txtPhone)) {
                    Toast.makeText(getContext(), "Empty credentials!", Toast.LENGTH_SHORT).show();
                } else if (txtPassword.length() < 6) {
                    Toast.makeText(getContext(), "Password too short!", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(txtUsername, txtName, txtEmail, txtPassword,txtCity,txtPhone);
                }
            }
        });

        return view;
    }

    private void registerUser(final String username, final String name, final String email, String password,String city,String phone) {

        pd.setMessage("Please wait!");
        pd.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                HashMap<String, Object> map = new HashMap<>();
                map.put("id", mAuth.getCurrentUser().getUid());
                map.put("username", username);
                map.put("name", name);
                map.put("email", email);
                map.put("city", city);
                map.put("phone", phone);

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("Users").document(mAuth.getCurrentUser().getUid()).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            pd.dismiss();
                            User user = new User(mAuth.getCurrentUser().getUid(),username,password,name,email,city,phone, System.currentTimeMillis());
                            LoginUser.getUser().setUserData(user);
                            Navigation.findNavController(getView()).navigate(R.id.action_registerFragment_to_myProfileFragment);
                        }
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}