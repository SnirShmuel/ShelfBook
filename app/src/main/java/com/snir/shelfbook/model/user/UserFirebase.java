package com.snir.shelfbook.model.user;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.snir.shelfbook.model.book.BookModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserFirebase {
    final static String USERS_COLLECTION = "Users";

    public static void getUser(String id, final UserModel.Listener<User> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
       synchronized (UserFirebase.class){
           db.collection(USERS_COLLECTION).whereEqualTo("id", id)
                   .get().addOnCompleteListener((task)->{
               User user = null;
               if (task.isSuccessful()){
                   user = new User();
                   for(QueryDocumentSnapshot doc : task.getResult()){
                       Map<String, Object> json = doc.getData();
                       user = factory(json);
                   }
               }
               listener.onComplete(user);
           });
       }
    }


    private static User factory(Map<String, Object> json){
        User user = new User();
        user.setId((String) Objects.requireNonNull(json.get("id")));
        user.setUsername((String) Objects.requireNonNull(json.get("username")));
        user.setPassword((String) Objects.requireNonNull(json.get("password")));
        user.setName((String) Objects.requireNonNull(json.get("name")));
        user.setEmail((String) Objects.requireNonNull(json.get("email")));
        user.setCity((String) Objects.requireNonNull(json.get("city")));
        user.setPhone((String) Objects.requireNonNull(json.get("phone")));
        return user;
    }

    public  static  void updateDetails(User user, UserModel.Listener<User> listener){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection(USERS_COLLECTION).document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        Map<String,Object> updates = new HashMap<>();
        updates.put("id",user.getId());
        updates.put("username", user.getUsername());
        updates.put("password",user.getPassword());
        updates.put("name", user.getName());
        updates.put("email", user.getEmail());
        updates.put("city", user.getCity());
        updates.put("phone", user.getPhone());
//        userRef.update(updates)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        listener.onComplete(user);
//                    }
//                });

        userRef.set(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Update email and password for auth
                        FirebaseAuth.getInstance().getCurrentUser().updateEmail(user.getEmail());
                        FirebaseAuth.getInstance().getCurrentUser().updatePassword(user.getPassword());

                        listener.onComplete(user);
                    }
                });
    }
}
