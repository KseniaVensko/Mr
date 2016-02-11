package com.example.kseniavensko.mr;
//TODO: adb shell - android debugger, am broadcast -a [package].HELLO -es name juraj - activity manager
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DonkeyReceiver extends BroadcastReceiver {
    private static final String TAG = "Donkey Receiver";

    public DonkeyReceiver() {
        Log.i(TAG, "DonkeyReceiver was just created");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.i(TAG, "Message: " + intent.getStringExtra("message"));
    }
}
