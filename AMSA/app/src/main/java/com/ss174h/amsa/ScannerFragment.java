package com.ss174h.amsa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Fragment;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class ScannerFragment extends Fragment {

    private Context context;

    public ScannerFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {

        View scannerView = layoutInflater.inflate(R.layout.fragment_scanner, viewGroup, false);
        IntentFilter intentFilter = new IntentFilter(requestReceiver.PROCESS_RESPONSE);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);

        Button scanButton = scannerView.findViewById(R.id.scan_now);

        scanButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ScannerService.class);
                getActivity().startService(intent);
            }
        });

        requestReceiver rr = new requestReceiver();
        context.registerReceiver(rr, intentFilter);

        return scannerView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public class requestReceiver extends BroadcastReceiver {

        public static final String PROCESS_RESPONSE = "com.ss174h.app_scanner.intent.action.PROCESS_RESPONSE";

        @Override
        public void onReceive(Context context, Intent intent) {

            ArrayList<String> array = intent.getStringArrayListExtra(ScannerService.RESPONSE_ARRAY);

            if(array.isEmpty()) {
                Toast.makeText(context, "No sideloaded applications installed!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Sideloaded applications installed!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
