package com.example.testprojectalarm.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    //NAMA DATABASE ALARM
    public static final String DB_NAME = "ShakeAlarm.db";

    //NAMA TABEL ALARM
    public static final String TABLE_NAME = "tbl_alarm";

    //KOLOM-KOLOM TABEL ALARM
    public static final String COL_ID_ALARM = "ID_Alarm";
    public static final String COL_JAM_ALARM = "Jam_Alarm";
    public static final String COL_MENIT_ALARM = "Menit_Alarm";
    public static final String COL_SISTEM_AM_PM = "Sistem_Alarm";
    public static final String COL_RINGTONE_ALARM = "Lagu_Alarm";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + COL_ID_ALARM + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_JAM_ALARM + " INTEGER NOT NULL, " + COL_MENIT_ALARM + " INTEGER NOT NULL, " + COL_SISTEM_AM_PM + " TEXT NOT NULL, "
                + COL_RINGTONE_ALARM + " TEXT NOT NULL)";

        //BUAT TABLE ALARM
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
