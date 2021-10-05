package com.example.pozivnik.Database;

public class NumberNote {
    public static final String TABLE_NAME = "prozilci";

    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_NUMBER = "NUMBER";
    public static final String COLUMN_TIMESTAMP = "TIMESTAMP";
    public static final String COLUMN_FILTER_INCLUDES = "FILTER_INCLUDES";
    public static final String COLUMN_FILTER_EXCLUDES = "FILTER_EXCLUDES";


    private int id;
    private String timestamp;
    private String number;
    private String name;
    private String filterIncludes;
    private String filterExcludes;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_NUMBER + " TEXT,"
                    + COLUMN_FILTER_INCLUDES + " TEXT,"
                    + COLUMN_FILTER_EXCLUDES + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public NumberNote() {
    }

    public NumberNote(int id, String name , String number, String timestamp, String filterIncludes, String filterExcludes) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.timestamp = timestamp;
        this.filterIncludes = filterIncludes;
        this.filterExcludes = filterExcludes;

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

    public String getFilterIncludes() {
        return filterIncludes;
    }

    public void setFilterIncludes(String filterIncludes) {
        this.filterIncludes = filterIncludes;
    }

    public String getFilterExcludes() {
        return filterExcludes;
    }

    public void setFilterExcludes(String filterExcludes) {
        this.filterExcludes = filterExcludes;
    }
}