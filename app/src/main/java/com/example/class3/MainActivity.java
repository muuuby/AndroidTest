package com.example.class3;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    TextView out;
    EditText text2;
    String ss;
    float dollarRate,euroRate,wonRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text2 = findViewById(R.id.t2);
//        dollarRate = 0.147f;
//        euroRate = 0.1147f;
//        wonRate = 0.1f;

        //修改文件
        SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        PreferenceManager.getDefaultSharedPreferences(this);
        dollarRate = sharedPreferences.getFloat("dollar_rate",0.0f);
        euroRate = sharedPreferences.getFloat("euro_rate",0.0f);
        wonRate = sharedPreferences.getFloat("won_rate",0.0f);
    }

    public void btn1(View view) {
        ss = text2.getText().toString();
        Float f = Float.parseFloat(ss);
        out.setText("结果为"+f*0.1147);
    }

    public void btn2(View view) {
        ss = text2.getText().toString();
        Float f = Float.parseFloat(ss);
        out.setText("结果为"+f*0.147);
    }

    public void btn3(View view) {
        ss = text2.getText().toString();
        Float f = Float.parseFloat(ss);
        out.setText("结果为"+f*15.3718);
    }

    public void change(View btn) {
        ss = text2.getText().toString();
        if(ss.equals("")){
            Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
        }else{
            if(btn.getId()==R.id.button){
                Float f = Float.parseFloat(ss);
                text2.setText(""+f*dollarRate);
            }else if(btn.getId()==R.id.button2){
                Float f = Float.parseFloat(ss);
                text2.setText(""+f*euroRate);
            }else if(btn.getId()==R.id.button3){
                Float f = Float.parseFloat(ss);
                text2.setText(""+f*wonRate);
            }
        }
    }

    public void openmain2(View btn) {
        Intent main2 = new Intent(this,MainActivity2.class);
        main2.putExtra("dollar_rate_key",dollarRate);
        main2.putExtra("euro_rate_key",euroRate);
        main2.putExtra("won_rate_key",wonRate);

        Log.i(TAG,"openmain2:dollarRate="+dollarRate);
        Log.i(TAG,"openmain2:euroRate="+euroRate);
        Log.i(TAG,"openmain2:wonRate="+wonRate);

        startActivityForResult(main2, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==3){
            Bundle bdl = data.getExtras();
            dollarRate = bdl.getFloat("dollar_rate_key",0.0f);
            euroRate = bdl.getFloat("euro_rate_key",0.0f);
            wonRate = bdl.getFloat("won_rate_key",0.0f);

            Log.i(TAG,"result:dollarRate="+dollarRate);
            Log.i(TAG,"result:euroRate="+euroRate);
            Log.i(TAG,"result:wonRate="+wonRate);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.first_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.item1:
                Intent main2 = new Intent(this,MainActivity2.class);
                main2.putExtra("dollar_rate_key",dollarRate);
                main2.putExtra("euro_rate_key",euroRate);
                main2.putExtra("won_rate_key",wonRate);

                Log.i(TAG,"openmain2:dollarRate="+dollarRate);
                Log.i(TAG,"openmain2:euroRate="+euroRate);
                Log.i(TAG,"openmain2:wonRate="+wonRate);

                startActivityForResult(main2, 2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
