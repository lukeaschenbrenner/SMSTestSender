package com.txtnet.smstestsender;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    public static String finalResults = "";
    private static boolean alreadyCreated = false;
    @SuppressLint("SetTextI18n")
    public void onStart() {
        super.onStart();
        if(alreadyCreated){
            return;
        }
        ExecutorService executor = Executors.newFixedThreadPool(3);
        Callable<ArrayList<String>> textMessageTester = new TextMessageTester(FirstFragment.PHONE_NUMBER, view.getContext());
        future = executor.submit(textMessageTester);

        TextView tv1 = (TextView)view.findViewById(R.id.textview_second);

        tv1.setText("Currently sending " + TextMessageTester.SYMBOL_TABLE.size() +" messages... Please wait");
        alreadyCreated = true;

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
                future.cancel(true);
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
    @SuppressLint("SetTextI18n")
    public void update(String str){
        TextView tv1 = (TextView)view.findViewById(R.id.textview_second);

        ArrayList<String> failedOnes = null;
        try {

            failedOnes = future.get(1000, TimeUnit.MILLISECONDS);
        } catch (ExecutionException | InterruptedException e) {
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
            finalText.append("Sending " + TextMessageTester.SYMBOL_TABLE.size() +" SMS messages. \nFailed characters: ");
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
    public void finish(){
        //Button btn = (Button) this.view.findViewById(R.id.copy_results);
        TextView tv1 = (TextView)view.findViewById(R.id.textview_second);
        String text = tv1.getText().toString();
        tv1.setText(getString(R.string.finalText, text, getOSBuildNumber()));

        binding.copyResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) view.getContext().getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", finalResults);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity(), "Text copied!",
                        Toast.LENGTH_LONG).show();
            }
        });
        binding.copyResults.setVisibility(View.VISIBLE);
    }

    /**
     * This method returns Build Number of the device from the OS Build fingerprint
     * @return osBuildNumber - human entered name of the device
     */
    public static String getOSBuildNumber() {
        String osBuildNumber = Build.FINGERPRINT;  //"google/shamu/shamu:5.1.1/LMY48Y/2364368:user/release-keys”
        final String forwardSlash = "/";
        String osReleaseVersion = Build.VERSION.RELEASE + forwardSlash;
        try {
            osBuildNumber = osBuildNumber.substring(osBuildNumber.indexOf(osReleaseVersion));  //"5.1.1/LMY48Y/2364368:user/release-keys”
            osBuildNumber = osBuildNumber.replace(osReleaseVersion, "");  //"LMY48Y/2364368:user/release-keys”
            osBuildNumber = osBuildNumber.substring(0, osBuildNumber.indexOf(forwardSlash)); //"LMY48Y"
        } catch (Exception e) {
            Log.e("getOSBuildNumber", "Exception while parsing - " + e.getMessage());
        }

        return osBuildNumber;
    }

}