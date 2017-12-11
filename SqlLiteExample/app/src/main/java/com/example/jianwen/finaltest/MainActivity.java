package com.example.jianwen.finaltest;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";


    private Button btnCreateDatabase;
    private MySqliteOpenHelper mySqliteOpenHelper;
    private SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {

        mySqliteOpenHelper = new MySqliteOpenHelper(getApplicationContext());
        EditText editText = (EditText) findViewById(R.id.editText);
        mySqliteOpenHelper.insertTest(editText.getText().toString());

        Intent intent = new Intent(this, DisplayMessageActivity.class);
        startActivity(intent);
    }
}


