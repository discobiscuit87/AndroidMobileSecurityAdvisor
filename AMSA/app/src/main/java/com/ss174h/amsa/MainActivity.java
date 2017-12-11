package com.ss174h.amsa;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button b1, b2, b3, b4, b5, b6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = findViewById(R.id.scanApps);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScannerFragment fragment = new ScannerFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.container,fragment,"Scanner");
                fragmentTransaction.commit();
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
                Toast.makeText(MainActivity.this,"Viewing app permissions",Toast.LENGTH_LONG).show();
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
            }
        });
    }

}
