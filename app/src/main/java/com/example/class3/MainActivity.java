package com.example.class3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    TextView text1;
    TextView out;
    EditText text2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取控件
        text1 = findViewById(R.id.text1);
        //text1.setText("hello!");

        text2 = findViewById(R.id.text2);
        String str1 = text2.getText().toString();
        String tag;
        Log.i(TAG, "onCreate: msg......");

        out = findViewById(R.id.out);

        Button btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = text2.getText().toString();
                Float f = Float.parseFloat(str);
                out.setText(str+"摄氏度等于"+String.valueOf(f*1.8+32)+"华氏度");
            }
        });

    }

    public void doClick(View view) {
//        out = findViewById(R.id.out);
        out.setText("property");
    }
}
