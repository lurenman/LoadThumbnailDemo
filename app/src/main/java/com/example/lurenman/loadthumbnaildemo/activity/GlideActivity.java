package com.example.lurenman.loadthumbnaildemo.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lurenman.loadthumbnaildemo.R;

/**
 * @author: baiyang.
 * Created on 2018/1/8.
 */

public class GlideActivity extends BaseActivity {
    private static final String TAG = "GlideActivity";
    private ImageView iv_img;
    private String imageUrl;

    @NonNull
    @Override
    protected String getActionBarTitle() {
        return "Glide加载缩略图";
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_glide);
        iv_img = (ImageView) findViewById(R.id.iv_img);
    }

    @Override
    protected void initVariables() {
        imageUrl = "http://file.ataw.cn/HospPerformance/Model/Image/2017/06/20/File/20170620173507137A9A7CC4BD991149058A765A34095728CF.jpg?ut=20170620173516";
    }

    @Override
    protected void loadData() {
        Glide.with(this).load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//图片缓存模式
                .skipMemoryCache(true)//跳过缓存方便测试
                .thumbnail(0.1f).into(iv_img);
    }
}
