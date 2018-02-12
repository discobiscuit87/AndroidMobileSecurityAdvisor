package com.ss174h.amsa;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
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
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);

        IntentFilter intentFilter = new IntentFilter(PROCESS_RESPONSE);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        Intent intent = new Intent(this, APKScannerService.class);
        startService(intent);
        apkReceiver = new APKReceiver();
        registerReceiver(apkReceiver, intentFilter);

        //Create file descriptor
        verifyStoragePermissions(this);

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }

        if (!isAccessGranted()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("This app uses other app to work. Click go to settings and enable " +
                    "AMSA in the \"App Usage Permission\"")
                    .setTitle("Permission Required")
                    .setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                            startActivity(intent);
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.show();
        }

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
                if(!array.isEmpty()) {
                    Intent appIntent = new Intent(v.getContext(), ViewAppsActivity.class);
                    appIntent.putStringArrayListExtra("array",array);
                    appIntent.putExtra("Check","CheckCerts");
                    v.getContext().startActivity(appIntent);
                }
            }
        });

        b2 = (Button) findViewById(R.id.scanMalware);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!array.isEmpty()) {
                    Intent appIntent = new Intent(v.getContext(), ScanMalwareActivity.class);
                    appIntent.putStringArrayListExtra("array",array);
                    v.getContext().startActivity(appIntent);
                }
            }
        });

        b3 = (Button) findViewById(R.id.viewPermissions);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!array.isEmpty()) {
                    Intent appIntent = new Intent(v.getContext(), ViewAppsActivity.class);
                    appIntent.putStringArrayListExtra("array",array);
                    appIntent.putExtra("Check","CheckPerms");
                    v.getContext().startActivity(appIntent);
                }
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
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(PROCESS_RESPONSE);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        Intent intent = new Intent(this, APKScannerService.class);
        startService(intent);
        registerReceiver(apkReceiver, intentFilter);
        verifyStoragePermissions(this);

        File storageDir = new File("/sdcard/Download");

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }

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


    private boolean isAccessGranted() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = 0;
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        applicationInfo.uid, applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                }
                break;

            default:
                break;
        }
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
