package com.ss174h.amsa.EnvCondition;


import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import com.ss174h.amsa.R;

public class ReviewEnv extends AppCompatActivity implements ParserResponseInterface{

    private ReviewEnvPresenter reviewEnvPresenter;
    private TextView curVersionView;
    private TextView latVersionView;
    private TextView adviceView;

    //private String latestRelease;
    private String currentAndroidVersion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_env);
        reviewEnvPresenter = new ReviewEnvPresenter(this);
        currentAndroidVersion = reviewEnvPresenter.getAndroidVersion();
        String phoneModel = reviewEnvPresenter.getDeviceName();
        curVersionView = (TextView) findViewById(R.id.curVerText);
        latVersionView = (TextView) findViewById(R.id.latVerText);
        adviceView = (TextView) findViewById(R.id.adviceText);

        new HtmlParser(this).execute("https://en.wikipedia.org/wiki/Android_(operating_system)");

        String model = "ddff";

        String gh = "sdsd";

    }

    @Override
    public void onParsingDone(ArticleModel articleModel) {
        if(articleModel!=null){
            String latestRelease = articleModel.getArticle();
            String versionNoText = latestRelease.substring(0,3);
            Log.d("Latest version num text",versionNoText);
            Float androidNo = Float.parseFloat(versionNoText);
            Log.d("Float version num text",androidNo+"");
            Log.d("Current android ver",reviewEnvPresenter.getAndroidVersionNum()+"");
            String adviceMsg = "";

            if(reviewEnvPresenter.getAndroidVersionNum()<androidNo) {
                adviceMsg = "You may not have the latest version of android operating system for your device" +
                            " please go to settings and check whether an update is available for you.";

                Log.d("comparing version",adviceMsg);

                String knownVulnerabilities = "";

                if(reviewEnvPresenter.getAndroidVersionNum()<5.1)
                {
                    knownVulnerabilities = "This version is known to have " +
                            "vulnerabilities which can be exploited " +
                            "via a single MMS message. Please check with your manufacturer for any updates.\n";
                }

                else if(reviewEnvPresenter.getAndroidVersionNum()<7)
                {
                   knownVulnerabilities = "This version is known to have a " +
                            "vulnerability that could lead to a DOS attack on your device. Please check with your manufacturer for any updates.  " +
                            "Please note, the AMSA will not be compatible with Android 7.0 and later.";
                }

                adviceMsg = adviceMsg + "\n\n" + knownVulnerabilities;
            }

            else
            {
                adviceMsg = "You have the latest operating system, no update is required ";
            }
            curVersionView.setText(currentAndroidVersion);
            latVersionView.setText(latestRelease);
            adviceView.setText(adviceMsg);

        }
        else
            curVersionView.setText("Something wrong! Can't parse HTML");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
