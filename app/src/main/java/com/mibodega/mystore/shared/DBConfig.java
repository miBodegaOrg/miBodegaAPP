package com.mibodega.mystore.shared;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBConfig extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    private  static final String DATABASE_NOMBRE ="DBConfigMB";

    public DBConfig(Context context) {
        super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String[] tables = {
                "CREATE TABLE config (\n" +
                        "    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                        "    fechaJob VARCHAR (250),\n" +
                        "    usuario VARCHAR (150)\n" +
                        "    )",
                "CREATE TABLE chat (\n" +
                        " ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                        " owner VARCHAR (150),\n" +
                        " message TEXT,\n" +
                        " fecha VARCHAR (250)\n" +
                        ")\n",
                "CREATE TABLE recomendation (\n" +
                        " ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                        " message TEXT,\n" +
                        " fecha VARCHAR (250)\n" +
                        ")\n",
                "CREATE TABLE tokens (\n" +
                        " ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                        " token TEXT\n" +
                        ")\n",
        };
        for(String table : tables){
            db.execSQL(table);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS config");
        db.execSQL("DROP TABLE IF EXISTS chat");
        db.execSQL("DROP TABLE IF EXISTS recomendation");
        db.execSQL("DROP TABLE IF EXISTS tokens");
        onCreate(db);
    }
}