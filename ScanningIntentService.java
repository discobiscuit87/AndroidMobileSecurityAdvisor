/*
 * Copyright (C) 2016 AMSA Android Mobile Security Advisor
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.fyp.ss163g.android.application.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Log;

import com.fyp.ss163g.android.application.fragments.ScanningFragment;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import static android.content.pm.PackageManager.GET_CONFIGURATIONS;
import static android.content.pm.PackageManager.GET_GIDS;
import static android.content.pm.PackageManager.GET_PERMISSIONS;
import static android.content.pm.PackageManager.GET_SIGNATURES;

public class ScanningIntentService extends IntentService {

    public static final String RESPONSE_ARRAY = "myArray";
    private static final String TAG = "ScanningIntentService";
    private static final int FLAGS = GET_GIDS | GET_CONFIGURATIONS | GET_PERMISSIONS
            | GET_SIGNATURES;
    CertificateFactory cf;
    private PackageManager packageManager;
    private ArrayList<String> test = new ArrayList<>();

    public ScanningIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.packageManager = super.getPackageManager();
        try {
            cf = CertificateFactory.getInstance("X509");
        } catch (CertificateException e) {
            Log.wtf(TAG, "Failed to get X509 certificate factory");
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String packageName = intent.getStringExtra("packageName");

        if (packageName != null) {
            try {
                PackageInfo packageInfo = this.packageManager.getPackageInfo(packageName, FLAGS);
                this.scan(packageInfo);
            } catch (PackageManager.NameNotFoundException e) {
                Log.wtf(TAG, "No such package: " + packageName);
            }
        } else {
            for (PackageInfo packageInfo : this.packageManager.getInstalledPackages(FLAGS)) {
                this.scan(packageInfo);
            }
        }

        Intent broadcast = new Intent();
        broadcast.setAction(ScanningFragment.myRequestReciever.PROCESS_RESPOSE);
        broadcast.addCategory(Intent.CATEGORY_DEFAULT);
        broadcast.putStringArrayListExtra(RESPONSE_ARRAY, test);
        sendBroadcast(broadcast);
    }

    private void scan(PackageInfo packageInfo) {
        if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            if (this.isMalware(packageInfo) && !this.isFromGooglePlay(packageInfo.packageName)
                    && !isOwnApp(packageInfo.packageName)) {
                test.add(packageInfo.packageName.toString());
            }
        }
    }

    private boolean isMalware(PackageInfo packageInfo) {
        return this.isMalwareByUsedPermissions(packageInfo.requestedPermissions)
                || this.isMalwareBySignatures(packageInfo.signatures);
    }

    private boolean isMalwareByUsedPermissions(String[] usedPermissions) {
        if (usedPermissions != null) {
            for (String usedPermission : usedPermissions) {
                if (android.Manifest.permission.READ_CALENDAR.equals(usedPermission)
                        | android.Manifest.permission.WRITE_CALENDAR.equals(usedPermission)
                        | android.Manifest.permission.CAMERA.equals(usedPermission)
                        | android.Manifest.permission.READ_CONTACTS.equals(usedPermission)
                        | android.Manifest.permission.WRITE_CONTACTS.equals(usedPermission)
                        | android.Manifest.permission.GET_ACCOUNTS.equals(usedPermission)
                        | android.Manifest.permission.ACCESS_FINE_LOCATION.equals(usedPermission)
                        | android.Manifest.permission.ACCESS_COARSE_LOCATION.equals(usedPermission)
                        | android.Manifest.permission.RECORD_AUDIO.equals(usedPermission)
                        | android.Manifest.permission.READ_PHONE_STATE.equals(usedPermission)
                        | android.Manifest.permission.CALL_PHONE.equals(usedPermission)
                        | android.Manifest.permission.READ_CALL_LOG.equals(usedPermission)
                        | android.Manifest.permission.WRITE_CALL_LOG.equals(usedPermission)
                        | android.Manifest.permission.ADD_VOICEMAIL.equals(usedPermission)
                        | android.Manifest.permission.USE_SIP.equals(usedPermission)
                        | android.Manifest.permission.PROCESS_OUTGOING_CALLS.equals(usedPermission)
                        | android.Manifest.permission.BODY_SENSORS.equals(usedPermission)
                        | android.Manifest.permission.SEND_SMS.equals(usedPermission)
                        | android.Manifest.permission.RECEIVE_SMS.equals(usedPermission)
                        | android.Manifest.permission.READ_SMS.equals(usedPermission)
                        | android.Manifest.permission.RECEIVE_WAP_PUSH.equals(usedPermission)
                        | android.Manifest.permission.RECEIVE_MMS.equals(usedPermission)
                        | android.Manifest.permission.READ_EXTERNAL_STORAGE.equals(usedPermission)
                        | android.Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(usedPermission)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isMalwareBySignatures(Signature[] signatures) {
        if (signatures != null) {
            for (Signature signature : signatures) {
                try {
                    X509Certificate cert = toX509Certificate(signature);

                    try {
                        cert.checkValidity();
                    } catch (CertificateExpiredException e) {
                        return true;
                    } catch (CertificateNotYetValidException e) {
                        return true;
                    }

                    if ("CN=Android Debugger,O=Android,C=US".equals(cert.getIssuerDN().getName())) {
                        return true;
                    }
                } catch (CertificateException e) {
                    return true;
                }
            }
        }
        return false;
    }

    private X509Certificate toX509Certificate(Signature signature) throws CertificateException {
        byte[] cert = signature.toByteArray();
        InputStream input = new ByteArrayInputStream(cert);
        return (X509Certificate) cf.generateCertificate(input);
    }

    private boolean isFromGooglePlay(String package_name) {
        String install_package_manager = this.packageManager.getInstallerPackageName(package_name);

        if (install_package_manager == null) return false;

        else if (install_package_manager.equals("com.google.android.feedback")
                || install_package_manager.equals("com.android.vending")) {
            return true;
        }

        return false;
    }

    private boolean isOwnApp(String package_name) {
        if (package_name.equals("com.fyp.ss163g.android")) return true;
        return false;
    }
}