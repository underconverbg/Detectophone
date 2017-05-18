package com.underconverbg.detectophone;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

/**
 * Created by underconverbg on 2017/5/17.
 */

class DBOpenHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "person.db"; //数据库名称
    private static final int DATABASE_VERSION = 1;//数据库版本

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE detect (userid varchar(20)," +
                " phonenum varchar(30), callphonenum varchar(30)," +
                " datetime varchar(30), recordfilepath varchar(30)," +
                " type varchar(10), recordtime varchar(30))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS detect");
        onCreate(db);
    }

}