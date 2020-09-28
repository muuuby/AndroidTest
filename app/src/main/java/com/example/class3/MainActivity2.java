package com.example.class3;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity2 extends AppCompatActivity {
    private static final String TAG = "MainActivity2";
    EditText text,text2,text3;
    String dollar,euro,won;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        text = findViewById(R.id.text);
        text2 =findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);

        //接收数据
        Intent intent = getIntent();
        float dollar2 = intent.getFloatExtra("dollar_rate_key",0.0f);
        float euro2 = intent.getFloatExtra("euro_rate_key",0.0f);
        float won2 = intent.getFloatExtra("won_rate_key",0.0f);

        Log.i(TAG,"onCreate:dollarRate="+dollar2);
        Log.i(TAG,"opCreate:euroRate="+euro2);
        Log.i(TAG,"opCreate:wonRate="+won2);

        text.setText(String.valueOf(dollar2));
        text2.setText(String.valueOf(euro2));
        text3.setText(String.valueOf(won2));

        //返回数据
        dollar = text.getText().toString();
        Float d = Float.parseFloat(dollar);
        euro = text2.getText().toString();
        Float e = Float.parseFloat(euro);
        won = text3.getText().toString();
        Float w = Float.parseFloat(won);

        Bundle bdl = new Bundle();
        bdl.putFloat("dollar_rate_key",d);
        bdl.putFloat("euro_rate_key",e);
        bdl.putFloat("won_rate_key",w);
        intent.putExtras(bdl);
    }

    public void save(View view) {
        //返回数据
        dollar = text.getText().toString();
        Float d = Float.parseFloat(dollar);
        euro = text2.getText().toString();
        Float e = Float.parseFloat(euro);
        won = text3.getText().toString();
        Float w = Float.parseFloat(won);

        //修改文件
        SharedPreferences sp = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat("dollar_rate",d);
        editor.putFloat("euro_rate",e);
        editor.putFloat("won_rate",w);
        editor.apply();

        Intent intent = getIntent();
        Bundle bdl = new Bundle();
        bdl.putFloat("dollar_rate_key",d);
        bdl.putFloat("euro_rate_key",e);
        bdl.putFloat("won_rate_key",w);
        intent.putExtras(bdl);
        setResult(3,intent);

        finish();
    }
}