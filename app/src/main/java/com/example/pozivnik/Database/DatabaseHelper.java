package com.example.pozivnik.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {


    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "database_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        // create notes table
        db.execSQL(MessageNote.CREATE_TABLE);
        db.execSQL(NumberNote.CREATE_TABLE);
        db.execSQL(CallBackNumberNote.CREATE_TABLE);
        values.put(CallBackNumberNote.COLUMN_NAMEN, "PRIDEM");
        values.put(CallBackNumberNote.COLUMN_NUMBER, "059074870");
        db.insert(CallBackNumberNote.TABLE_NAME, null, values);
        values.put(CallBackNumberNote.COLUMN_NAMEN, "NE PRIDEM");
        values.put(CallBackNumberNote.COLUMN_NUMBER, "059074879");
        db.insert(CallBackNumberNote.TABLE_NAME, null, values);
        values.put(CallBackNumberNote.COLUMN_NAMEN, "PRIDEM KASNEJE");
        values.put(CallBackNumberNote.COLUMN_NUMBER, "059074874");
        db.insert(CallBackNumberNote.TABLE_NAME, null, values);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + MessageNote.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + NumberNote.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CallBackNumberNote.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }
    public long insertMessageNote(String number, String message) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(MessageNote.COLUMN_NUMBER, number);
        values.put(MessageNote.COLUMN_MESSAGE, message);


        // insert row
        long id = db.insert(MessageNote.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }
    public long insertNumberNote(String name, String number, String filterIncludes, String filterExcludes) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(NumberNote.COLUMN_NAME, name);
        values.put(NumberNote.COLUMN_NUMBER, number);
        values.put(NumberNote.COLUMN_FILTER_INCLUDES, filterIncludes);
        values.put(NumberNote.COLUMN_FILTER_EXCLUDES, filterExcludes);
        // insert row
        long id = db.insert(NumberNote.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }
    public int updateNumberNote(NumberNote numberNote) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NumberNote.COLUMN_NAME, numberNote.getName());
        values.put(NumberNote.COLUMN_NUMBER, numberNote.getNumber());
        values.put(NumberNote.COLUMN_FILTER_INCLUDES, numberNote.getFilterIncludes());
        values.put(NumberNote.COLUMN_FILTER_EXCLUDES, numberNote.getFilterExcludes());
        // updating row
        return db.update(NumberNote.TABLE_NAME, values, NumberNote.COLUMN_ID + " = ?",
                new String[]{String.valueOf(numberNote.getId())});
    }
    public int updateCallBackNumberNote(CallBackNumberNote callBackNumberNote) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CallBackNumberNote.COLUMN_NAMEN, callBackNumberNote.getNamen());
        values.put(CallBackNumberNote.COLUMN_NUMBER, callBackNumberNote.getNumber());
        // updating row
        return db.update(CallBackNumberNote.TABLE_NAME, values, CallBackNumberNote.COLUMN_ID + " = ?",
                new String[]{String.valueOf(callBackNumberNote.getId())});
    }

    public MessageNote getMessageNote(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(MessageNote.TABLE_NAME,
                new String[]{MessageNote.COLUMN_ID, MessageNote.COLUMN_NUMBER , MessageNote.COLUMN_MESSAGE, MessageNote.COLUMN_TIMESTAMP},
                MessageNote.COLUMN_ID + "= ?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare messageNote object
        MessageNote messageNote = new MessageNote(
                cursor.getInt(cursor.getColumnIndex(MessageNote.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(MessageNote.COLUMN_NUMBER)),
                cursor.getString(cursor.getColumnIndex(MessageNote.COLUMN_MESSAGE)),
                cursor.getString(cursor.getColumnIndex(MessageNote.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return messageNote;
    }

    public NumberNote getNumberNote(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(NumberNote.TABLE_NAME,
                new String[]{NumberNote.COLUMN_ID, NumberNote.COLUMN_NAME, NumberNote.COLUMN_NUMBER, NumberNote.COLUMN_TIMESTAMP, NumberNote.COLUMN_FILTER_INCLUDES, NumberNote.COLUMN_FILTER_EXCLUDES},
                NumberNote.COLUMN_ID + "= ?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        NumberNote numberNote = new NumberNote(
                cursor.getInt(cursor.getColumnIndex(NumberNote.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(NumberNote.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(NumberNote.COLUMN_NUMBER)),
                cursor.getString(cursor.getColumnIndex(NumberNote.COLUMN_TIMESTAMP)),
                cursor.getString(cursor.getColumnIndex(NumberNote.COLUMN_FILTER_INCLUDES)),
                cursor.getString(cursor.getColumnIndex(NumberNote.COLUMN_FILTER_EXCLUDES)));

        // close the db connection
        cursor.close();

        return numberNote;
    }
    public CallBackNumberNote getCallBackNumberNote(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(CallBackNumberNote.TABLE_NAME,
                new String[]{CallBackNumberNote.COLUMN_ID, CallBackNumberNote.COLUMN_NAMEN, CallBackNumberNote.COLUMN_NUMBER},
                CallBackNumberNote.COLUMN_ID + "= ?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        CallBackNumberNote callBackNumberNote = new CallBackNumberNote(
                cursor.getInt(cursor.getColumnIndex(CallBackNumberNote.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(CallBackNumberNote.COLUMN_NAMEN)),
                cursor.getString(cursor.getColumnIndex(CallBackNumberNote.COLUMN_NUMBER)));

        // close the db connection
        cursor.close();

        return callBackNumberNote;
    }

    public List<MessageNote> getAllMessageNotes() {
        List<MessageNote> messageNotes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + MessageNote.TABLE_NAME + " ORDER BY " +
                MessageNote.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MessageNote messageNote = new MessageNote();
                messageNote.setId(cursor.getInt(cursor.getColumnIndex(MessageNote.COLUMN_ID)));
                messageNote.setMessage(cursor.getString(cursor.getColumnIndex(MessageNote.COLUMN_MESSAGE)));
                messageNote.setNumber(cursor.getString(cursor.getColumnIndex(MessageNote.COLUMN_NUMBER)));
                messageNote.setTimestamp(cursor.getString(cursor.getColumnIndex(MessageNote.COLUMN_TIMESTAMP)));

                messageNotes.add(messageNote);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return messageNotes list
        return messageNotes;
    }
    public List<NumberNote> getAllNumberNotes() {
        List<NumberNote> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + NumberNote.TABLE_NAME + " ORDER BY " +
                NumberNote.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                NumberNote numberNote = new NumberNote();
                numberNote.setId(cursor.getInt(cursor.getColumnIndex(NumberNote.COLUMN_ID)));
                numberNote.setName(cursor.getString(cursor.getColumnIndex(NumberNote.COLUMN_NAME)));
                numberNote.setNumber(cursor.getString(cursor.getColumnIndex(NumberNote.COLUMN_NUMBER)));
                numberNote.setTimestamp(cursor.getString(cursor.getColumnIndex(NumberNote.COLUMN_TIMESTAMP)));
                numberNote.setFilterIncludes(cursor.getString(cursor.getColumnIndex(NumberNote.COLUMN_FILTER_INCLUDES)));
                numberNote.setFilterExcludes(cursor.getString(cursor.getColumnIndex(NumberNote.COLUMN_FILTER_EXCLUDES)));

                notes.add(numberNote);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }
    public ArrayList<String> getAllNumbers() {
        ArrayList<String> numbers = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT "+ NumberNote.COLUMN_NUMBER + " FROM " + NumberNote.TABLE_NAME + " ORDER BY " +
                NumberNote.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                numbers.add(cursor.getString(cursor.getColumnIndex(NumberNote.COLUMN_NUMBER)));
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return numbers;
    }
    public List<CallBackNumberNote> getAllCallBackNumberNotes() {
        List<CallBackNumberNote> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + CallBackNumberNote.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CallBackNumberNote CallBackNumberNote = new CallBackNumberNote();
                CallBackNumberNote.setId(cursor.getInt(cursor.getColumnIndex(CallBackNumberNote.COLUMN_ID)));
                CallBackNumberNote.setNamen(cursor.getString(cursor.getColumnIndex(CallBackNumberNote.COLUMN_NAMEN)));
                CallBackNumberNote.setNumber(cursor.getString(cursor.getColumnIndex(CallBackNumberNote.COLUMN_NUMBER)));

                notes.add(CallBackNumberNote);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }
    public ArrayList<String> getAllCallBackNumbers() {
        ArrayList<String> callBackNumbers = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT "+ CallBackNumberNote.COLUMN_NUMBER + " FROM " + CallBackNumberNote.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                callBackNumbers.add(cursor.getString(cursor.getColumnIndex(NumberNote.COLUMN_NUMBER)));
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return callBackNumbers;
    }

    public int getMessageNotesCount() {
        String countQuery = "SELECT  * FROM " + MessageNote.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }
    public int getNumberNotesCount() {
        String countQuery = "SELECT  * FROM " + NumberNote.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }
    public int getCallBackNumberNotesCount() {
        String countQuery = "SELECT  * FROM " + CallBackNumberNote.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }
    public void deleteMessageNote(MessageNote messageNote) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MessageNote.TABLE_NAME, MessageNote.COLUMN_ID + " = ?",
                new String[]{String.valueOf(messageNote.getId())});
        db.close();
    }
    public void deleteNumberNote(NumberNote note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NumberNote.TABLE_NAME, NumberNote.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }
    public void deleteAllMessageNote(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MessageNote.TABLE_NAME,null,null);
        db.close();
    }
    public NumberNote getNumberFilters(String number){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(NumberNote.TABLE_NAME,
                new String[]{NumberNote.COLUMN_ID, NumberNote.COLUMN_NAME, NumberNote.COLUMN_NUMBER, NumberNote.COLUMN_TIMESTAMP, NumberNote.COLUMN_FILTER_INCLUDES, NumberNote.COLUMN_FILTER_EXCLUDES},
                NumberNote.COLUMN_NUMBER + "= ?",
                new String[]{String.valueOf(number)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        NumberNote numberNote = new NumberNote(
                cursor.getInt(cursor.getColumnIndex(NumberNote.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(NumberNote.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(NumberNote.COLUMN_NUMBER)),
                cursor.getString(cursor.getColumnIndex(NumberNote.COLUMN_TIMESTAMP)),
                cursor.getString(cursor.getColumnIndex(NumberNote.COLUMN_FILTER_INCLUDES)),
                cursor.getString(cursor.getColumnIndex(NumberNote.COLUMN_FILTER_EXCLUDES)));
        // close the db connection
        cursor.close();

        return numberNote;
    }
}