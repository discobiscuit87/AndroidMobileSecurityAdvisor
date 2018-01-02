package com.ss174h.amsa.EnvCondition;
import android.os.Build;
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

    public String getAndroidVersion()
    {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;

        double releaseNumeric = Double.parseDouble(Build.VERSION.RELEASE.replaceAll("(\\d+[.]\\d+)(.*)","$1"));

        if(releaseNumeric<7)
            codeName = "Marshmallow";
        else if(releaseNumeric<8)
            codeName = "Nougat";
        else if(releaseNumeric<9)
            codeName = "Oreo";

        return "Android SDK: " + sdkVersion + " (Operating System Version: " + release + " " + codeName +")";
    }

    public boolean checkAndroidVersion()
    {
        if(codeName.equalsIgnoreCase("Oreo"))
        {
            return true;
        }

        return false;
    }

    public String getAndroidLatestVersion()
    {
        return "Oreo";
    }
}
