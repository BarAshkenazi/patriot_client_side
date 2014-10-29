package com.example.android.DBConn;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseWrapper extends SQLiteOpenHelper
{
	public static String CITIES = "Cities";
	public static String CITY_ID = "_id";
	public static String CITY_NAME = "_name";
	public static String CITY_CHOSEN ="_chosen";
	private static  String DATABASE_NAME = "Cities.db";
	private static int DATABASE_VERSION = 1;

    // creation SQLite statement
    private static final String DATABASE_CREATE = "create table " + CITIES
            + "(" + CITY_ID + " integer primary key autoincrement, "
            + CITY_NAME + " text not null, " +  CITY_CHOSEN + " text not null);";

    public DataBaseWrapper(Context context) 
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + CITIES);
        onCreate(db);
    }
}
