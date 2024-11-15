package com.mibodega.mystore.shared;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.mibodega.mystore.models.common.ChatMessage;
import com.mibodega.mystore.models.common.RecomendationMessage;

import java.util.ArrayList;
import java.util.List;


public class DBfunctionsTableData {
    public boolean insert_message_sqlite(Context context, ChatMessage message) {
        boolean Finality = false;
        DBConfig dbconfig = new DBConfig(context);
        SQLiteDatabase db = dbconfig.getWritableDatabase();
        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put("owner", message.getOwner());
            values.put("message", message.getMessage());
            values.put("fecha", message.getFecha());
            // Agrega más columnas y valores según tus necesidades
            db.insert("chat", null, values);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            Finality = true;
        }
        db.close();
        return Finality;
    }
    public boolean insert_recomendation_sqlite(Context context, RecomendationMessage message) {
        boolean Finality = false;
        DBConfig dbconfig = new DBConfig(context);
        SQLiteDatabase db = dbconfig.getWritableDatabase();
        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put("message", message.getMessage());
            values.put("fecha", message.getFecha());
            // Agrega más columnas y valores según tus necesidades
            db.insert("recomendation", null, values);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            Finality = true;
        }
        db.close();
        return Finality;

    }

    public String get_recomendation_save(Context context) {
        String recomendationSaved = "";
        DBConfig dbconfig = new DBConfig(context);
        SQLiteDatabase db = dbconfig.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT message FROM recomendation ORDER BY fecha DESC LIMIT 1;", null);

        if (cursor.moveToFirst()) {
            recomendationSaved = cursor.getString(0);
            System.out.println("--> "+recomendationSaved);
        }
        cursor.close();
        if (recomendationSaved == null) recomendationSaved = "";
        return recomendationSaved;
    }
    public boolean insertUserRemember(Context context, String token) {

        boolean Finality = false;
        DBConfig dbConfig = new DBConfig(context);
        SQLiteDatabase db = dbConfig.getWritableDatabase();
        db.beginTransaction();
        try {
            // Verificar si hay datos en la tabla antes de actualizar
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM config", null);
            if (cursor != null) {
                cursor.moveToFirst();
                int count = cursor.getInt(0);
                cursor.close();
                if (count > 0) {
                    // Hay datos, realizar la actualización
                    db.execSQL("UPDATE tokens SET token='"+token+"'");
                } else {
                    ContentValues values = new ContentValues();
                    values.put("token", token);
                    db.insert("tokens", null, values);
                }
                Finality = true;
            }


            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            Finality = true;
        }
        db.close();
        return Finality;
    }
    public String get_user_save(Context context) {
        String _token = "";
        DBConfig dbconfig = new DBConfig(context);
        SQLiteDatabase db = dbconfig.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT token FROM tokens  LIMIT 1", null);

        if (cursor.moveToFirst()) {
            _token = cursor.getString(0);
            System.out.println("--> "+_token);
        }
        cursor.close();
        if (_token == null) _token = "";
        return _token;
    }
    public void cleanTokensSignIn(Context context) {
        DBConfig dbConfig = new DBConfig(context);
        SQLiteDatabase db = dbConfig.getWritableDatabase();
        db.beginTransaction();
        try {
            db.execSQL("DELETE FROM tokens");
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        db.close();
    }


    public List<ChatMessage> get_messages_sqlite(Context context) {
        List<ChatMessage> list = new ArrayList<>();

        DBConfig dbconfig = new DBConfig(context);
        SQLiteDatabase dbs = dbconfig.getWritableDatabase();
        Cursor cursor = dbs.rawQuery("SELECT ID,message,owner,fecha " +
                "FROM chat ORDER BY fecha ASC;\n", null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new ChatMessage(
                                cursor.getInt(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                cursor.getString(3)
                        )
                );
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

}
