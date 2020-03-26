package com.example.testprojectalarm;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testprojectalarm.Model.DataAlarm;

public class AlarmActivity extends AppCompatActivity {
    private TimePicker alarmPicker;

    private TextView txtAddAlarm;
    private TextView txtRingtone;

    private Button btnAddAlarm;
    private Spinner spinnerRingtone;

    DataAlarm data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Typeface customFont = Typeface.createFromAsset(getAssets(),
                "font/sen_bold.ttf");

        alarmPicker = (TimePicker) findViewById(R.id.alarmPicker);
        txtAddAlarm = (TextView) findViewById(R.id.txtAddAlarm);
        txtRingtone = (TextView) findViewById(R.id.txtRingtone);
        btnAddAlarm = (Button) findViewById(R.id.btnAddAlarm);
        spinnerRingtone = (Spinner) findViewById(R.id.spinnerRingtone);

        txtAddAlarm.setTypeface(customFont);

        btnAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAlarmData(v);
            }
        });
    }

    private void addAlarmData(View view) {
        int hour = alarmPicker.getHour();
        int minute = alarmPicker.getMinute();

        String sistem_am_pm = setAlarmSistemAMPM(hour);

        String ringtone = spinnerRingtone.getSelectedItem().toString();
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

        data = new DataAlarm(AlarmActivity.this, "write");

        //set
        data.setJam_alarm(hour);
        data.setMenit_alarm(minute);
        data.setSistem_alarm(sistem_am_pm);

        if (song_choosed != "Choose Ringtone") {
            data.setLagu_alarm(song_choosed);

            boolean insert = data.insertAlarmData();

            if(insert == true) {
                Toast.makeText(this, "Insert data success", Toast.LENGTH_SHORT).show();

                returnHome();
            } else {
                Toast.makeText(this, "Insert data error", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "You need to choose a ringtone !", Toast.LENGTH_SHORT).show();
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

    private void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }
}
