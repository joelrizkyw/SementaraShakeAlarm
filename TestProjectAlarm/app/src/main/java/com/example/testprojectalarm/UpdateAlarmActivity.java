package com.example.testprojectalarm;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testprojectalarm.Database.DatabaseHelper;
import com.example.testprojectalarm.Model.DataAlarm;

public class UpdateAlarmActivity extends AppCompatActivity {
    private TimePicker alarmUpdatePicker;
    private Button btnEdit, btnDelete;
    private Spinner spinnerUpdateRingtone;

    String id_alarm;
    int id;

    DataAlarm dataAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_alarm);

        dataAlarm = new DataAlarm(UpdateAlarmActivity.this, "write");

        alarmUpdatePicker = (TimePicker) findViewById(R.id.alarmUpdatePicker);
        btnEdit = (Button) findViewById(R.id.btnEditAlarm);
        btnDelete = (Button) findViewById(R.id.btnDeleteAlarm);
        spinnerUpdateRingtone = (Spinner) findViewById(R.id.spinnerUpdateRingtone);

        Intent intent = getIntent();

        id_alarm = intent.getStringExtra("id alarm");

        id = Integer.parseInt(id_alarm);

        try {
            Cursor cursor = dataAlarm.getAlarmDataById(id);

            if(cursor.getCount() > 0) {
                while(cursor.moveToNext()) {
                    String hour = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_JAM_ALARM));
                    String minute = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_MENIT_ALARM));
                    String ringtone = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_RINGTONE_ALARM));

                    int hour_alarm = Integer.parseInt(hour);
                    int minute_alarm = Integer.parseInt(minute);

                    alarmUpdatePicker.setHour(hour_alarm);
                    alarmUpdatePicker.setMinute(minute_alarm);

                    spinnerUpdateRingtone.setSelection(getIndex(spinnerUpdateRingtone,
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_RINGTONE_ALARM))));
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong in update activity", Toast.LENGTH_SHORT).show();
        }
        
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAlarm(v);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAlarm(v);
            }
        });
    }

    public void editAlarm(View view) {
        int hour = alarmUpdatePicker.getHour();
        int minute = alarmUpdatePicker.getMinute();

        String sistem_am_pm = setAlarmSistemAMPM(hour);

        String ringtone = spinnerUpdateRingtone.getSelectedItem().toString();
        String song_choosed;

        switch(ringtone) {
            case "Your Name" :
                song_choosed = "Your Name";
                break;
            case "Blue Water":
                song_choosed = "Blue Water";
                break;
            case "Hikari" :
                song_choosed = "Hikari";
                break;
            default:
                song_choosed = "Choose Ringtone";
                break;
        }

        //set new alarm data
        dataAlarm.setJam_alarm(hour);
        dataAlarm.setMenit_alarm(minute);
        dataAlarm.setSistem_alarm(sistem_am_pm);

        if (song_choosed != "Choose Ringtone") {
            dataAlarm.setLagu_alarm(song_choosed);

            int update = dataAlarm.updateAlarmData(id);

            if(update > 0) {
                Toast.makeText(this, "Update data success", Toast.LENGTH_SHORT).show();

                returnHome();
            } else {
                Toast.makeText(this, "Update data error", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "You need to choose a ringtone !", Toast.LENGTH_SHORT).show();
        }

    }

    public void deleteAlarm(View view) {
        int delete = dataAlarm.deleteAlarmData(id);

        if(delete > 0) {
            Toast.makeText(UpdateAlarmActivity.this, "Data is Deleted ", Toast.LENGTH_LONG).show();

            returnHome();

            finish();
        } else {
            Toast.makeText(UpdateAlarmActivity.this, "Delete data went wrong :(" , Toast.LENGTH_LONG).show();
        }
    }

    private String setAlarmSistemAMPM(int hour) {
        String am_or_pm;

        if(hour == 0) {
            am_or_pm = "AM";
        } else if(hour < 12) {
            am_or_pm = "AM";
        } else if(hour == 12) {
            am_or_pm = "PM";
        } else {
            am_or_pm = "PM";
        }

        return am_or_pm;
    }

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }

    private void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }
}
