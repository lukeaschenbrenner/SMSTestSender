package com.txtnet.smstestsender;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;

import com.txtnet.smstestsender.receiver.SmsDeliveredReceiver;
import com.txtnet.smstestsender.receiver.SmsSentReceiver;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class TextMessageTester implements Callable<ArrayList<String>> {
    
    Context currentContext;
    public final String PHONE_NUMBER;
    public static ArrayList<String> SYMBOL_TABLE;
    public static ArrayList<String> messageResults = new ArrayList<>(); // ONLY STORE FAILED TESTS!!! eg. "messageResults.size()/SYMBOL.table tests passed"

    @SuppressLint("UnspecifiedImmutableFlag")
    @Override
    public ArrayList<String> call() throws Exception {

        SmsManager sms = SmsManager.getDefault();
        //Handler handler = new Handler();

        ArrayList<PendingIntent> sentPendingIntents = new ArrayList<PendingIntent>();
        ArrayList<PendingIntent> deliveredPendingIntents = new ArrayList<PendingIntent>();


            for (int i = 0; i < SYMBOL_TABLE.size() && !Thread.currentThread().isInterrupted(); i++) { //smsQueue.size()
                String msg = SYMBOL_TABLE.get(i).substring(0, 1);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    sentPendingIntents.add(PendingIntent.getBroadcast(currentContext, i,
                            new Intent(currentContext, SmsSentReceiver.class).putExtra("msg", msg.toString()), PendingIntent.FLAG_MUTABLE));
                    deliveredPendingIntents.add(PendingIntent.getBroadcast(currentContext, i,
                            new Intent(currentContext, SmsDeliveredReceiver.class), PendingIntent.FLAG_MUTABLE));
                }else{
                    sentPendingIntents.add(PendingIntent.getBroadcast(currentContext, i,
                            new Intent(currentContext, SmsSentReceiver.class).putExtra("msg", msg.toString()), PendingIntent.FLAG_UPDATE_CURRENT));
                    deliveredPendingIntents.add(PendingIntent.getBroadcast(currentContext, i,
                            new Intent(currentContext, SmsDeliveredReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT));
                }
                sms.sendTextMessage(PHONE_NUMBER, null, SYMBOL_TABLE.get(i), sentPendingIntents.get(i), deliveredPendingIntents.get(i));
                Thread.sleep(1000);

            }

            //try{
            //    Thread.sleep(5000);
            //}catch(InterruptedException ie){
            //    return messageResults;
           // }
         //   SmsSentReceiver.sec.update("");
        return messageResults;
    }
    
    public TextMessageTester(String phoneNumber, Context c){
        currentContext = c;
        SYMBOL_TABLE = new ArrayList<>();
        PHONE_NUMBER = phoneNumber;
        //public static final String[] SYMBOL_TABLE = {"test message!"};
        //public static final String[] SYMBOL_TABLE = {"@","£","$","¥","è","é","ù","ì","ò","Ç","\n","Ø","ø","Å","å","_","Æ","æ","ß","É","!","\"","#","¤","%","&","'","(",")","*","+",",","-",".","/","0","1","2","3","4","5","6","7","8","9",":",";","<","=",">","?","¡","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","Ä","Ö","Ñ","Ü","\u00A7","¿","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","ä","ö","ñ","ü","à"};
       // String[] symbols = {"@", "£", "$", "¥", "è", "é", "ù", "ì", "ò", "Ç", "\n", "Ø", "ø", "Å", "å", "_", "Æ", "æ", "ß", "É", "!", "\"", "#", "¤", "%", "&", "'", "(", ")", "*", "+", ",", "-", ".", "/", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ":", ";", "<", "=", ">", "?", "¡", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "Ä", "Ö", "Ñ", "Ü", "\u00A7", "¿", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "ä", "ö", "ñ", "ü", "à"};
        String[] symbols = {"@", "£", "$", "¥", "è", "Γ", "Λ", "Ω", "Π", "Ψ", "Σ", "Θ", "Ξ", "Φ"};
        for(String chara : symbols){
            StringBuilder candidate = new StringBuilder();
            for(int i = 0; i < 160; i++){
                candidate.append(chara);
            }
            SYMBOL_TABLE.add(candidate.toString()); //String.valueOf(chara.toCharArray()[0] * 160
        }
    }
}
