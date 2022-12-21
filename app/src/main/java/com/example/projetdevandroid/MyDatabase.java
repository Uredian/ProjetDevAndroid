package com.example.projetdevandroid;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class MyDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_TABLE_NAME = "imageStorage";
    private static final String PKEY = "pkey";
    private static final String COL1 = "image";
    private static final String TAG = "DATABASE : ";

    MyDatabase(Context context) {
        super(context, DATABASE_TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String DATABASE_TABLE_CREATE = "CREATE TABLE " + DATABASE_TABLE_NAME + " (" +
                PKEY + " INTEGER PRIMARY KEY," +
                COL1 + " BLOB);";
        db.execSQL(DATABASE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_NAME);

        // create new table
        onCreate(db);
    }


    public long insertData(Bitmap bitmap) {
        //Conversion de bitmap en byte array pour qu'il puisse etre stocké dans la bdd
        byte[] bytes = DbBitmapConverter.getBytes(bitmap);

        Log.i(TAG, " Insert in database");

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        ContentValues values = new ContentValues();
        values.put(COL1, bytes);

        long pkey = db.insertOrThrow(DATABASE_TABLE_NAME, null, values);
        db.setTransactionSuccessful();
        db.endTransaction();

        return pkey;
    }

    @SuppressLint("Range")
    public Map<Integer, Bitmap> readDatabase() {
        /*On sélectionne toutes les images dans la bdd, on les reconverti en Bitmap et on les
         **retourne ainsi que leur clé primaire
         */
        Log.i(TAG, "Reading database...");
        String select = "SELECT * from " + DATABASE_TABLE_NAME;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(select, null);

        Log.i(TAG, "Number of entries: " + cursor.getCount());

        Map<Integer, Bitmap> images = new HashMap<>();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                byte[] blob = cursor.getBlob(cursor.getColumnIndex(COL1));
                int id = cursor.getInt(cursor.getColumnIndex(PKEY));
                images.put(id, (DbBitmapConverter.getImage(blob)));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return images;
    }

    public void deleteImage(Integer pkey) {
        //On supprime l'image ayant pour clé primaire pkey
        SQLiteDatabase db = getWritableDatabase();
        db.delete(DATABASE_TABLE_NAME, PKEY + "=?", new String[]{Integer.toString(pkey)});
    }
}