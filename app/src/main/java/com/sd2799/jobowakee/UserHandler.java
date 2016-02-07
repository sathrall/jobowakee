package com.sd2799.jobowakee;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by fexofenadine180mg on 11/11/15.
 */
public class UserHandler {
    private ArrayList<User> mUsers;

    private static UserHandler sUserHandler;
    private Context mAppContext;

    private UserHandler(Context appContext) {
        mAppContext = appContext;
        mUsers = new ArrayList<User>();
    }

    public static UserHandler get(Context c) {
        if (sUserHandler == null) {
            sUserHandler = new UserHandler(c.getApplicationContext());
        }
        return sUserHandler;
    }

    public ArrayList<User> getUsers() {
        return mUsers;
    }

    public User getUser(String username) {
        for (User user : mUsers) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        return null;
    }

    public void addUser(String email, String username, String password) {
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        mUsers.add(user);
    }

    public void addUser(UUID id, String email, String username, String password) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        mUsers.add(user);
    }
}
