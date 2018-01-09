package com.example.lurenman.loadthumbnaildemo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.lurenman.loadthumbnaildemo.activity.GetThumbnailActivity;
import com.example.lurenman.loadthumbnaildemo.activity.GlideActivity;

public class MainActivity extends AppCompatActivity {

    private Button bt_glide;
    private Context mContext;
    private Button bt_getThumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        bt_glide = (Button) findViewById(R.id.bt_glide);
        bt_getThumbnail = (Button) findViewById(R.id.bt_getThumbnail);
        initEvents();

    }

    private void initEvents() {
        bt_glide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GlideActivity.class);
                startActivity(intent);
            }
        });
        bt_getThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GetThumbnailActivity.class);
                startActivity(intent);
            }
        });
    }
}
