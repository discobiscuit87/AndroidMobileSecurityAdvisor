package com.ss174h.amsa.EnvCondition;

import android.content.Intent;
import android.os.Build;


import static android.content.Context.BATTERY_SERVICE;

/**
 * Created by jianwen on 2/1/18.
 */

//This class contains logic only

public class ReviewEnvPresenter {

    private ReviewEnv reviewEnvPre;
    String codeName = "";

    public ReviewEnvPresenter(ReviewEnv reviewEnvPre)
    {
        this.reviewEnvPre = reviewEnvPre;
    }


    //Returns android sdk and version number

    public String getAndroidVersion()
    {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;

        double releaseNumeric = Double.parseDouble(Build.VERSION.RELEASE.replaceAll("(\\d+[.]\\d+)(.*)","$1"));

        if(releaseNumeric<5 && releaseNumeric==4.4)
            codeName = "Kitkat";

        else if(releaseNumeric<5 && releaseNumeric==4.3)
            codeName = "Jelly Bean";

        else if(releaseNumeric<6)
            codeName = "Lollipop";

        else if(releaseNumeric<7)
            codeName = "Marshmallow";
        else if(releaseNumeric<8)
            codeName = "Nougat";
        else if(releaseNumeric<9)
            codeName = "Oreo";

        return "Android SDK: " + sdkVersion + " (Operating System Version: " + release + " " + codeName +")";
    }

    //Returns android version number e.g 6.1.0

    public float getAndroidVersionNum()
    {
        String release = Build.VERSION.RELEASE;

        float num = Float.parseFloat(release.substring(0,2));

        return num;
    }

    public boolean isOldAndroidVersion()
    {
        if(codeName.equalsIgnoreCase("Oreo"))
        {
            return true;
        }

        return true;
    }

    public String getAndroidLatestVersion()
    {
        return "Oreo";
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public String getBatteryPercentage(Intent batteryChangedIntent) {
        int level = batteryChangedIntent.getIntExtra("level", 0);
        int scale = batteryChangedIntent.getIntExtra("scale", 100);
        return String.valueOf(level * 100 / scale) + "%";
    }
}
