package com.ss174h.amsa.EnvCondition;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ss174h.amsa.R;

import org.json.JSONObject;

import java.util.List;

public class ReviewEnv extends AppCompatActivity implements ParserResponseInterface{

    private ReviewEnvPresenter reviewEnvPresenter;
    private TextView curVersionView;
    private TextView latVersionView;
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

        //txtView.setText(currentAndroidVersion);

        //new HtmlParser(this).execute("https://developer.android.com/training/index.html");
        new HtmlParser(this).execute("https://en.wikipedia.org/wiki/Android_(operating_system)");

        String model = "ddff";

        //new HtmlParser(this).execute("http://www.manutd.com/");


        //new HtmlParser(this).execute("https://developer.android.com/training/index.html");

        //Prompt for system update if current os version is not the latest
        /*
        if(reviewEnvPresenter.isOldAndroidVersion()==true)
        {
            // 1. Instantiate an AlertDialog.Builder with its constructor
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked update os button

                    dialog.dismiss();
                }
            });

            builder.setPositiveButton("Update OS",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked update os button

                    startActivityForResult(new Intent(Settings.ACTION_DEVICE_INFO_SETTINGS),0);
                }
            });

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage("Please select system update to get the latest operating" +
                    " System for your device").setTitle("System Update");

            // 3. Get the AlertDialog from create()
            AlertDialog dialog = builder.create();
            dialog.show();
            //envInfoPresenter.formText();

        }
        */
    }

    @Override
    public void onParsingDone(ArticleModel articleModel) {

        //progressBar.setVisibility(View.GONE);

        if(articleModel!=null){
           //txtView.setText(articleModel.getHeadline());


            String latestRelease = articleModel.getArticle();
            curVersionView.setText(currentAndroidVersion);
            latVersionView.setText(latestRelease);
            //String latestRelease = articleModel.getArticle().substring(articleModel.getArticle().indexOf("Latest Release"),articleModel.getArticle().indexOf("Latest Release")+20);
            //txtView.setText(articleModel.getArticle());

            //txtView.setText(latestRelease);
        }
        else
            curVersionView.setText("Something wrong! Can't parse HTML");
    }
}
