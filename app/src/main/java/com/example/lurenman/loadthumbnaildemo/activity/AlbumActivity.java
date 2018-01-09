package com.example.lurenman.loadthumbnaildemo.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.lurenman.loadthumbnaildemo.R;
import com.example.lurenman.loadthumbnaildemo.adapter.AlbumAdapter;
import com.example.lurenman.loadthumbnaildemo.utils.BitmapUtil;
import com.example.lurenman.loadthumbnaildemo.utils.MessageEntity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * @author: baiyang.
 * Created on 2018/1/8.
 */

public class AlbumActivity extends BaseActivity {

    private GridView album_gv;
    private AlbumAdapter mAlbumAdapter;

    @NonNull
    @Override
    protected String getActionBarTitle() {
        return "相册";
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_album);
        album_gv = (GridView) findViewById(R.id.album_gv);
    }

    @Override
    protected void initVariables() {
        //获取所有图片路径
        List<String> list = BitmapUtil.initAlbumData(getApplicationContext());
        mAlbumAdapter = new AlbumAdapter(list, this);
        album_gv.setAdapter(mAlbumAdapter);

    }

    @Override
    protected void initEnvent() {
        super.initEnvent();
        album_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MessageEntity messageEntity = MessageEntity.obtianMessage();
                messageEntity.mWhat = 100;
                messageEntity.mMsg = mAlbumAdapter.getUrl(position);
                EventBus.getDefault().post(messageEntity);
                AlbumActivity.this.finish();
            }
        });
    }

    @Override
    protected void loadData() {

    }

}
