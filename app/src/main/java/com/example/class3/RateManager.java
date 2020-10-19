package com.example.class3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RateManager {
    private DBHelper dbHelper;
    private String TBNAME;

    public RateManager(Context context){
        dbHelper = new DBHelper(context);
        TBNAME = dbHelper.TB_NAME;
    }

    public void add(RateItem item){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("curname",item.getCurName());
        values.put("currate",item.getCurRate());

        db.insert(TBNAME,null,values);
        db.close();
    }

    public RateItem findById(int id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TBNAME,null,"ID=?",new String[]{String.valueOf(id)},
                null,null,null);

        RateItem item = null;
        if(cursor!=null && cursor.moveToFirst()){
            item = new RateItem();
            item.setId(cursor.getInt(cursor.getColumnIndex("ID")));
            item.setCurName(cursor.getString(cursor.getColumnIndex("CURNAME")));
            item.setCurRate(cursor.getString(cursor.getColumnIndex("CURRATE")));
            cursor.close();
        }

        db.close();
        return item;
    }
}

class RateItem{

    String Name;
    String Rate;
    int ID;

    public String getCurName() {
        return Name;
    }

    public String getCurRate() {
        return Rate;
    }

    public void setId(int id) {
        ID = id;
    }

    public void setCurName(String curname) {
        Name = curname;
    }

    public void setCurRate(String currate) {
        Rate = currate;
    }
}