package com.snir.shelfbook.model.user;

public class LoginUser {
    public User userData;
    // static variable single_instance of type Singleton
    private static LoginUser single_instance = null;

    // private constructor restricted to this class itself
    private LoginUser() {
        userData = new User();
    }

    private LoginUser(User user) {
        userData = user;
    }

    // static method to create instance of Singleton class
    public static LoginUser getUser() {
            if (single_instance == null) {
                single_instance = new LoginUser();
            }

            return single_instance;
    }
    public void setUserData(User user){
        userData = user;
    }
}
