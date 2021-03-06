package com.example.class3;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RateListActivity extends ListActivity implements
        AdapterView.OnItemClickListener {
    private static final String TAG = "RateListActivity";
    Handler handler;
    String titleStr;
    String detailStr;

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
                    ArrayList<HashMap<String, String>> listItems;
                    SimpleAdapter listItemAdapter;
                    listItems = new ArrayList<HashMap<String, String>>();
                    //获取数据
                    Bundle bdl = msg.getData();
                    String date = bdl.getString("date");
                    String d = date.substring(12, 22);

                    //读取文件里储存的日期
                    final SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
                    PreferenceManager.getDefaultSharedPreferences(RateListActivity.this);
                    final String predate = sharedPreferences.getString("date", "");
                    Log.i(TAG, "result:date=" + predate);
                    final SharedPreferences.Editor editor = sharedPreferences.edit();

                    //日期不同则更新数据库
                    List<String> list1 = new ArrayList<String>();
                    RateManager rm = new RateManager(RateListActivity.this);
                    if (!d.equals(predate)) {
                        Log.i(TAG,"UPDATE");
                        for (int i = 0; i < 162; i += 6) {
                            String ss = bdl.getString("item" + i);
                            list1.add(ss);
                        }

                        //一个个item
                        for (int i = 0; i < 162; i += 6) {
                            HashMap<String, String> map = new HashMap<String,
                                    String>();
                            String ss = bdl.getString("item" + i);
                            String[] ratelist = ss.split("==>");
                            String rate = ratelist[0];
                            String detail = ratelist[1];

                            RateItem item = new RateItem();
                            item.setId(i/6);
                            item.setCurName(rate);
                            item.setCurRate(detail);
                            rm.add(item);
                            Log.i(TAG,String.valueOf(i/6));

                            map.put("ItemTitle", rate); // 货币名称
                            map.put("ItemDetail", detail); // 数据
                            listItems.add(map);
                        }
                        editor.putString("date",d);
                        editor.commit();
                        Log.i(TAG,"update success");
                    }else{
                        //日期相同则读取数据库
                        RateItem item = new RateItem();
                        for(int i=1;i<28;i++){
                            HashMap<String, String> map = new HashMap<String,
                                    String>();
                            Log.i(TAG,""+i);
                            item = rm.findById(i);
                            String rate = item.getCurRate();
                            String name = item.getCurName();
                            Log.i(TAG,name);
                            map.put("ItemTitle", rate); // 货币名称
                            map.put("ItemDetail", name); // 数据
                            listItems.add(map);
                        }
                        Log.i(TAG,"datas from database");
                    }

                    //自定义adapter
                    MyAdapter myAdapter = new MyAdapter(RateListActivity.this,
                            R.layout.activity_rate_list,
                            listItems);
                    setListAdapter(myAdapter);

                    getListView().setOnItemClickListener(new AdapterView.OnItemClickListener(){
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id){
                            Object itemAtPosition = getListView().getItemAtPosition(position);
                            HashMap<String, String> map = (HashMap<String, String>) itemAtPosition;
                            titleStr = map.get("ItemTitle");
                            detailStr = map.get("ItemDetail");
                            Log.i(TAG, "onItemClick: titleStr=" + titleStr);
                            Log.i(TAG, "onItemClick: detailStr=" + detailStr);
                            openmain3(view);
                            //另一种方法
//                            TextView title = (TextView) view.findViewById(R.id.itemTitle);
//                            TextView detail = (TextView) view.findViewById(R.id.itemDetail);
//                            String title2 = String.valueOf(title.getText());
//                            String detail2 = String.valueOf(detail.getText());
//                            Log.i(TAG, "onItemClick: title2=" + title2);
//                            Log.i(TAG, "onItemClick: detail2=" + detail2);
                        }

                        private void openmain3(View view) {
                            Intent main3 = new Intent(RateListActivity.this,Activity3.class);
                            main3.putExtra("detail",detailStr);
                            main3.putExtra("title",titleStr);
                            Log.i(TAG,""+titleStr);
                            Log.i(TAG,""+detailStr);
                            startActivityForResult(main3, 2);
                        }
                    });
                    // 生成适配器的 Item 和动态数组对应的元素
//                    listItemAdapter = new SimpleAdapter(RateListActivity.this,
//                            listItems, // listItems 数据源
//                            R.layout.activity_rate_list, // ListItem 的 XML 布局实现
//                            new String[] { "ItemTitle", "ItemDetail" },
//                            new int[] { R.id.itemTitle, R.id.itemDetail }
//                    );
//                    setListAdapter(listItemAdapter);
//                    Bundle bdl = msg.getData();
//                    List<String> list1 = new ArrayList<String>();
//                    for(int i=0;i<162;i+=6){
//                        String ss = bdl.getString("item"+i);
//                        list1.add(ss);
//                    }
//
//                    ListAdapter adapter = new ArrayAdapter<String>(
//                            RateListActivity.this,
//                            android.R.layout.simple_list_item_1,
//                            list1);
//                    setListAdapter(adapter);
                }
                super.handleMessage(msg);
            }
        };

        //开启子线程
        RateListActivity.myThread t = new RateListActivity.myThread();
        new Thread(t).start();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    //后台线程访问网络
    public class myThread implements Runnable{
        @Override
        public void run() {
            String url = "https://www.usd-cny.com/bankofchina.htm";
            String date1;

            try {
                //解析html文件
                Document doc = Jsoup.connect(url).get();
                Log.i(TAG,"run:" + doc.title());
                //读取table内容
                Elements tables = doc.getElementsByTag("table");
                Element table6 = tables.get(0);
                Elements tds = table6.getElementsByTag("td");

                //读取时间
                Elements date = doc.getElementsByClass("time");
                date1 = date.text();

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
                bdl.putString("date",date1);
                msg.setData(bdl);
                msg.sendToTarget();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}

class MyAdapter extends ArrayAdapter{

    private static final String TAG = "MyAdapter";

    public MyAdapter(Context context,
                     int resource,
                     ArrayList<HashMap<String,String>> list) {
        super(context, resource, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if(itemView == null){
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.activity_rate_list,parent,false);
        }
        Map<String,String> map = (Map<String, String>) getItem(position);
        TextView title = (TextView) itemView.findViewById(R.id.itemTitle);
        TextView detail = (TextView) itemView.findViewById(R.id.itemDetail);
        title.setText(map.get("ItemTitle"));
        detail.setText(map.get("ItemDetail"));
        return itemView;
    }

}