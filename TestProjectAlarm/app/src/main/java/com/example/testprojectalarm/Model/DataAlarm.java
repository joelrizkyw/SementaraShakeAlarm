package com.example.testprojectalarm.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.testprojectalarm.Database.DatabaseHelper;

public class DataAlarm {
    private DatabaseHelper myDatabase;

    private SQLiteDatabase databaseCrud;

    private int jam_alarm, menit_alarm;
    private String lagu_alarm, sistem_alarm;

    /**
     *
     * @param context
     * @param purpose UNTUK MENGETAHUI APAKAH "READ" ATAU "WRITE"
     */
    public DataAlarm(Context context, String purpose) {
        //BUKA KONEKSI DATABASE
        myDatabase = new DatabaseHelper(context);

        if(purpose == "write") {
            //MENAMBAH, MENGHAPUS, ATAU MENGUBAH DATA PADA TABEL
            databaseCrud = myDatabase.getWritableDatabase();
        } else if(purpose == "read") {
            //MEMBACA DATA PADA TABEL
            databaseCrud = myDatabase.getReadableDatabase();
        }
    }

    public void setJam_alarm(int jam_alarm) {
        this.jam_alarm = jam_alarm;
    }

    public void setMenit_alarm(int menit_alarm) {
        this.menit_alarm = menit_alarm;
    }

    public void setSistem_alarm(String sistem_alarm) {
        this.sistem_alarm = sistem_alarm;
    }

    public void setLagu_alarm(String lagu_alarm) {
        this.lagu_alarm = lagu_alarm;
    }

    /**
     * UNTUK MEMBACA DATA DARI TABEL ALARM
     * @return OBJEK CURSOR
     */
    public Cursor readAlarmData() {
        Cursor cursor = databaseCrud.rawQuery("SELECT rowid AS _id, * FROM "
                + DatabaseHelper.TABLE_NAME, null);

        if(cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    /**
     * UNTUK MENAMBAHKAN DATA KE DALAM TABEL ALARM
     * @return TRUE jika insert data berhasil, FALSE jika gagal
     */
    public boolean insertAlarmData() {
        ContentValues cv = new ContentValues();

        //DATA JAM MENIT SISTEM AM/PM
        cv.put(DatabaseHelper.COL_JAM_ALARM, jam_alarm);
        cv.put(DatabaseHelper.COL_MENIT_ALARM, menit_alarm);
        cv.put(DatabaseHelper.COL_SISTEM_AM_PM, sistem_alarm);
        cv.put(DatabaseHelper.COL_RINGTONE_ALARM, lagu_alarm);

        long insert = databaseCrud.insert(DatabaseHelper.TABLE_NAME, null, cv);

        if(insert != -1) {
            return true;
        }

        return false;
    }

    /**
     *
     * @param id
     * @return NILAI DARI VARIABEL "update"
     */
    public int updateAlarmData(int id) {
        ContentValues cv = new ContentValues();

        //DATA JAM MENIT SISTEM AM/PM
        cv.put(DatabaseHelper.COL_JAM_ALARM, jam_alarm);
        cv.put(DatabaseHelper.COL_MENIT_ALARM, menit_alarm);
        cv.put(DatabaseHelper.COL_SISTEM_AM_PM, sistem_alarm);
        cv.put(DatabaseHelper.COL_RINGTONE_ALARM, lagu_alarm);

        int update = databaseCrud.update(DatabaseHelper.TABLE_NAME, cv,
                DatabaseHelper.COL_ID_ALARM + " = " + id, null);

        return update;
    }

    public int deleteAlarmData(int id) {
        int delete = databaseCrud.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.COL_ID_ALARM + " = " + id, null);

        return delete;
    }

    public Cursor getAlarmDataById(int id) {
        Cursor cursor = databaseCrud.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME +
                " WHERE " + DatabaseHelper.COL_ID_ALARM + " = " + id, null);

        return cursor;
    }
}
