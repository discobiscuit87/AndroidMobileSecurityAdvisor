package com.ss174h.amsa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ss174h.amsa.APKScanner.APKScannerService;
import com.ss174h.amsa.EnvCondition.EnvironmentInfoFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button b1, b2, b3, b4, b5, b6;
    public static final String PROCESS_RESPONSE = "com.ss174h.app_scanner.intent.action.PROCESS_RESPONSE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = findViewById(R.id.scanApps);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentFilter intentFilter = new IntentFilter(PROCESS_RESPONSE);
                intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
                Intent intent = new Intent(v.getContext(), APKScannerService.class);
                v.getContext().startService(intent);
                APKReceiver receiver = new APKReceiver();
                v.getContext().registerReceiver(receiver, intentFilter);
            }
        });

        b2 = findViewById(R.id.scanMalware);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Scanning for malware",Toast.LENGTH_LONG).show();
            }
        });

        b3 = findViewById(R.id.viewPermissions);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentFilter intentFilter = new IntentFilter(PROCESS_RESPONSE);
                intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
                Intent intent = new Intent(v.getContext(), APKScannerService.class);
                v.getContext().startService(intent);
                PermissionsReceiver receiver = new PermissionsReceiver();
                v.getContext().registerReceiver(receiver, intentFilter);
            }
        });

        b4 = findViewById(R.id.monitorBehaviour);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Monitoring app behaviour",Toast.LENGTH_LONG).show();
            }
        });

        b5 = findViewById(R.id.monitorTraffic);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Monitoring app traffic",Toast.LENGTH_LONG).show();
            }
        });

        b6 = findViewById(R.id.reviewEnvironment);
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Reviewing mobile environment",Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), EnvironmentInfoFragment.class);

                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    public class APKReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            ArrayList<String> array = intent.getStringArrayListExtra("array");

            if(array.isEmpty()) {
                Toast.makeText(context, "No sideloaded applications installed!", Toast.LENGTH_LONG).show();
            } else {
                Intent appIntent = new Intent(context, ViewAppsActivity.class);
                appIntent.putStringArrayListExtra("array",array);
                appIntent.putExtra("Check","CheckCerts");
                context.startActivity(appIntent);
            }
        }
    }

    public class PermissionsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            ArrayList<String> array = intent.getStringArrayListExtra("array");

            if(array.isEmpty()) {
                Toast.makeText(context, "No sideloaded applications installed!", Toast.LENGTH_LONG).show();
            } else {
                Intent appIntent = new Intent(context, ViewAppsActivity.class);
                appIntent.putStringArrayListExtra("array",array);
                appIntent.putExtra("Check","CheckPerms");
                context.startActivity(appIntent);
            }
        }
    }
}
