package com.ss174h.amsa.APKScanner;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;

import static android.content.pm.PackageManager.GET_CONFIGURATIONS;
import static android.content.pm.PackageManager.GET_GIDS;
import static android.content.pm.PackageManager.GET_PERMISSIONS;
import static android.content.pm.PackageManager.GET_SIGNATURES;

public class APKScannerService extends IntentService {

    private PackageManager pm;
    public static final String RESPONSE_ARRAY = "myArray";
    private static final int FLAGS = GET_GIDS | GET_CONFIGURATIONS | GET_PERMISSIONS | GET_SIGNATURES;
    private ArrayList<String> packages = new ArrayList<>();

    public APKScannerService() {
        super("APKScannerService");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.pm = super.getPackageManager();
    }

    @Override
    protected void onHandleIntent(Intent in) {
        String pName = in.getStringExtra("pName");

        if(pName != null) {
            try {
                PackageInfo packageInfo = this.pm.getPackageInfo(pName, FLAGS);
                this.scan(packageInfo);
            } catch (PackageManager.NameNotFoundException e) {
                Log.wtf("APKScannerService", "No such package: " + pName);
            }
        } else {
            for (PackageInfo packageInfo : this.pm.getInstalledPackages(FLAGS)) {
                this.scan(packageInfo);
            }
        }

        Intent intent = new Intent();
        intent.setAction(APKScannerFragment.myReceiver.PROCESS_RESPONSE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putStringArrayListExtra(RESPONSE_ARRAY,packages);
        sendBroadcast(intent);
    }

    private void scan(PackageInfo packageInfo) {
        if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            if (!this.fromGooglePlay(packageInfo.packageName) && !isAMSA(packageInfo.packageName)) {
                packages.add(packageInfo.packageName);
            }
        }
    }

    private boolean fromGooglePlay(String package_name) {
        String install_package_manager = this.pm.getInstallerPackageName(package_name);

        if (install_package_manager == null) return false;

        else if (install_package_manager.equals("com.google.android.feedback") || install_package_manager.equals("com.android.vending")) {
            return true;
        }

        return false;
    }

    private boolean isAMSA(String package_name) {
        if (package_name.equals("com.ss174h.amsa")) return true;
        return false;
    }
}
