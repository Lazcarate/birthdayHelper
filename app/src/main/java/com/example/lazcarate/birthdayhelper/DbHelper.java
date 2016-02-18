package com.example.lazcarate.birthdayhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lazcarate on 31/1/16.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final int DB_SCHEME_VERSION = 1;
    private static final String DATABASE_NAME = "misCumples";

    public DbHelper(Context context) {

        super(context, DATABASE_NAME, null, DB_SCHEME_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creamos la tabla
        db.execSQL(DataBaseManager.CREA_TABLA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DataBaseManager.DROP_TABLE);
        onCreate(db);

    }
}
