package com.example.projecta;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * BroadCastReceiver is a class made for the odd option of which the player is trying to turn off the screen
 * while the game is still running in the background
 */

public class ScreenReceiver extends BroadcastReceiver {

    public static boolean wasScreenOn = true;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            // do whatever you need to do here

            wasScreenOn = false;
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            // and do whatever you need to do here
            wasScreenOn = true;
        }
    }

}
