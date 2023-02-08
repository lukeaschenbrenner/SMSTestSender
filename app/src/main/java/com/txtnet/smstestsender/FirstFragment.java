package com.txtnet.smstestsender;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.txtnet.smstestsender.databinding.FragmentFirstBinding;
import com.txtnet.smstestsender.receiver.SmsDeliveredReceiver;
import com.txtnet.smstestsender.receiver.SmsSentReceiver;

import java.util.ArrayList;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }
    public final String PHONE_NUMBER = "+19133360765";
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.permsBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UnspecifiedImmutableFlag")
            @Override
            public void onClick(View view) {


                SmsManager sms = SmsManager.getDefault();
                Handler handler = new Handler();
                ArrayList<PendingIntent> sentPendingIntents = new ArrayList<PendingIntent>();
                ArrayList<PendingIntent> deliveredPendingIntents = new ArrayList<PendingIntent>();


                for (int i = 0; i < 2; i++) { //smsQueue.size()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        sentPendingIntents.add(i, PendingIntent.getBroadcast(view.getContext(), 0,
                                new Intent(view.getContext(), SmsSentReceiver.class).putExtra("body", "send success"), PendingIntent.FLAG_IMMUTABLE));
                        deliveredPendingIntents.add(i, PendingIntent.getBroadcast(view.getContext(), 0,
                                new Intent(view.getContext(), SmsDeliveredReceiver.class), PendingIntent.FLAG_IMMUTABLE));
                    }else{
                        sentPendingIntents.add(i, PendingIntent.getBroadcast(view.getContext(), 0,
                                new Intent(view.getContext(), SmsSentReceiver.class).putExtra("body", "send success"), PendingIntent.FLAG_UPDATE_CURRENT));
                        deliveredPendingIntents.add(i, PendingIntent.getBroadcast(view.getContext(), 0,
                                new Intent(view.getContext(), SmsDeliveredReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT));
                    }


                }
                handler.postDelayed(new Runnable() {
                    public void run() {
                        sms.sendTextMessage(PHONE_NUMBER, null, "test message", sentPendingIntents.get(0), deliveredPendingIntents.get(0));
                    }
                }, 1000L * 1); //i
                handler.postDelayed(new Runnable() {
                    public void run() {
                        sms.sendTextMessage(PHONE_NUMBER, null, "test message", sentPendingIntents.get(0), deliveredPendingIntents.get(0));
                    }
                }, 1000L * 2); //i

                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}