package com.jthou.flowers;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ViewGroup content = (ViewGroup) findViewById(android.R.id.content);
        final RelativeLayout layout = (RelativeLayout) content.getChildAt(0);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                HeartView view = new HeartView(MainActivity.this);
                layout.addView(view);
                handler.postDelayed(this, 500);
            }
        }, 500);

//        ViewGroup content = (ViewGroup) findViewById(android.R.id.content);
//        final RelativeLayout layout = (RelativeLayout) content.getChildAt(0);
//
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ImageView child = new ImageView(MainActivity.this);
//                child.setImageBitmap(BitmapUtil.createHeart(MainActivity.this));
//                layout.addView(child);
//                handler.postDelayed(this, 500);
//            }
//        }, 1000);
    }

}
