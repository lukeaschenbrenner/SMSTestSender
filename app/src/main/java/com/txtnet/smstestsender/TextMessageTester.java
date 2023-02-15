package com.txtnet.smstestsender;

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
    //public static String[] SYMBOL_TABLE = {"\n","ü","à", "@@@ìå0(5<PPsVR§¡%PY.q:hw,7?7;E\'hé94$tå<zHqy5p/POåÆ!B+A*M@£Zqòù#!ZåmbbZOHfnQ!DG#ße+ò_m9#Äö\'xpØ<EBu)hV¿Zehèk5ß17N%HxmÑCùpIXÇñR:Äungi-æU\nL,soG§g9xÅRy§¤,*#ßUÉb¥&VPj", "@£@;ÅDw(NHd2PMd:hC(mò(Çæ@JQ5b*t/ég9c@Ç6;%6-.hIBl1(Kuæ¿å¡ÄU;h=¥DLö\'$j%4l1y¤g5L+\"EM77ep¤xé¡cv¤l8qCåB7ìooQsqvHUT=*£.GHöoÖò/-nV-fUwÇ\'%N6iint\'PìF)YVW9SwoÑb)g_ò6¥8+ìU", "@$@=)öp89)/f_X¿$:fpwfö,kBaæP!5òFfe7Ç.&CAì)b¥Pk$Aa5UqròH)wsEÇ!/d/SÄjcÑß!u\"æ%ÉMJBO1Ñ9&Sß<fäprä$ààààààààààààààààààààààààààààààààààààààààààààààààààààààààààààààààààà", "", "", "", "", "", ""};
    public static ArrayList<String> SYMBOL_TABLE;
    public static ArrayList<String> messageResults = new ArrayList<>(); // ONLY STORE FAILED TESTS!!! eg. "messageResults.size()/SYMBOL.table tests passed"

    @Override
    public ArrayList<String> call() throws Exception {

        SmsManager sms = SmsManager.getDefault();
        //Handler handler = new Handler();

        ArrayList<PendingIntent> sentPendingIntents = new ArrayList<PendingIntent>();
        ArrayList<PendingIntent> deliveredPendingIntents = new ArrayList<PendingIntent>();

        Log.e("BEFORE", Boolean.toString(Thread.currentThread().isInterrupted()));

            for (int i = 0; i < SYMBOL_TABLE.size() && !Thread.currentThread().isInterrupted(); i++) { //smsQueue.size()
                Log.e("BEFORE", "BEFORE SEND");
                String msg = SYMBOL_TABLE.get(i).substring(0, 1);
//    ArrayList<String> msgArray = smsManager.divideMessage(msg);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
              //  Log.e("SEND:", SYMBOL_TABLE[i]);
                sms.sendTextMessage(PHONE_NUMBER, null, SYMBOL_TABLE.get(i), sentPendingIntents.get(i), deliveredPendingIntents.get(i));
                Thread.sleep(1000);

            }
            //try{
            //    Thread.sleep(5000);
            //}catch(InterruptedException ie){
            //    return messageResults;
           // }
        Log.e("fut", "future about to return");
         //   SmsSentReceiver.sec.update("");
        return messageResults;
    }
    
    public TextMessageTester(String phoneNumber, Context c){
        currentContext = c;
        SYMBOL_TABLE = new ArrayList<>();
        PHONE_NUMBER = phoneNumber; //= "+19133360765"
        //public static final String[] SYMBOL_TABLE = {"test message!"};
        //public static final String[] SYMBOL_TABLE = {"@","£","$","¥","è","é","ù","ì","ò","Ç","\n","Ø","ø","Å","å","_","Æ","æ","ß","É","!","\"","#","¤","%","&","'","(",")","*","+",",","-",".","/","0","1","2","3","4","5","6","7","8","9",":",";","<","=",">","?","¡","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","Ä","Ö","Ñ","Ü","\u00A7","¿","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","ä","ö","ñ","ü","à"};
       // String[] symbols = {"@", "£", "$", "¥", "è", "é", "ù", "ì", "ò", "Ç", "\n", "Ø", "ø", "Å", "å", "_", "Æ", "æ", "ß", "É", "!", "\"", "#", "¤", "%", "&", "'", "(", ")", "*", "+", ",", "-", ".", "/", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ":", ";", "<", "=", ">", "?", "¡", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "Ä", "Ö", "Ñ", "Ü", "\u00A7", "¿", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "ä", "ö", "ñ", "ü", "à"};
        String[] symbols = {"@", "£", "$", "¥", "è"};
        for(String chara : symbols){
            StringBuilder candidate = new StringBuilder();
            //candidate.setLength(160);
            for(int i = 0; i < 160; i++){
                candidate.append(chara);
            }
            SYMBOL_TABLE.add(candidate.toString()); //String.valueOf(chara.toCharArray()[0] * 160
        }
        Log.e("finalchars", String.valueOf(SYMBOL_TABLE.size()));
       // SYMBOL_TABLE[6] = SYMBOL_TABLE[3].substring(0, SYMBOL_TABLE[3].length()/2);
       // SYMBOL_TABLE[7] = SYMBOL_TABLE[3].substring(SYMBOL_TABLE[3].length()/2);

//        SYMBOL_TABLE[8] = SYMBOL_TABLE[4].substring(0, SYMBOL_TABLE[4].length()/2);
//        SYMBOL_TABLE[9] = SYMBOL_TABLE[4].substring(SYMBOL_TABLE[4].length()/2);

  //      SYMBOL_TABLE[10] = SYMBOL_TABLE[5].substring(0, SYMBOL_TABLE[5].length()/2);
    //    SYMBOL_TABLE[11] = SYMBOL_TABLE[5].substring(SYMBOL_TABLE[5].length()/2);

    }
}
