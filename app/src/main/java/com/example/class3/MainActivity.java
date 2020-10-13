package com.example.class3;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    TextView out;
    EditText text2;
    String ss;
    float dollarRate,euroRate,wonRate;

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text2 = findViewById(R.id.t2);

        //读取文件
        final SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        PreferenceManager.getDefaultSharedPreferences(this);
        final String predate = sharedPreferences.getString("date","");
        Log.i(TAG,"result:date="+predate);
        dollarRate = sharedPreferences.getFloat("dollar_rate",0.0f);
        euroRate = sharedPreferences.getFloat("euro_rate",0.0f);
        wonRate = sharedPreferences.getFloat("won_rate",0.0f);

        final SharedPreferences.Editor editor = sharedPreferences.edit();

        handler = new Handler(){
            public void handleMessage(Message msg) {
                if(msg.what==3) {
                    Bundle bdl = msg.getData();
                    String date = bdl.getString("date");
                    String d = date.substring(12,22);
                    if(!d.equals(predate)){
                        Log.i(TAG,"UPDATE");
                        dollarRate = bdl.getFloat("dollar_rate_key",0.0f);
                        euroRate = bdl.getFloat("euro_rate_key",0.0f);
                        wonRate = bdl.getFloat("won_rate_key",0.0f);

                        editor.putFloat("dollar_rate",dollarRate);
                        editor.putFloat("euro_rate",euroRate);
                        editor.putFloat("won_rate",wonRate);
                        editor.putString("date",d);
                        editor.commit();
                    }
                }
                super.handleMessage(msg);
            }
        };

        //开启子线程
        myThread t = new myThread();
        new Thread(t).start();
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

    //后台线程访问网络
    public class myThread implements Runnable{
        @Override
        public void run() {
                String url = "https://www.usd-cny.com/bankofchina.htm";
                float dollar=0.0f,euro=0.0f,won=0.0f;
                String date1=null;
                try {
                    //解析html文件
                    Document doc = Jsoup.connect(url).get();
                    Log.i(TAG,"run:" + doc.title());
                    //读取时间
                    Elements date = doc.getElementsByClass("time");
                    date1 = date.text();
                    //读取table内容
                    Elements tables = doc.getElementsByTag("table");
                    Element table6 = tables.get(0);
                    Elements tds = table6.getElementsByTag("td");
                    for(int i=0;i<tds.size();i+=6){
                        Element td1 = tds.get(i);
                        Element td2 = tds.get(i+5);

                        String str1 = td1.text();
                        String val = td2.text();
                        Log.i(TAG,"run:" + str1 + "==>" + val);

                        float v = 100f / Float.parseFloat(val);
                        if(str1.equals("美元")){
                            dollar = v;
                        }else if(str1.equals("英镑")){
                            euro = v;
                        }else if(str1.equals("韩元")){
                            won = v;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //利用bundle对象来传值
                Message msg = handler.obtainMessage(3);
                Bundle bdl = new Bundle();
                bdl.putFloat("dollar_rate_key",dollar);
                bdl.putFloat("euro_rate_key",euro);
                bdl.putFloat("won_rate_key",won);
                bdl.putString("date",date1);
                msg.setData(bdl);
                msg.sendToTarget();
//            Message message = handler.obtainMessage(5);
//            message.obj = "hello from run()";
//            handler.sendMessage(message);
        }
    }

    //网页文本获取
    private String inputStream2String(InputStream inputStream)
            throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "gb2312");
        while (true) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0) {
                break;
            }
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }
}

