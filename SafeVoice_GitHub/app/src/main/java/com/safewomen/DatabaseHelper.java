package com.safewomen;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "safevoice.db";
    private static final int DB_VERSION = 2;

    private static final String TABLE_USER = "users";
    private static final String COL_ID = "id";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASS = "password";
    private static final String COL_TRUST = "trusted_number";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE " + TABLE_USER + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_EMAIL + " TEXT, " +
                COL_PASS + " TEXT, " +
                COL_TRUST + " TEXT);";
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    public boolean insertUser(String email, String pass, String trust) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_EMAIL, email);
        cv.put(COL_PASS, pass);
        cv.put(COL_TRUST, trust);
        long id = db.insert(TABLE_USER, null, cv);
        return id != -1;
    }

    public boolean checkLogin(String email, String pass) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_USER, new String[]{COL_ID}, COL_EMAIL+"=? AND "+COL_PASS+"=?", new String[]{email, pass}, null, null, null);
        boolean ok = c.moveToFirst();
        c.close();
        return ok;
    }

    public String getTrustedNumber() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_USER, new String[]{COL_TRUST}, null, null, null, null, COL_ID + " DESC", "1");
        if (c.moveToFirst()) {
            String num = c.getString(0);
            c.close();
            return num;
        }
        c.close();
        return "";
    }
}
