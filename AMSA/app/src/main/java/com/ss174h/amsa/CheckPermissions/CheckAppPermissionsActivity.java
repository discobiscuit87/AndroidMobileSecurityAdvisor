package com.ss174h.amsa.CheckPermissions;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.text.method.ScrollingMovementMethod;

import com.ss174h.amsa.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class CheckAppPermissionsActivity extends AppCompatActivity {

    String permissions = "";
    int count = 0;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_app_permissions);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String version = intent.getStringExtra("version");
        String packName = intent.getStringExtra("packName");
        String danger = "";
        String[] out = new String[1];
        out[0] = packName;
        new GetData().execute(out);

        try {
            Drawable icon = getPackageManager().getApplicationIcon(packName);
            ImageView imageView = (ImageView) findViewById(R.id.app_icon);
            imageView.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException nnf) {

        }

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(packName,PackageManager.GET_PERMISSIONS);
            String[] requestedPermissions = packageInfo.requestedPermissions;

            for(String permission : requestedPermissions) {
                checkPermissions(permission);
            }
            danger = count + " Dangerous Permissions Found";
        } catch (PackageManager.NameNotFoundException nnf) {

        }

        TextView t1 = (TextView) findViewById(R.id.app_name);
        t1.setText(name);

        TextView t2 = (TextView) findViewById(R.id.version);
        t2.setText(version);

        TextView t3 = (TextView) findViewById(R.id.perms_info);
        t3.setText(danger);

        TextView t4 = (TextView) findViewById(R.id.permissions);
        if(count == 0) {
            t4.setTextColor(Color.GREEN);
            permissions = "This application contains no dangerous permissions";
        } else {
            t4.setTextColor(Color.RED);
        }
        t4.setText(permissions);
        t4.setMovementMethod(new ScrollingMovementMethod());

        TextView t5 = (TextView) findViewById(R.id.advice_info);
        t5.setText("Permissions Advice");
    }

    public void checkPermissions(String permission) {

        if(Manifest.permission.READ_CALENDAR.equals(permission)) {
            permissions += "This app can view your calendar.\n";
            count++;
        } else if(Manifest.permission.WRITE_CALENDAR.equals(permission)) {
            permissions += "This app can write to your calendar.\n";
            count++;
        } else if(Manifest.permission.CAMERA.equals(permission)) {
            permissions += "This app can access your camera.\n";
            count++;
        } else if(Manifest.permission.READ_CONTACTS.equals(permission)) {
            permissions += "This app can see the details of your contacts.\n";
            count++;
        } else if(Manifest.permission.WRITE_CONTACTS.equals(permission)) {
            permissions += "This app can write information to your contacts.\n";
            count++;
        } else if(Manifest.permission.GET_ACCOUNTS.equals(permission)) {
            permissions += "This app can see a list of user accounts stored on this device.\n";
            count++;
        } else if(Manifest.permission.ACCESS_FINE_LOCATION.equals(permission)) {
            permissions += "This app can view your precise location.\n";
            count++;
        } else if(Manifest.permission.ACCESS_COARSE_LOCATION.equals(permission)) {
            permissions += "This app can view your approximate location.\n";
            count++;
        } else if(Manifest.permission.RECORD_AUDIO.equals(permission)) {
            permissions += "This app can record audio through your device.\n";
            count++;
        } else if(Manifest.permission.READ_PHONE_STATE.equals(permission)) {
            permissions += "This app can get your cell network information.\n";
            count++;
        } else if(Manifest.permission.READ_PHONE_NUMBERS.equals(permission)) {
            permissions += "This app can see your phone number.\n";
            count++;
        } else if(Manifest.permission.CALL_PHONE.equals(permission)) {
            permissions += "This app can initiate a phone call without your confirmation.\n";
            count++;
        } else if(Manifest.permission.ANSWER_PHONE_CALLS.equals(permission)) {
            permissions += "This app can answer incoming phone calls.\n";
            count++;
        } else if(Manifest.permission.READ_CALL_LOG.equals(permission)) {
            permissions += "This app can view your incoming and outgoing call logs.\n";
            count++;
        } else if(Manifest.permission.WRITE_CALL_LOG.equals(permission)) {
            permissions += "This app can write information to your incoming and outgoing call logs.\n";
            count++;
        } else if(Manifest.permission.ADD_VOICEMAIL.equals(permission)) {
            permissions += "This app can add voicemails into your voicemail system.\n";
            count++;
        } else if(Manifest.permission.WRITE_CALL_LOG.equals(permission)) {
            permissions += "This app can write information to your incoming and outgoing call logs.\n";
            count++;
        } else if(Manifest.permission.USE_SIP.equals(permission)) {
            permissions += "This app can use the SIP service.\n";
            count++;
        } else if(Manifest.permission.PROCESS_OUTGOING_CALLS.equals(permission)) {
            permissions += "This app can redirect or abort your outgoing calls.\n";
            count++;
        }  else if(Manifest.permission.BODY_SENSORS.equals(permission)) {
            permissions += "This app can view information (e.g. heart-rate) from any body sensors you may use.\n";
            count++;
        } else if(Manifest.permission.SEND_SMS.equals(permission)) {
            permissions += "This app can send SMS messages.\n";
            count++;
        } else if(Manifest.permission.RECEIVE_SMS.equals(permission)) {
            permissions += "This app can receive SMS messages.\n";
            count++;
        } else if(Manifest.permission.READ_SMS.equals(permission)) {
            permissions += "This app can read your SMS messages.\n";
            count++;
        } else if(Manifest.permission.WRITE_CALL_LOG.equals(permission)) {
            permissions += "This app can receive WAP Push messages.\n";
            count++;
        } else if(Manifest.permission.WRITE_CALL_LOG.equals(permission)) {
            permissions += "This app can monitor incoming MMS messages.\n";
            count++;
        } else if(Manifest.permission.READ_EXTERNAL_STORAGE.equals(permission)) {
            permissions += "This app can read from your external storage.\n";
            count++;
        } else if(Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission)) {
            permissions += "This app can write information to your external storage.\n";
            count++;
        }
    }

    private class GetData extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... arg0) {
            String address = "https://play.google.com/store/apps/details?id=" + arg0[0];

            try {
                category = Jsoup.connect(address)
                        .get()
                        .select("a.document-subtitle.category")
                        .first()
                        .attr("href");
            } catch (IOException io) {

            }

            return category;
        }

        @Override
        protected void onPostExecute(String result) {
            TextView t6 = (TextView) findViewById(R.id.advice);
            AppCategories appCategories = new AppCategories();
            t6.setText(appCategories.checkCategory(category, permissions));
            t6.setMovementMethod(new ScrollingMovementMethod());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
