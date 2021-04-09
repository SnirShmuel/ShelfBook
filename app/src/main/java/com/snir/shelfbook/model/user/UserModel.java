package com.snir.shelfbook.model.user;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.snir.shelfbook.MyApplication;
import com.snir.shelfbook.model.AppLocalDb;

import static android.content.Context.MODE_PRIVATE;

public class UserModel {
    public static final UserModel instance = new UserModel();

    public interface Listener<T> {
        void onComplete(T data);
    }

    public interface CompListener {
        void onComplete();
    }


//    public LiveData<User> getUser(){
//        refreshUser(null);
//        LiveData<User> liveData = AppLocalDb.db.userDao().getAllUsers();
//        return liveData;
//    }

    public void getUser(UserModel.Listener<User> listener) {
        UserFirebase.getUser(FirebaseAuth.getInstance().getCurrentUser().getUid(), listener);
    }

    public void refreshUser(final UserModel.CompListener listener) {
        UserFirebase.getUser(FirebaseAuth.getInstance().getCurrentUser().getUid(), new UserModel.Listener<User>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onComplete(final User data) {
                new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        long lastUpdated = 0;
                        AppLocalDb.db.userDao().insertAll(data);

                        SharedPreferences.Editor edit = MyApplication.context.getSharedPreferences("TAG", MODE_PRIVATE).edit();
                        edit.apply();
                        return "";
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if (listener != null) listener.onComplete();
                    }
                }.execute("");
            }
        });
    }

    public void updateUserDetails(User user, Listener<User> listener) {
        UserFirebase.updateDetails(user, listener);
    }

    public static void setLoginUser(Listener<User> listener) {
        UserModel.instance.getUser(new UserModel.Listener<User>() {
            @Override
            public void onComplete(User data) {
                listener.onComplete(data);
            }
        });
    }
}