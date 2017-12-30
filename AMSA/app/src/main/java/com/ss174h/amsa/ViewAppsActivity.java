package com.ss174h.amsa;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ss174h.amsa.APKScanner.CheckAppCertActivity;
import com.ss174h.amsa.CheckPermissions.CheckAppPermissionsActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ViewAppsActivity extends AppCompatActivity {

    private String version, name, installedOn, packName, type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final ArrayList<String> packages;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_apps);

        Intent intent = getIntent();
        packages = intent.getStringArrayListExtra("array");
        type = intent.getStringExtra("Check");

        TextView t = findViewById(R.id.page_title);
        t.setText("Sideloaded Applications");

        AppAdapter appAdapter = new AppAdapter(this,packages);
        final ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(appAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ApplicationInfo applicationInfo;

                try {
                    applicationInfo = getPackageManager().getApplicationInfo(packages.get(position), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    return;
                }

                if(type.equals("CheckCerts")) {
                    getAppInfo(getPackageManager(),applicationInfo);
                    Intent intent = new Intent(view.getContext(),CheckAppCertActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("version", version);
                    intent.putExtra("installedOn", installedOn);
                    intent.putExtra("packName", packName);
                    startActivity(intent);
                } else if(type.equals("CheckPerms")) {
                    getAppInfo(getPackageManager(),applicationInfo);
                    Intent intent = new Intent(view.getContext(),CheckAppPermissionsActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("version", version);
                    intent.putExtra("installedOn", installedOn);
                    intent.putExtra("packName", packName);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    public void getAppInfo(PackageManager pm, ApplicationInfo appInfo) {

        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        CharSequence label = appInfo.loadLabel(pm);
        name = label.toString();

        try {
            PackageInfo packageInfo = pm.getPackageInfo(appInfo.packageName, 0);
            version = "Version " + packageInfo.versionName + " (" + packageInfo.versionCode + ")";
            packName = packageInfo.packageName;
        } catch (PackageManager.NameNotFoundException nnf) {
            version = "nothing found";
        }

        try {
            PackageInfo packageInfo = pm.getPackageInfo(appInfo.packageName, PackageManager.GET_PERMISSIONS);
            installedOn = "Installed on: " + date.format(new Date(packageInfo.firstInstallTime));
        } catch (PackageManager.NameNotFoundException nnf) {
            installedOn = "";
        }
    }
}
