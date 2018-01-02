package com.ss174h.amsa.EnvCondition;
import android.os.Build;
/**
 * Created by jianwen on 2/1/18.
 */

//This class contains logic only

public class ReviewEnvPresenter {

    private ReviewEnv reviewEnvPre;

    public ReviewEnvPresenter(ReviewEnv reviewEnvPre)
    {
        this.reviewEnvPre = reviewEnvPre;
    }

    public String getAndroidVersion()
    {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return "Android SDK: " + sdkVersion + " (Operating System Version: " + release +")";
    }

    public String getAndroidLatestVersion()
    {
        return "";
    }
}
