package com.txtnet.smstestsender;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.txtnet.smstestsender.databinding.FragmentSecondBinding;
import com.txtnet.smstestsender.receiver.SmsSentReceiver;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private Future<ArrayList<String>> future;
    private View view;
    //String phoneNumber;

    public void onStart() {
        super.onStart();

        ExecutorService executor = Executors.newFixedThreadPool(3);
        Callable<ArrayList<String>> textMessageTester = new TextMessageTester(FirstFragment.PHONE_NUMBER, view.getContext());
        future = executor.submit(textMessageTester);

        TextView tv1 = (TextView)view.findViewById(R.id.textview_second);
        int i = 1;
        int j = 2;
        //while(!future.isDone()){
        //    StringBuilder periods = new StringBuilder();
        //    while(i % j > 0) {
        //        periods.append(".");
        //        i++;
        //    }
        //    j = 2 + (j+1)%5;
        //    i = 1;
        tv1.setText("Currently sending messages... Please wait");
       // tv1.setText("Currently sending messages" + periods.toString());
        //}

    }
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        SmsSentReceiver.setSF(this);
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // CANCEL EXECUTOR NOWWWWWWWWWWWWWWWWW
                future.cancel(true);
              //  NavHostFragment.findNavController(SecondFragment.this)
              //          .navigate(R.id.action_SecondFragment_to_FirstFragment);
                Toast.makeText(getActivity(), "Cancelled all pending messages.",
                        Toast.LENGTH_LONG).show();
            }
        });
        this.view = view;


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    ArrayList<String> ars = new ArrayList<>();
    public void update(String str){
        TextView tv1 = (TextView)view.findViewById(R.id.textview_second);

        ArrayList<String> failedOnes = null;
        try {

            failedOnes = future.get(1000, TimeUnit.MILLISECONDS);
            Log.e("FUT", "future obtained.");
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            ars.add(str);
            failedOnes = ars;
        }
        if(failedOnes == null){
            tv1.setText("ERROR: Tests failed to complete.");
        }
        else{
            StringBuilder finalText = new StringBuilder();
            finalText.append("Failed characters: ");
            if(failedOnes.size() == 0){
                finalText.append("NONE (so far.)");
            }
            for(String failedchar : failedOnes){
                if(failedchar.equals("\n")){
                    finalText.append("newline, ");
                }else{
                    finalText.append(failedchar + ", ");
                }
            }
            tv1.setText(finalText.toString());
        }
    }

}