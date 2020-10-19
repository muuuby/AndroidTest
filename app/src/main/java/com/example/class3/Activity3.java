package com.example.class3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Activity3 extends AppCompatActivity {

    private static final String TAG ="Activity3" ;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);

        text = findViewById(R.id.textView1);

        //接收数据
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String detail = intent.getStringExtra("detail");

        Log.i(TAG,""+title);
        Log.i(TAG,""+detail);

        text.setText(title);
    }
}