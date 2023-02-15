package com.txtnet.smstestsender.receiver;

import static android.provider.Telephony.*;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.txtnet.smstestsender.BuildConfig;
import com.txtnet.smstestsender.SecondFragment;
import com.txtnet.smstestsender.TextMessageTester;

import java.util.ArrayList;
import java.util.logging.Level;

public class SmsSentReceiver extends BroadcastReceiver {

    static ArrayList<String> failedMessages = new ArrayList<>();
    public static SecondFragment sec;
    private static final int NUM_MESSAGES = 114;
    private int currentNum = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "text: " + intent.getStringExtra("msg"), Toast.LENGTH_SHORT)
                .show();

        switch (getResultCode()) {

            case Activity.RESULT_OK:
                Log.e("e", intent.getStringExtra("msg"));
                break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
         //   case Activity.RESULT_OK:
                failedMessages.add(intent.getStringExtra("msg"));
                TextMessageTester.messageResults.add(intent.getStringExtra("msg"));
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
        if(getResultCode() != Activity.RESULT_OK){
            sec.update(intent.getStringExtra("msg"));
        }
        //sec.update();

    }
    public ArrayList<String> getFailedMessages(){
        return failedMessages;
    }
    public static void setSF(SecondFragment sf){
        sec = sf;
    }
}