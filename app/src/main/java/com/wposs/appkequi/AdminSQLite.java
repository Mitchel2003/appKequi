package com.wposs.appkequi;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class AdminSQLite extends SQLiteOpenHelper{


    public AdminSQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase DataBase) {
        DataBase.execSQL("CREATE TABLE usuario(id INT(5) PRIMARY KEY NOT NULL,nombre VARCHAR(15), cedula INT(15), beneficio VARCHAR(2))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int getCont(String name){
        SQLiteDatabase bd=this.getReadableDatabase();
        Cursor select =bd.rawQuery("select count (*) from "+name,null);

        int num=0;
        if(select !=null && select.moveToFirst()){
            num=select.getInt(0);
            select.close();
        }
        return num;
    }
}
