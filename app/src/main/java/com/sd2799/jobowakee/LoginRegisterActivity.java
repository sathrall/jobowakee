package com.sd2799.jobowakee;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.UUID;

/**
 * Created by student on 11/4/2015.
 */
public class LoginRegisterActivity extends Activity {
    private static final String TAG = "LoginRegisterActivity";

    public static final String IS_LOGGED_IN = "is_logged_in";

    private JobowakeeDatabaseHelper mDatabase = null;
    private SQLiteDatabase mDB = null;
    private Cursor mCursor = null;

    public SharedPreferences pref;
    public SharedPreferences.Editor editor;

    private String tableUsers = JobowakeeDatabase.Users.USERS_TABLE_NAME;
    private String tableEmployers = JobowakeeDatabase.Employers.EMPLOYERS_TABLE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        pref = getApplicationContext().getSharedPreferences("UserStatus", 0);
        editor = pref.edit();

        mDatabase = new JobowakeeDatabaseHelper(this.getApplicationContext());
        mDB = mDatabase.getWritableDatabase();
    }

    @Override
    public void onBackPressed() {
        if (pref.getBoolean(LoginRegisterActivity.IS_LOGGED_IN, false)) {
            Intent main = new Intent(LoginRegisterActivity.this, MainActivity.class);
            startActivity(main);
            finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mDB != null) {
            mDB.close();
        }

        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    public boolean canLogin(String username, String password) {
        int count = -1;
        Cursor c = null;

        try {
            count = (int) DatabaseUtils.queryNumEntries(mDB, tableUsers,
                    "USERNAME=? AND PASSWORD=?", new String[]{username, password});
        } finally {
            if (c != null) {
                c.close();
            }
        }

        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    protected boolean registerUser(String email, String username, String password) {
        // create the user id
        String userId = UUID.randomUUID().toString();
        boolean success = false;

        mDB.beginTransaction();

        try {
            ContentValues userRecordToAdd = new ContentValues();
            userRecordToAdd.put(JobowakeeDatabase.Users.USER_ID, userId);
            userRecordToAdd.put(JobowakeeDatabase.Users.EMAIL, email);
            userRecordToAdd.put(JobowakeeDatabase.Users.USERNAME, username);
            userRecordToAdd.put(JobowakeeDatabase.Users.PASSWORD, password);
            mDB.insertOrThrow(JobowakeeDatabase.Users.USERS_TABLE_NAME,
                    JobowakeeDatabase.Users.USER_ID, userRecordToAdd);
            mDB.setTransactionSuccessful();
            success = true;
        } catch (SQLException e) {
            Log.d(TAG, "SQLException: " + e);
        } catch (Exception e) {
            Log.d(TAG, "Other Exception: " + e);
        } finally {
            mDB.endTransaction();
        }

        return success;
    }

    protected boolean checkUserExists(String username) {
        int count = -1;
        Cursor c = null;
        try {
            count = (int) DatabaseUtils.queryNumEntries(mDB, tableUsers,
                    "USERNAME=?", new String[]{username});
        } finally {
            if (c != null) {
                c.close();
            }
        }

        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    protected boolean hasProfile(UUID id) {
        String userId = id.toString();
        int count = -1;
        Cursor c = null;
        try {
            count = (int) DatabaseUtils.queryNumEntries(mDB, tableEmployers,
                    "USER_ID=?", new String[]{userId});
        } finally {
            if (c != null) {
                c.close();
            }
        }

        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    protected User setUserData(String username) {
        Cursor c = null;
        UUID userId = null;
        String email = null;
        String password = null;

        try {
            String query = "SELECT * FROM table_users WHERE USERNAME = ?";
            c = mDB.rawQuery(query, new String[]{username});
            if (c.moveToFirst()) {
                userId = UUID.fromString(c.getString(c.getColumnIndex("user_id")));
                email = c.getString(c.getColumnIndex("email"));
                password = c.getString(c.getColumnIndex("password"));
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }

        if (userId != null && email != null && password != null) {
            UserHandler userHandler = UserHandler.get(LoginRegisterActivity.this);
            userHandler.addUser(userId, email, username, password);
            return userHandler.getUser(username);
        } else {
            Log.i(TAG, "Could not create user data object");
            return null;
        }
    }

}
