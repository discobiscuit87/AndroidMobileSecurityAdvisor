package com.ss174h.amsa.CheckPermissions;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.text.method.ScrollingMovementMethod;

import com.ss174h.amsa.R;

public class CheckAppPermissionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_app_permissions);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String version = intent.getStringExtra("version");
        String packName = intent.getStringExtra("packName");
        String permissions = "";

        try {
            Drawable icon = getPackageManager().getApplicationIcon(packName);
            ImageView imageView = findViewById(R.id.app_icon);
            imageView.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException nnf) {

        }

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(packName,PackageManager.GET_PERMISSIONS);
            String[] requestedPermissions = packageInfo.requestedPermissions;
            for(String permission : requestedPermissions) {
                permissions += permission + "\n";
            }
        } catch (PackageManager.NameNotFoundException nnf) {

        }

        TextView t1 = findViewById(R.id.app_name);
        t1.setText(name);

        TextView t2 = findViewById(R.id.version);
        t2.setText(version);

        TextView t3 = findViewById(R.id.permissions);
        t3.setText(permissions);
        t3.setMovementMethod(new ScrollingMovementMethod());
    }
}
