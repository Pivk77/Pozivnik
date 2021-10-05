package com.example.pozivnik.Database;

public class MessageNote {
    public static final String TABLE_NAME = "messages";

    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_MESSAGE = "MESSAGE";
    public static final String COLUMN_NUMBER = "NUMBER";
    public static final String COLUMN_TIMESTAMP = "TIMESTAMP";

    private int id;
    private String message;
    private String timestamp;
    private String number;
    private String name;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NUMBER + " TEXT,"
                    + COLUMN_MESSAGE + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public MessageNote() {
    }

    public MessageNote(int id, String number , String message, String timestamp) {
        this.id = id;
        this.message = message;
        this.timestamp = timestamp;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}