package com.ss174h.amsa;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ss174h.amsa.APKScanner.APKScannerService;
import com.ss174h.amsa.DetectNewAPK.MyFileObserver;
import com.ss174h.amsa.MalwareScanner.ScanMalwareActivity;
import com.ss174h.amsa.RealTime.PackageHandler;
import com.ss174h.amsa.MonitorBehaviour.MonitorActivity;
import com.ss174h.amsa.EnvCondition.ReviewEnv;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button b1, b2, b3, b4, b5;
    public static final String PROCESS_RESPONSE = "com.ss174h.amsa.intent.action.PROCESS_RESPONSE";
    private ArrayList<String> array;
    private APKReceiver apkReceiver;
    private PermissionsReceiver permissionsReceiver;
    private MalwareReceiver malwareReceiver;

    BroadcastReceiver mExternalStorageReceiver;
    boolean mExternalStorageAvailable = false;
    boolean mExternalStorageWriteable = false;

    //This path contains all the files downloaded by user
    private String downloadPath ="/sdcard/Download";

    MyFileObserver fileOb = new MyFileObserver("/sdcard/Download");

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.GET_ACCOUNTS_PRIVILEGED
    };

    //Add code launch service
    //Intent msgIntent = new Intent(this, MonitorDownloadFolder.class);

    void updateExternalStorageState() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
    }

    void startWatchingExternalStorage() {
        mExternalStorageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("test", "Storage: " + intent.getData());
                updateExternalStorageState();
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_REMOVED);
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);

        registerReceiver(mExternalStorageReceiver, filter);
        updateExternalStorageState();

    }

    void stopWatchingExternalStorage() {
        unregisterReceiver(mExternalStorageReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        PackageHandler packageHandler = new PackageHandler();
        registerReceiver(packageHandler, filter);

        //get all apk files
        /*
        PackageManager pm=getPackageManager();
        List<String > pkg_path = new ArrayList<String>();
        List<PackageInfo> pkginfo_list = pm.getInstalledPackages(PackageManager.GET_ACTIVITIES);
        List<ApplicationInfo> appinfo_list = pm.getInstalledApplications(0);

        for (int x=0; x < pkginfo_list.size(); x++)
        {
            PackageInfo pkginfo = pkginfo_list.get(x);
            pkg_path.add(appinfo_list.get(x).publicSourceDir);  //store package path in array

            Log.i("package path = " , pkg_path.get(x) + "");
            File tx = new File(pkg_path.get(x)+"");

            //Log.i("Application info",pkginfo_list.get(x).applicationInfo + "");
            Log.i("total space taken up",tx.getTotalSpace() + "");

        }
        */

        //File listAllFiles[] = downloadDir.listFiles();
        //File testF = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);


        /*uncomment later

        fileOb.startWatching();
        //Create file descriptor
        File storageDir = new File("/sdcard/Download");


        File listAllFiles[] = storageDir.listFiles();

        Log.d("isDirectory", "true");
        Log.d("App can access",storageDir.canRead()+"");
        Log.d("Path name",storageDir.getPath());
        Log.d("isExtStorageAvail", Environment.getExternalStorageState());

        verifyStoragePermissions(this);

        //listAllFiles.

        for(int i=0;i<listAllFiles.length;i++)
        {
            Log.i("Download Directory File",listAllFiles[i].getName());
        }

        */

        //bytesIntoHumanReadable

        b1 = findViewById(R.id.scanApps);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentFilter intentFilter = new IntentFilter(PROCESS_RESPONSE);
                intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
                Intent intent = new Intent(v.getContext(), APKScannerService.class);
                v.getContext().startService(intent);
                apkReceiver = new APKReceiver();
                v.getContext().registerReceiver(apkReceiver, intentFilter);
            }
        });

        b2 = findViewById(R.id.scanMalware);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentFilter intentFilter = new IntentFilter(PROCESS_RESPONSE);
                intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
                Intent intent = new Intent(v.getContext(), APKScannerService.class);
                v.getContext().startService(intent);
                malwareReceiver = new MalwareReceiver();
                v.getContext().registerReceiver(malwareReceiver, intentFilter);
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
                permissionsReceiver = new PermissionsReceiver();
                v.getContext().registerReceiver(permissionsReceiver, intentFilter);
            }
        });

        b4 = findViewById(R.id.monitorBehaviour);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MonitorActivity.class);
                startActivityForResult(intent,0);
                startActivity(intent);
            }
        });

        b5 = findViewById(R.id.reviewEnvironment);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Reviewing mobile environment",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), ReviewEnv.class);
                startActivityForResult(intent,0);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public class APKReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            array = intent.getStringArrayListExtra("array");

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

            array = intent.getStringArrayListExtra("array");

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

    public class MalwareReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            array = intent.getStringArrayListExtra("array");

            if(array.isEmpty()) {
                Toast.makeText(context, "No sideloaded applications installed!", Toast.LENGTH_LONG).show();
            } else {
                Intent appIntent = new Intent(context, ScanMalwareActivity.class);
                appIntent.putStringArrayListExtra("array",array);
                context.startActivity(appIntent);
            }
        }
    }
}
