package com.example.pozivnik.Database;

public class CallBackNumberNote {
    public static final String TABLE_NAME = "CallBackNumber";

    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_NAMEN = "NAMEN";
    public static final String COLUMN_NUMBER = "NUMBER";


    private int id;
    private String number;
    private String namen;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAMEN + " TEXT,"
                    + COLUMN_NUMBER + " TEXT"
                    + ")";

    public CallBackNumberNote() {
    }

    public CallBackNumberNote(int id, String namen , String number) {
        this.id = id;
        this.namen = namen;
        this.number = number;

    }

    public String getNamen() {
        return namen;
    }

    public void setNamen(String namen) {
        this.namen = namen;
    }

    public int getId() {
        return id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }


    public void setId(int id) {
        this.id = id;
    }

}
