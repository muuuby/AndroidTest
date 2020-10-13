package com.example.class3;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RateListActivity extends ListActivity {
    private static final String TAG = "RateListActivity";
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_rate_list);
//
//        String[] list_data = {"one","two","three","four"};
//        ListAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list1);
//        setListAdapter(adapter);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==7) {
                    Bundle bdl = msg.getData();
                    List<String> list1 = new ArrayList<String>();
                    for(int i=0;i<162;i+=6){
                        String ss = bdl.getString("item"+i);
                        list1.add(ss);
                    }
  
                    ListAdapter adapter = new ArrayAdapter<String>(
                            RateListActivity.this,
                            android.R.layout.simple_list_item_1,
                            list1);
                    setListAdapter(adapter);
                }
                super.handleMessage(msg);
            }
        };

        //开启子线程
        RateListActivity.myThread t = new RateListActivity.myThread();
        new Thread(t).start();
    }

    //后台线程访问网络
    public class myThread implements Runnable{
        @Override
        public void run() {
            String url = "https://www.usd-cny.com/bankofchina.htm";
            float dollar=0.0f,euro=0.0f,won=0.0f;

            try {
                //解析html文件
                Document doc = Jsoup.connect(url).get();
                Log.i(TAG,"run:" + doc.title());
                //读取table内容
                Elements tables = doc.getElementsByTag("table");
                Element table6 = tables.get(0);
                Elements tds = table6.getElementsByTag("td");

                //利用bundle对象来传值
                Message msg = handler.obtainMessage(7);
                Bundle bdl = new Bundle();
                for(int i=0;i<tds.size();i+=6){
                    Element td1 = tds.get(i);
                    Element td2 = tds.get(i+5);

                    String str1 = td1.text();
                    String val = td2.text();
                    Log.i(TAG,"run:" + str1 + "==>" + val);
                    bdl.putString("item"+i,str1 + "==>" + val);
                }
                msg.setData(bdl);
                msg.sendToTarget();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}