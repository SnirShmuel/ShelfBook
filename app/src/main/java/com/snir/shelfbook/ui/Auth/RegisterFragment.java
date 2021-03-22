package com.snir.shelfbook.ui.Auth;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.snir.shelfbook.R;

import java.util.HashMap;

public class RegisterFragment extends Fragment {

    private EditText username;
    private EditText name;
    private EditText email;
    private EditText password;
    private Button register;
    private TextView loginUser;

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
        View view =  inflater.inflate(R.layout.fragment_register, container, false);

        username = view.findViewById(R.id.username);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        register = view.findViewById(R.id.register);
        loginUser = view.findViewById(R.id.login_user);
        //TODO: what is this
//        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(getContext());

        loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: nav to login page
                Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_loginFragment);
                //startActivity(new Intent(RegisterActivity.this , LoginActivity.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtUsername = username.getText().toString();
                String txtName = name.getText().toString();
                String txtEmail = email.getText().toString();
                String txtPassword = password.getText().toString();

                if (TextUtils.isEmpty(txtUsername) || TextUtils.isEmpty(txtName)
                        || TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword)){
                    Toast.makeText(getContext(), "Empty credentials!", Toast.LENGTH_SHORT).show();
                } else if (txtPassword.length() < 6){
                    Toast.makeText(getContext(), "Password too short!", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(txtUsername , txtName , txtEmail , txtPassword);
                }
            }
        });

        return view;
    }

    private void registerUser(final String username, final String name, final String email, String password) {

        pd.setMessage("Please wait!");
        pd.show();

        mAuth.createUserWithEmailAndPassword(email , password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                HashMap<String , Object> map = new HashMap<>();
                map.put("name" , name);
                map.put("email", email);
                map.put("username" , username);
                map.put("id" , mAuth.getCurrentUser().getUid());
                map.put("bio" , "");
                map.put("imageurl" , "default");

                FirebaseFirestore db = FirebaseFirestore.getInstance();


                db.collection("Users").document(mAuth.getCurrentUser().getUid()).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            pd.dismiss();
                            Toast.makeText(getContext(), "Update the profile " +
                                    "for better expereince", Toast.LENGTH_SHORT).show();
                            // TODO: probably nav to home page
                            Navigation.findNavController(getView()).navigate(R.id.action_registerFragment_to_nav_home);
//                            Intent intent = new Intent(RegisterActivity.this , HomeActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
//                            finish();
                        }
                    }
                });

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