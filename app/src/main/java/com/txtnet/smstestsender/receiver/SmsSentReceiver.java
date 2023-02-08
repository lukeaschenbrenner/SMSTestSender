package com.txtnet.smstestsender.receiver;

import static android.provider.Telephony.*;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.txtnet.smstestsender.BuildConfig;

import java.util.logging.Level;

public class SmsSentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (getResultCode()) {
            case Activity.RESULT_OK:
                Toast.makeText(context,
                        "SMS Sent: " + intent.getStringExtra("body"),
                        Toast.LENGTH_SHORT).show();

                break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                Toast.makeText(context, "SMS generic failure", Toast.LENGTH_SHORT)
                        .show();
                if (BuildConfig.DEBUG) {
                    int errorCode = intent.getIntExtra("errorCode", -1);
                    if (errorCode != -1) {
                        //MainBrowserScreen.rootLogger.log(Level.INFO, "Error code: " + String.valueOf(errorCode));
                    }else{
                        //MainBrowserScreen.rootLogger.log(Level.INFO, "Unknown Error code: -1. This should never happen!");
                    }
                }



                break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:
                Toast.makeText(context, "SMS no service", Toast.LENGTH_SHORT)
                        .show();

                break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
                Toast.makeText(context, "SMS null PDU", Toast.LENGTH_SHORT).show();
                break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
                Toast.makeText(context, "SMS radio off", Toast.LENGTH_SHORT).show();
                //TODO: ask user to turn off airplane mode
                break;
        }
    }
}