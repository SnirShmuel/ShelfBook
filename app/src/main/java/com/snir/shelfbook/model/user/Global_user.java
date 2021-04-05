package com.snir.shelfbook.model.user;

public class Global_user {
    public User currentUser;
    // static variable single_instance of type Singleton
    private static Global_user single_instance = null;

    // private constructor restricted to this class itself
    private Global_user()
    {
        currentUser = new User();
    }

    // static method to create instance of Singleton class
    public static Global_user getInstance()
    {
        if (single_instance == null)
            single_instance = new Global_user();

        return single_instance;
    }
}
