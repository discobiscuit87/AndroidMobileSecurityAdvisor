package com.ss174h.amsa;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewAppsActivity extends AppCompatActivity {

    private String pageName, mDelete = null;
    private ArrayList<String> packages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_apps);

        Intent intent = getIntent();
        pageName = intent.getStringExtra("toolbarName");
        packages = intent.getStringArrayListExtra("array");

    }

    @Override
    public void onResume() {
        super.onResume();

        if (mDelete != null) {

            try {
                PackageManager pm = this.getPackageManager();
                PackageInfo info = pm.getPackageInfo(mDelete, 0);
            } catch (PackageManager.NameNotFoundException ex) {
                for (int i = 0; i < packages.size(); i++) {
                    if (mDelete.equals(packages.get(i))) {
                        packages.remove(i);
                    }
                }
            }
        }

        displayApps();
    }

    public void displayApps() {

        TextView t = findViewById(R.id.page_title);
        t.setText(pageName);


    }
}
