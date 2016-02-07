package com.sd2799.jobowakee;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by fexofenadine180mg on 11/11/15.
 */
public class JobowakeeDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "jobowakee.db";
    private static final int DATABASE_VERSION = 1;

    public JobowakeeDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " +
                JobowakeeDatabase.Users.USERS_TABLE_NAME + " (" +
                JobowakeeDatabase.Users._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                JobowakeeDatabase.Users.USER_ID + " TEXT, " +
                JobowakeeDatabase.Users.EMAIL + " TEXT, " +
                JobowakeeDatabase.Users.USERNAME + " TEXT, " +
                JobowakeeDatabase.Users.PASSWORD + " TEXT" + ");");

        db.execSQL("CREATE TABLE " +
                JobowakeeDatabase.Employers.EMPLOYERS_TABLE_NAME + " (" +
                JobowakeeDatabase.Employers._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                JobowakeeDatabase.Employers.USER_ID + " TEXT, " +
                JobowakeeDatabase.Employers.EMAIL + " TEXT, " +
                JobowakeeDatabase.Employers.FIRST_NAME + " TEXT, " +
                JobowakeeDatabase.Employers.LAST_NAME + " TEXT, " +
                JobowakeeDatabase.Employers.COMPANY_NAME + " TEXT, " +
                JobowakeeDatabase.Employers.COMPANY_ADDRESS + " TEXT, " +
                JobowakeeDatabase.Employers.COMPANY_PHONE + " TEXT);");
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + JobowakeeDatabase.Users.USERS_TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + JobowakeeDatabase.Employers.EMPLOYERS_TABLE_NAME);
        onCreate(db);
    }

}
