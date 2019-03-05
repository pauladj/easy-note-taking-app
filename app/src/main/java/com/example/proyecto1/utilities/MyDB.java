package com.example.proyecto1.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.proyecto1.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MyDB extends SQLiteOpenHelper {

    public MyDB(@Nullable Context context, @Nullable String name,
                @Nullable SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table Users
        db.execSQL("CREATE TABLE Users ('username' CHAR(255) PRIMARY KEY NOT NULL, 'password' " +
                "CHAR(255) NOT NULL, 'active' BIT DEFAULT 0)");

        // Create table Tags
        db.execSQL("CREATE TABLE Tags ('id' INTEGER PRIMARY KEY AUTOINCREMENT," +
                " 'name' CHAR(255) NOT NULL UNIQUE, 'username' INTEGER, FOREIGN KEY('username') "+
                " REFERENCES Users('username') ON DELETE CASCADE)");

        // Create table Notes
        db.execSQL("CREATE TABLE Notes ('id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'title' CHAR(255) NOT NULL, 'fileContent' CHAR(255) NOT NULL, 'date' DATETIME " +
                "NOT NULL DEFAULT CURRENT_TIMESTAMP, 'labelId' INTEGER, 'username' CHAR(255), " +
                "FOREIGN KEY('labelId') REFERENCES Tags('id') ON DELETE SET NULL, " +
                "FOREIGN KEY('username') REFERENCES Users('username') ON DELETE CASCADE)");

        // Insert dummy data
        db.execSQL("INSERT INTO Users VALUES ('admin', '1111', 0)");
        db.execSQL("INSERT INTO Tags(id, name, username) VALUES (1, 'tagPrueba', 'admin')");
        db.execSQL("INSERT INTO Notes(fileContent, labelId, title, username) VALUES ('prueba', 1, 'this is the title', 'admin')");
        db.execSQL("INSERT INTO Notes(fileContent, labelId, title, username) VALUES (' klsdfjkldf ksdjfksjdfks jdfksjdfksd fjkdfj f skdf jskjdf df kdfjskld fjkd jkdjf kdfj dfklf', 1, 'this is the title', 'admin')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        // Delete the existing tables
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS Tags");
        db.execSQL("DROP TABLE IF EXISTS Notes");

        // Create the tables again
        onCreate(db);
    }

    /**
     * Check if a username exists in database
     * @param username
     * @return True - exists, False - does not exist
     */
    public Boolean checkIfUsernameExists(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT username FROM Users WHERE username='" + username + "'", null);

        if (c.moveToNext() != false) {
            // there is a user with these data
            return true;
        }
        c.close();
        db.close();
        return false;
    }

    /**
     * Checks if a username exists with that password
     * @param username
     * @param password
     * @return True or False
     */
    public Boolean checkIfUserCanBeLoggedIn(String username, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT username FROM Users WHERE username='" + username + "' AND password='"+password + "'", null);

        if (c.moveToNext() != false) {
            // there is a user with these data
            return true;
        }
        c.close();
        db.close();
        return false;
    }

    /**
     * Sets the username as active
     * @param username
     */
    public void setUsernameAsActive(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues modification = new ContentValues();
        modification.put("active", 1);
        db.update("Users", modification, "username='" + username + "'", null);
        db.close();
    }

    /**
     * Gets the active username if there's one
     * @return the active username or null
     */
    public String getActiveUsername(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT username FROM Users WHERE active=1", null);

        if (c.moveToNext() != false) {
            // there is a user with these data
            String username = c.getString(0);
            return username;
        }
        c.close();
        db.close();
        return null;
    }

    /**
     * Get notes data to show on the main screen
     * @param username - of which we have to get the notes
     * @return titles, dates and tags of the notes
     */
    public ArrayList<ArrayList<String>> getNotesDataByUser(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT title, date, name FROM Notes INNER JOIN Tags ON Notes.labelId=Tags.id WHERE Notes.username='" + username + "'", null);

        ArrayList<String> notesTitles = new ArrayList<>();
        ArrayList<String> notesDates = new ArrayList<>();
        ArrayList<String> notesTagsNames = new ArrayList<>();

        while (c.moveToNext()) {
            // there is a user with these data
            String title = c.getString(0);
            String date = c.getString(1);
            String tagName = c.getString(2);

            notesTitles.add(title);
            notesDates.add(date);
            notesTagsNames.add(tagName);
        }
        c.close();
        db.close();

        ArrayList<ArrayList<String>> notesData = new ArrayList<>();
        notesData.add(notesTitles);
        notesData.add(notesDates);
        notesData.add(notesTagsNames);

        return notesData;
    }
}
