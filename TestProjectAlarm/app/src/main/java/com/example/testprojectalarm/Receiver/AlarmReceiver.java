package com.example.testprojectalarm.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.testprojectalarm.Service.RingtonePlayingService;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("in the receiver", "you made it !");

        String extraSwitch = intent.getStringExtra("switch");
        String extraSong = intent.getStringExtra("song");

        Log.e("extraSwitch value", extraSwitch);
        Log.e("extraSong value", extraSong);

        Intent intent_service = new Intent(context, RingtonePlayingService.class);

        intent_service.putExtra("switch", extraSwitch);
        intent_service.putExtra("song", extraSong);

        context.startService(intent_service);
    }
}
