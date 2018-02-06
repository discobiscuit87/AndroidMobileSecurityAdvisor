package com.ss174h.amsa;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ss174h.amsa.APKScanner.APKScannerService;
import com.ss174h.amsa.DetectNewAPK.MyFileObserver;
import com.ss174h.amsa.MalwareScanner.ScanMalwareActivity;
import com.ss174h.amsa.MonitorBehaviour.LogRemoteIPIntentService;
import com.ss174h.amsa.MonitorBehaviour.MonitorActivity;
import com.ss174h.amsa.EnvCondition.ReviewEnv;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button b1, b2, b3, b4, b5;
    public static final String PROCESS_RESPONSE = "com.ss174h.amsa.intent.action.PROCESS_RESPONSE";
    private ArrayList<String> array;
    private APKReceiver apkReceiver;
    private SDReceiver sdReceiver;

    //This path contains all the files downloaded by user
    private String downloadPath = "/sdcard/Download";

    MyFileObserver fileOb = new MyFileObserver(downloadPath,MainActivity.this);

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.GET_ACCOUNTS_PRIVILEGED
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter intentFilter = new IntentFilter(PROCESS_RESPONSE);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        Intent intent = new Intent(this, APKScannerService.class);
        startService(intent);
        apkReceiver = new APKReceiver();
        registerReceiver(apkReceiver, intentFilter);
        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addCategory(Intent.ACTION_MEDIA_MOUNTED);
        sdReceiver = new SDReceiver();
        registerReceiver(sdReceiver,intentFilter1);

        //Create file descriptor
        verifyStoragePermissions(this);

        File storageDir = new File(downloadPath);

        File listAllFiles[] = storageDir.listFiles();

        Log.d("isDirectory", "true");
        Log.d("App can access",storageDir.canRead()+"");
        Log.d("Path name",storageDir.getPath());
        Log.d("isExtStorageAvail", Environment.getExternalStorageState());



        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            for(int i=0;i<listAllFiles.length;i++)
            {
                Log.i("Download Directory File",listAllFiles[i].getName());
            }
        }

        fileOb.startWatching();

        b1 = (Button) findViewById(R.id.scanApps);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appIntent = new Intent(v.getContext(), ViewAppsActivity.class);
                appIntent.putStringArrayListExtra("array",array);
                appIntent.putExtra("Check","CheckCerts");
                v.getContext().startActivity(appIntent);
            }
        });

        b2 = (Button) findViewById(R.id.scanMalware);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appIntent = new Intent(v.getContext(), ScanMalwareActivity.class);
                appIntent.putStringArrayListExtra("array",array);
                v.getContext().startActivity(appIntent);
            }
        });

        b3 = (Button) findViewById(R.id.viewPermissions);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appIntent = new Intent(v.getContext(), ViewAppsActivity.class);
                appIntent.putStringArrayListExtra("array",array);
                appIntent.putExtra("Check","CheckPerms");
                v.getContext().startActivity(appIntent);
            }
        });

        b4 = (Button) findViewById(R.id.monitorBehaviour);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MonitorActivity.class);
                
                startActivity(intent);
            }
        });

        b5 = (Button) findViewById(R.id.reviewEnvironment);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Reviewing mobile environment",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), ReviewEnv.class);

                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(apkReceiver);
        unregisterReceiver(sdReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(PROCESS_RESPONSE);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(apkReceiver, intentFilter);
        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addCategory(Intent.ACTION_MEDIA_MOUNTED);
        registerReceiver(sdReceiver,intentFilter1);
        verifyStoragePermissions(this);

        File storageDir = new File("/sdcard/Download");


        File listAllFiles[] = storageDir.listFiles();

        Log.d("isDirectory", "true");
        Log.d("App can access",storageDir.canRead()+"");
        Log.d("Path name",storageDir.getPath());
        Log.d("isExtStorageAvail", Environment.getExternalStorageState());

        //listAllFiles.
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            for(int i=0;i<listAllFiles.length;i++)
            {
                Log.i("Download Directory File",listAllFiles[i].getName());
            }
        }
        fileOb.startWatching();
    }

    //To receive the populated arrayList from the scanner service
    public class APKReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            array = intent.getStringArrayListExtra("array");

            if(array.isEmpty()) {
                Toast.makeText(context, "No sideloaded applications installed!", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class SDReceiver extends BroadcastReceiver {

        String name;

        @Override
        public void onReceive(Context context, Intent intent) {
            File sdCardRoot = Environment.getExternalStorageDirectory();
            File yourDir = new File(sdCardRoot, "/Download");
            for (File f : yourDir.listFiles()) {
                if (f.isFile())
                    name = f.getName();
                    Log.e("Name", name);
            }
        }
    }

    //For application to get access to all apk files in download directory
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
