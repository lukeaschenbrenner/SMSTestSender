package com.txtnet.smstestsender.receiver;

import static android.provider.Telephony.*;

import android.annotation.SuppressLint;
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

    @SuppressLint("StaticFieldLeak")
    public static SecondFragment sec;
    private static int currentNum = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        currentNum++;
        Toast.makeText(context, "text: " + intent.getStringExtra("msg"), Toast.LENGTH_SHORT)
                .show();
        SecondFragment.finalResults = SecondFragment.finalResults + " [character: " + intent.getStringExtra("msg") + " result_code: " + getResultCode() + " supplied_error_code_or_-1: " + intent.getIntExtra("errorCode", -1) + "]\n";

        switch (getResultCode()) {

            case Activity.RESULT_OK:
                Log.e("e", intent.getStringExtra("msg"));
                break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
         //   case Activity.RESULT_OK:
                TextMessageTester.messageResults.add(intent.getStringExtra("msg"));
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
                break;
        }
        if(getResultCode() != Activity.RESULT_OK){
            sec.update(intent.getStringExtra("msg"));
        }
        //sec.update();
        Log.i("currentNum", String.valueOf(currentNum));
        Log.i("Tablesize", String.valueOf(TextMessageTester.SYMBOL_TABLE.size()));
        if(currentNum >= TextMessageTester.SYMBOL_TABLE.size()){
            sec.finish();
        }
    }
    public static void setSF(SecondFragment sf){
        sec = sf;
    }
}