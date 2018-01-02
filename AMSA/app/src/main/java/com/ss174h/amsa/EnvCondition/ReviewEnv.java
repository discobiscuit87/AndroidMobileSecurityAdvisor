package com.ss174h.amsa.EnvCondition;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.ss174h.amsa.R;

public class ReviewEnv extends AppCompatActivity {

    private ReviewEnvPresenter reviewEnvPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_env);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        reviewEnvPresenter = new ReviewEnvPresenter(this);

        String currentAndroidVersion = reviewEnvPresenter.getAndroidVersion();
        TextView txtView = (TextView) findViewById(R.id.osVersion);

        txtView.setText(currentAndroidVersion);

        //Prompt for system update if current os version is not the latest
        if(!reviewEnvPresenter.checkAndroidVersion())
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
    }
}
