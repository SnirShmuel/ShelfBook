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
    final static String QUESTION_COLLECTION = "Users";

    public static void getUser(String id, final UserModel.Listener<User> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(QUESTION_COLLECTION).whereEqualTo("id", id)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                User user = null;
                if (task.isSuccessful()){
                    user = new User();
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        Map<String, Object> json = doc.getData();
                        user = factory(json);
                    }
                }
                listener.onComplete(user);
            }
        });
    }


    private static User factory(Map<String, Object> json){
        User user = new User();
        user.setId((String) Objects.requireNonNull(json.get("id")));
        user.setUsername((String) Objects.requireNonNull(json.get("username")));
        user.setName((String) Objects.requireNonNull(json.get("name")));
        user.setEmail((String) Objects.requireNonNull(json.get("email")));
        return user;
    }

    public  static  void updateDetails(User user, UserModel.Listener<User> listener){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection(QUESTION_COLLECTION).document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        Map<String,Object> updates = new HashMap<>();
        updates.put("username", user.getUsername());
        updates.put("name", user.getName());
        updates.put("email", user.getEmail());
        updates.put("city", user.getCity());
        updates.put("phone", user.getPhone());
        userRef.update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.onComplete(user);
                    }
                });
    }
}
