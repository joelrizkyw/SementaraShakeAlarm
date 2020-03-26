package com.example.testprojectalarm;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testprojectalarm.Database.DatabaseHelper;
import com.example.testprojectalarm.Model.DataAlarm;
import com.example.testprojectalarm.Receiver.AlarmReceiver;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.seismic.ShakeDetector;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton fabSetAlarm;
    private ListView listAlarm;
    private TextView txtEmpty;
    private SimpleCursorAdapter adapter;

    private Switch switchOnOff, test;
    private Button btnOnOff;
    private int id_test;

    private String[] from = new String[]{DatabaseHelper.COL_ID_ALARM, DatabaseHelper.COL_JAM_ALARM,
            DatabaseHelper.COL_MENIT_ALARM, DatabaseHelper.COL_SISTEM_AM_PM};
    private int[] to = new int[]{R.id.txtIdAlarm, R.id.txtJamAlarm,
            R.id.txtMenitAlarm, R.id.txtSistemAlarm};

    DataAlarm dataAlarm;
    AlarmManager alarmManager;
    SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();

        createSensor();

        dataAlarm = new DataAlarm(MainActivity.this, "read");
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        fabSetAlarm = (FloatingActionButton) findViewById(R.id.fabSetAlarm);
        listAlarm = (ListView) findViewById(R.id.listAlarm);
        txtEmpty = (TextView) findViewById(R.id.txtEmpty);

        listAlarm.setEmptyView(findViewById(R.id.txtEmpty));

        Cursor cursor = dataAlarm.readAlarmData();

        adapter = new SimpleCursorAdapter(MainActivity.this, R.layout.alarm_list_style,
                cursor, from, to ,0);
        adapter.notifyDataSetChanged();

        listAlarm.setAdapter(adapter);

        listAlarm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txtIdAlarm = view.findViewById(R.id.txtIdAlarm);

                String id_alarm = txtIdAlarm.getText().toString();

                id_test = Integer.parseInt(id_alarm);

                Intent intent_next = new Intent(MainActivity.this, UpdateAlarmActivity.class);
                intent_next.putExtra("id alarm", id_alarm);

                startActivity(intent_next);
            }
        });

    }

    private void createSensor() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        final ShakeDetector shakeDetector = new ShakeDetector(new ShakeDetector.Listener() {
            @Override
            public void hearShake() {
                Toast.makeText(MainActivity.this, "Shake Detected", Toast.LENGTH_SHORT).show();
//
//                Intent test = new Intent(MainActivity.this, AlarmReceiver.class);
//                test.putExtra("shake", "turn music off");
//                sendBroadcast(test);

                setAlarmTime(getTest(), "Off");
            }
        });

        shakeDetector.start(sensorManager);
        shakeDetector.setSensitivity(ShakeDetector.SENSITIVITY_MEDIUM);

    }

    public void setBtnAlarm(View v) {
        Intent alarm_intent = new Intent(this, AlarmActivity.class);
        startActivity(alarm_intent);

    }
//
//    public void setButtonOnOff(View view) {
//        RelativeLayout rv = (RelativeLayout) view.getParent().getParent().getParent();
//
//        btnOnOff = (Button) rv.findViewById(R.id.btnOnOff);
//
//
//    }

    public void setSwitchOnOff(View view) {
        RelativeLayout rv = (RelativeLayout) view.getParent().getParent().getParent();

        switchOnOff = (Switch) rv.findViewById(R.id.switchOnOff);

        TextView txtId = (TextView) rv.findViewById(R.id.txtIdAlarm);
        TextView txtJam = (TextView) rv.findViewById(R.id.txtJamAlarm);
        TextView txtMenit = (TextView) rv.findViewById(R.id.txtMenitAlarm);
        TextView txtAM_PM = (TextView) rv.findViewById(R.id.txtSistemAlarm);

        if(switchOnOff.isChecked()) {
            Toast.makeText(this, "turn on", Toast.LENGTH_SHORT).show();

            txtJam.setTextColor(Color.parseColor("#33658a"));
            txtMenit.setTextColor(Color.parseColor("#33658a"));
            txtAM_PM.setTextColor(Color.parseColor("#33658a"));

            int id = Integer.parseInt(txtId.getText().toString());

            setAlarmTime(id, "On");
        } else {
            Toast.makeText(this, "turn off", Toast.LENGTH_SHORT).show();

            txtJam.setTextColor(Color.parseColor("#a8a8a8"));
            txtMenit.setTextColor(Color.parseColor("#a8a8a8"));
            txtAM_PM.setTextColor(Color.parseColor("#a8a8a8"));

            int id = Integer.parseInt(txtId.getText().toString());

            setTest(id);

            setAlarmTime(id, "Off");
        }

    }

    private void setTest(int id) {
        this.id_test = id;
    }

    private int getTest() {
        return this.id_test;
    }

    public void setAlarmTime(int id, String purpose) {
        long alarm_milisecond;

        Intent my_intent = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent;

        try {
            Cursor cursor = dataAlarm.getAlarmDataById(id);

            if(cursor.getCount() > 0) {
                while(cursor.moveToNext()) {
                    String hour = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_JAM_ALARM));
                    String minute = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_MENIT_ALARM));
                    String sistem = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_SISTEM_AM_PM));
                    String ringtone = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_RINGTONE_ALARM));

                    int hour_alarm = Integer.parseInt(hour);
                    int minute_alarm = Integer.parseInt(minute);
                    int sistem_alarm = setSistemAM_PM(hour_alarm);

                    Log.e("hour:minute sistem", hour_alarm + ":" + minute_alarm + " " + sistem_alarm);

                    Calendar calendar = Calendar.getInstance();

                    calendar.set(Calendar.MINUTE, minute_alarm);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.AM_PM, sistem_alarm);

                    if(calendar.getTimeInMillis() < System.currentTimeMillis()) {
                        alarm_milisecond = calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY;
                    } else {
                        alarm_milisecond = calendar.getTimeInMillis();
                    }

                    switch (ringtone) {
                        case "Your Name" :
                            my_intent.putExtra("song","Your Name");
                            break;
                        case "Blue Water":
                            my_intent.putExtra("song","Blue Water");
                            break;
                        case "Hikari" :
                            my_intent.putExtra("song","Hikari");
                            break;
                        default:
                            //nothing
                            break;
                    }

                    if(purpose == "On") {
                        my_intent.putExtra("switch", "alarm on");

//                        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0,
//                                my_intent, PendingIntent.FLAG_UPDATE_CURRENT);

                        pendingIntent = getDistinctPendingIntent(my_intent, id);

                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarm_milisecond, pendingIntent);
                        } else {
                            alarmManager.set(AlarmManager.RTC_WAKEUP, alarm_milisecond, pendingIntent);
                        }
                    } else if(purpose == "Off") {
                        my_intent.putExtra("switch", "alarm off");

//                        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0,
//                                my_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        pendingIntent = getDistinctPendingIntent(my_intent, id);

                        alarmManager.cancel(pendingIntent);
                        //stop the ringtone
                        sendBroadcast(my_intent);
                    }

                }
            }
        } catch(Exception e) {
            Toast.makeText(MainActivity.this, "Set Alarm Error", Toast.LENGTH_SHORT).show();
        }
    }

    private int setSistemAM_PM(int hour) {
        int am_or_pm;

        if(hour == 0) {
            am_or_pm = Calendar.AM;
        } else if(hour < 12) {
            am_or_pm = Calendar.AM;
        } else if(hour == 12) {
            am_or_pm = Calendar.PM;
        } else {
            am_or_pm = Calendar.PM;
        }

        return am_or_pm;
    }

    private PendingIntent getDistinctPendingIntent(Intent intent, int requestId) {
        PendingIntent pi = PendingIntent.getBroadcast(
                MainActivity.this,
                requestId,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return pi;
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {

            NotificationChannel channelTest = new NotificationChannel("channelAlarm",
                    "Alarm",
                    NotificationManager.IMPORTANCE_HIGH);

            channelTest.setDescription("This is an alarm channel");

            try {
                NotificationManager manager = getSystemService(NotificationManager.class);

                manager.createNotificationChannel(channelTest);
            } catch(Exception e) {
                Log.e("NotificationManager value", null);
            }
        }
    }

}
