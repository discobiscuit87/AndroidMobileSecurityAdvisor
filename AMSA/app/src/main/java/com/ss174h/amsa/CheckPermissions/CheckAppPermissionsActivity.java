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

import java.io.IOException;
import java.util.ArrayList;

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
            String address = "https://play.google.com/store/apps/details?id="+arg0[0];

            try {
                category = Jsoup.connect(address)
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("span[itemprop=genre]")
                        .first()
                        .ownText();
            } catch (IOException io) {

            }

            return category;
        }

        @Override
        protected void onPostExecute(String result) {
            if(category == null) {
                TextView t6 = (TextView) findViewById(R.id.advice);
                t6.append("Unable to identify the category for the application.  If the dangerous permissions above seem excessive for it's category, exercise extreme caution.\n");
            } else {
                Log.e("Cat",category);
                switch(category) {
                    case "Art & Design":
                        art();
                        break;
                    case "Auto & Vehicles":
                        auto();
                        break;
                    case "Beauty":
                        beauty();
                        break;
                    case "Books & Reference":
                        books();
                        break;
                    case "Business":
                        business();
                        break;
                    case "Comics":
                        comics();
                        break;
                    case "Communication":
                        comms();
                        break;
                    case "Dating":
                        dating();
                        break;
                    case "Education":
                        education();
                        break;
                    case "Entertainment":
                        entertainment();
                        break;
                    case "Events":
                        events();
                        break;
                    case "Finance":
                        finance();
                        break;
                    case "Food & Drink":
                        food();
                        break;
                    case "Health & Fitness":
                        health();
                        break;
                    case "House & Home":
                        house();
                        break;
                    case "Lifestle":
                        lifestyle();
                        break;
                    case "Maps & Navigation":
                        maps();
                        break;
                    case "Medical":
                        medical();
                        break;
                    case "Music & Audio":
                        music();
                        break;
                    case "News & Magazines":
                        news();
                        break;
                    case "Parenting":
                        parenting();
                        break;
                    case "Personalization":
                        personalization();
                        break;
                    case "Photography":
                        photography();
                        break;
                    case "Productivity":
                        productivity();
                        break;
                    case "Shopping":
                        shopping();
                        break;
                    case "Social":
                        social();
                        break;
                    case "Sports":
                        sports();
                        break;
                    case "Tools":
                        tool();
                        break;
                    case "Travel & Local":
                        travel();
                        break;
                    case "Video Players & Editors":
                        video();
                        break;
                    case "Weather":
                        weather();
                        break;
                    case "Libraries & Demo":
                        lib();
                        break;
                }
            }
        }
    }

    public void art() {
        TextView t6 = (TextView) findViewById(R.id.advice);
        t6.setMovementMethod(new ScrollingMovementMethod());
        if(permissions.contains("This app can read from your external storage.\n")
                || permissions.contains("This app can write information to your external storage.\n")) {
            t6.setText("As this application is an Art & Design application, there is a reasonable chance that it requires external storage related permissions. Any other listed dangerous permissions may be unnecessary for it's operation.\n");
        } else {
            t6.setText("As this application is an Art & Design application, these permissions seem to be unnecessary for it's category.\n");
        }
    }

    public void auto() {
        int count = 0;
        TextView t6 = (TextView) findViewById(R.id.advice);
        t6.setMovementMethod(new ScrollingMovementMethod());
        if(permissions.contains("This app can read from your external storage.\n")
                || permissions.contains("This app can write information to your external storage.\n")) {
            t6.setText("As this application is an Auto & Vehicles application, there is a reasonable chance that it requires external storage related permissions.\n");
            count++;
        } else if(permissions.contains("This app can view your precise location.\n")
                || permissions.contains("This app can view your approximate location.\n")) {
            t6.append("As this application is an Auto & Vehicles application, there is a reasonable chance that it requires location related permissions.\n");
            count++;
        } else {
            t6.setText("As this application is an Auto & Vehicles application, these permissions may be unnecessary for it's category.\n");
        }

        if(count > 0) {
            t6.append("Any other listed dangerous permissions may be unnecessary for it's operation.\n");
        }
    }

    public void beauty() {
        TextView t6 = (TextView) findViewById(R.id.advice);
        t6.setMovementMethod(new ScrollingMovementMethod());
        if(permissions.contains("This app can access your camera.\n")) {
            t6.setText("As this application is a Beauty application, there is a reasonable chance that it requires camera access permissions. Any other listed dangerous permissions may be unnecessary for it's operation.\n");
        } else {
            t6.setText("As this application is a Beauty application, these permissions may be unnecessary for it's category.\n");
        }
    }

    public void books() {
        int count = 0;
        TextView t6 = (TextView) findViewById(R.id.advice);
        t6.setMovementMethod(new ScrollingMovementMethod());
        if(permissions.contains("This app can read from your external storage.\n")
                || permissions.contains("This app can write information to your external storage.\n")) {
            t6.setText("As this application is a Books & Reference application, there is a reasonable chance that it requires external storage related permissions.\n");
            count++;
        } else if(permissions.contains("This app can see a list of user accounts stored on this device.\n")) {
            t6.append("As this application is a Books & Reference application, there is a reasonable chance that it requires access to user accounts stored on the device.\n");
            count++;
        } else {
            t6.setText("As this application is an Books & Reference application, these permissions may be unnecessary for it's category.\n");
        }

        if(count > 0) {
            t6.append("Any other listed dangerous permissions may be unnecessary for it's operation.\n");
        }
    }

    public void business() {
        int count = 0;
        TextView t6 = (TextView) findViewById(R.id.advice);
        t6.setMovementMethod(new ScrollingMovementMethod());
        if(permissions.contains("This app can read from your external storage.\n")
                || permissions.contains("This app can write information to your external storage.\n")) {
            t6.setText("As this application is a Business application, there is a reasonable chance that it requires external storage related permissions.\n");
            count++;
        } else if(permissions.contains("This app can view your precise location.\n")
                || permissions.contains("This app can view your approximate location.\n")) {
            t6.append("As this application is a Business application, there is a reasonable chance that it requires location related permissions.\n");
            count++;
        } else if(permissions.contains("This app can view your calendar.\n") ||
                permissions.contains("This app can write to your calendar.\n")) {
            t6.append("As this application is a Business application, there is a reasonable chance that it requires calendar related permissions.\n");
            count++;
        } else {
            t6.setText("As this application is a Business application, these permissions may be unnecessary for it's category.\n");
        }

        if(count > 0) {
            t6.append("Any other listed dangerous permissions may be unnecessary for it's operation.\n");
        }
    }

    public void comics() {
        TextView t6 = (TextView) findViewById(R.id.advice);
        t6.setMovementMethod(new ScrollingMovementMethod());
        if(permissions.contains("This app can read from your external storage.\n")
                || permissions.contains("This app can write information to your external storage.\n")) {
            t6.append("As this application is a Comics application, there is a reasonable chance that it requires external storage related permissions. Any other listed dangerous permissions may be unnecessary for it's operation.\n");
        } else {
            t6.append("As this application is a Comics application, these permissions seem to be unnecessary for it's category.\n");
        }
    }

    public void comms() {
        int count = 0;
        TextView t6 = (TextView) findViewById(R.id.advice);
        t6.setMovementMethod(new ScrollingMovementMethod());
        if(permissions.contains("This app can see a list of user accounts stored on this device.\n")) {
            t6.append("As this application is a Communication application, there is a reasonable chance that it requires access to user accounts stored on the device.\n");
            count++;
        } else if(permissions.contains("This app can view your precise location.\n")
                || permissions.contains("This app can view your approximate location.\n")) {
            t6.append("As this application is a Communication application, there is a reasonable chance that it requires location related permissions.\n");
            count++;
        }  else if(permissions.contains("This app can view your calendar.\n") ||
                permissions.contains("This app can write to your calendar.\n")) {
            t6.append("As this application is a Communication application, there is a reasonable chance that it requires calendar related permissions.\n");
            count++;
        } else if(permissions.contains("This app can access your camera.\n")) {
            t6.append("As this application is a Communication application, there is a reasonable chance that it requires camera access permissions.\n");
            count++;
        } else if(permissions.contains("This app can record audio through your device.\n")) {
            t6.append("As this application is a Communication application, there is a reasonable chance that it requires microphone access permissions.\n");
            count++;
        } else if(permissions.contains("This app can see the details of your contacts.\n") ||
                permissions.contains("This app can write information to your contacts.\n")) {
            t6.append("As this application is a Communication application, there is a reasonable chance that it requires contacts related permissions.\n");
            count++;
        } else {
            t6.append("As this application is a Communication application, these permissions seem to be unnecessary for it's category.\n");
        }

        if(count > 0) {
            t6.append("Any other listed dangerous permissions may be unnecessary for it's operation.\n");
        }
    }

    public void dating() {
        int count = 0;
        TextView t6 = (TextView) findViewById(R.id.advice);
        t6.setMovementMethod(new ScrollingMovementMethod());
        if(permissions.contains("This app can see a list of user accounts stored on this device.\n")) {
            t6.append("As this application is a Dating application, there is a reasonable chance that it requires access to user accounts stored on the device.\n");
            count++;
        } else if(permissions.contains("This app can view your precise location.\n")
                || permissions.contains("This app can view your approximate location.\n")) {
            t6.append("As this application is a Dating application, there is a reasonable chance that it requires location related permissions.\n");
            count++;
        } else if(permissions.contains("This app can view your calendar.\n") ||
                permissions.contains("This app can write to your calendar.\n")) {
            t6.append("As this application is a Dating application, there is a reasonable chance that it requires calendar related permissions.\n");
            count++;
        } else if(permissions.contains("This app can access your camera.\n")) {
            t6.append("As this application is a Dating application, there is a reasonable chance that it requires camera access permissions.\n");
            count++;
        } else if(permissions.contains("This app can record audio through your device.\n")) {
            t6.append("As this application is a Dating application, there is a reasonable chance that it requires microphone access permissions.\n");
            count++;
        } else {
            t6.append("As this application is a Dating application, these permissions seem to be unnecessary for it's category.\n");
        }

        if(count > 0) {
            t6.append("Any other listed dangerous permissions may be unnecessary for it's operation.\n");
        }
    }

    public void education() {
        int count = 0;
        TextView t6 = (TextView) findViewById(R.id.advice);
        t6.setMovementMethod(new ScrollingMovementMethod());
        if(permissions.contains("This app can see a list of user accounts stored on this device.\n")) {
            t6.append("As this application is an Education application, there is a reasonable chance that it requires access to user accounts stored on the device.\n");
            count++;
        } else if(permissions.contains("This app can view your calendar.\n") ||
                permissions.contains("This app can write to your calendar.\n")) {
            t6.append("As this application is an Education application, there is a reasonable chance that it requires calendar related permissions.\n");
            count++;
        } else if(permissions.contains("This app can record audio through your device.\n")) {
            t6.append("As this application is an Education application, there is a reasonable chance that it requires microphone access permissions.\n");
            count++;
        } else if(permissions.contains("This app can read from your external storage.\n")
                || permissions.contains("This app can write information to your external storage.\n")) {
            t6.append("As this application is an Education application, there is a reasonable chance that it requires external storage related permissions.\n");
            count++;
        } else {
            t6.append("As this application is an Education application, these permissions seem to be unnecessary for it's category.\n");
        }

        if(count > 0) {
            t6.append("Any other listed dangerous permissions may be unnecessary for it's operation.\n");
        }
    }

    public void entertainment() {
        int count = 0;
        TextView t6 = (TextView) findViewById(R.id.advice);
        t6.setMovementMethod(new ScrollingMovementMethod());
        if(permissions.contains("This app can see a list of user accounts stored on this device.\n")) {
            t6.append("As this application is an Entertainment application, there is a reasonable chance that it requires access to user accounts stored on the device.\n");
            count++;
        } else if(permissions.contains("This app can record audio through your device.\n")) {
            t6.append("As this application is an Entertainment application, there is a reasonable chance that it requires microphone access permissions.\n");
            count++;
        } else if(permissions.contains("This app can access your camera.\n")) {
            t6.append("As this application is an Entertainment application, there is a reasonable chance that it requires camera access permissions.\n");
            count++;
        } else if(permissions.contains("This app can read from your external storage.\n")
                || permissions.contains("This app can write information to your external storage.\n")) {
            t6.append("As this application is an Entertainment application, there is a reasonable chance that it requires external storage related permissions.\n");
            count++;
        } else {
            t6.append("As this application is an Entertainment application, these permissions seem to be unnecessary for it's category.\n");
        }

        if(count > 0) {
            t6.append("Any other listed dangerous permissions may be unnecessary for it's operation.\n");
        }
    }

    public void events() {
        int count = 0;
        TextView t6 = (TextView) findViewById(R.id.advice);
        t6.setMovementMethod(new ScrollingMovementMethod());
        if(permissions.contains("This app can view your calendar.\n") ||
                permissions.contains("This app can write to your calendar.\n")) {
            t6.append("As this application is an Events application, there is a reasonable chance that it requires calendar related permissions.\n");
            count++;
        } else if(permissions.contains("This app can view your precise location.\n")
                || permissions.contains("This app can view your approximate location.\n")) {
            t6.append("As this application is an Events application, there is a reasonable chance that it requires location related permissions.\n");
            count++;
        } else if(permissions.contains("This app can view your calendar.\n") ||
                permissions.contains("This app can write to your calendar.\n")) {
            t6.append("As this application is an Events application, there is a reasonable chance that it requires calendar related permissions.\n");
            count++;
        } else if(permissions.contains("This app can see a list of user accounts stored on this device.\n")) {
            t6.append("As this application is an Events application, there is a reasonable chance that it requires access to user accounts stored on the device.\n");
            count++;
        } else {
            t6.append("As this application is an Entertainment application, these permissions seem to be unnecessary for it's category.\n");
        }

        if(count > 0) {
            t6.append("Any other listed dangerous permissions may be unnecessary for it's operation.\n");
        }
    }

    public void finance() {
        int count = 0;
        TextView t6 = (TextView) findViewById(R.id.advice);
        t6.setMovementMethod(new ScrollingMovementMethod());
        if(permissions.contains("This app can see a list of user accounts stored on this device.\n")) {
            t6.append("As this application is a Finance application, there is a reasonable chance that it requires access to user accounts stored on the device.\n");
            count++;
        } else if(permissions.contains("This app can access your camera.\n")) {
            t6.append("As this application is a Finance application, there is a reasonable chance that it requires camera access permissions.\n");
            count++;
        } else if(permissions.contains("This app can read from your external storage.\n")
                || permissions.contains("This app can write information to your external storage.\n")) {
            t6.append("As this application is a Finance application, there is a reasonable chance that it requires external storage related permissions.\n");
            count++;
        }
    }

    public void food() {
        int count = 0;
        TextView t6 = (TextView) findViewById(R.id.advice);
        t6.setMovementMethod(new ScrollingMovementMethod());
        if(permissions.contains("This app can view your precise location.\n")
                || permissions.contains("This app can view your approximate location.\n")) {
            t6.append("As this application is a Food & Drink application, there is a reasonable chance that it requires location related permissions.\n");
            count++;
        } else if(permissions.contains("This app can view your calendar.\n") ||
                permissions.contains("This app can write to your calendar.\n")) {
            t6.append("As this application is a Food & Drink application, there is a reasonable chance that it requires calendar related permissions.\n");
            count++;
        } else if(permissions.contains("This app can see a list of user accounts stored on this device.\n")) {
            t6.append("As this application is an Food & Drink application, there is a reasonable chance that it requires access to user accounts stored on the device.\n");
            count++;
        } else {
            t6.append("As this application is a Food & Drink application, these permissions seem to be unnecessary for it's operation.");
        }

        if(count > 0) {
            t6.append("Any other listed dangerous permissions may be unnecessary for it's operation.\n");
        }
    }

    public void health() {

    }

    public void house() {

    }

    public void lifestyle() {

    }

    public void maps() {

    }

    public void medical() {

    }

    public void music() {

    }

    public void news() {

    }

    public void parenting() {

    }

    public void personalization() {

    }

    public void photography() {

    }

    public void productivity() {

    }

    public void shopping() {

    }

    public void social() {

    }

    public void sports() {

    }

    public void tool() {

    }

    public void travel() {

    }

    public void video() {

    }

    public void weather() {

    }

    public void lib() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
