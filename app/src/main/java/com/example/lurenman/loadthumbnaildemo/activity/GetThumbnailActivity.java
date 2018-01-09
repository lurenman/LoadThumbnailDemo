package com.example.lurenman.loadthumbnaildemo.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.lurenman.loadthumbnaildemo.R;
import com.example.lurenman.loadthumbnaildemo.SMApp;
import com.example.lurenman.loadthumbnaildemo.dialog.SelectPicDialog;
import com.example.lurenman.loadthumbnaildemo.utils.BitmapUtil;
import com.example.lurenman.loadthumbnaildemo.utils.FileUtil;
import com.example.lurenman.loadthumbnaildemo.utils.MessageEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * @author: baiyang.
 * Created on 2018/1/8.
 * 参考
 * http://www.binkery.com/archives/401.html
 */

public class GetThumbnailActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    private static final String TAG = "GetThumbnailActivity";
    private String mSdFileAbsolutePath = SMApp.getContext().getExternalFilesDir(null).getAbsolutePath();
    private static final int PERMISSION_CODE = 200;
    private Button bt_getAll;
    private Button bt_gallery;
    private ImageView iv_getAll;
    private ImageView iv_gallery;

    private SelectPicDialog mSelectPicDialog;

    @NonNull
    @Override
    protected String getActionBarTitle() {
        return "GetThumbnail";
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_get_thumbnail);
        EventBus.getDefault().register(this);
        bt_getAll = (Button) findViewById(R.id.bt_getAll);
        bt_gallery = (Button) findViewById(R.id.bt_gallery);
        iv_getAll = (ImageView) findViewById(R.id.iv_getAll);
        iv_gallery = (ImageView) findViewById(R.id.iv_gallery);
        //核实权限
        checkPermissions();

    }

    private void checkPermissions() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            initSomeThing();
        } else {
            EasyPermissions.requestPermissions(this, "需要读取SD卡权限", PERMISSION_CODE, Manifest.permission.READ_EXTERNAL_STORAGE
                    , Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    private void initSomeThing() {
        bt_getAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<HashMap<String, String>> allPictures = BitmapUtil.getAllPictures(getApplicationContext());
                if (allPictures != null && !allPictures.isEmpty()) {
                    Toast.makeText(getApplicationContext(), allPictures.size() + "", Toast.LENGTH_SHORT).show();
                    HashMap<String, String> stringStringHashMap = allPictures.get(0);
                    String thumbnail_path = stringStringHashMap.get("thumbnail_path");
                    //拿到集合中的第一个缩略图
                    Glide.with(mContext).load(thumbnail_path).into(iv_getAll);
                }

            }
        });
        bt_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectPicDialog == null) {
                    mSelectPicDialog = new SelectPicDialog(GetThumbnailActivity.this);
                    mSelectPicDialog.setItemClickListener(new SelectPicDialog.SelectPicItemClickListener() {
                        @Override
                        public void clickTakePhoto() {
                            Toast.makeText(mContext, "clickTakePhoto", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void clickGoAlbum() {
                            Intent intent = new Intent(mContext, AlbumActivity.class);
                            startActivity(intent);
                        }
                    });
                    mSelectPicDialog.show();
                } else {
                    mSelectPicDialog.show();
                }
            }
        });
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiverAlbumMsg(MessageEntity msg) {
        if (msg.mWhat == 100) {
            String sourPath = (String) msg.mMsg;
            Log.e(TAG, "receiverAlbumMsg sourPath: " + sourPath);
            File sourPathFile = new File(sourPath);
            try {
                long sourPathFileSize = FileUtil.getFileSize(sourPathFile);
                String sourPathFileformetSize = FileUtil.FormetFileSize(sourPathFileSize);
                Log.e(TAG, "receiverAlbumMsg: sourPathFileformetSize:" + sourPathFileformetSize);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //这块通过图片sourPathpath查找缩略图的路径
            String thumbnailPath = BitmapUtil.queryImageThumbnailByPath(mContext, sourPath);
            Log.e(TAG, "receiverAlbumMsg thumbnailPath: " + thumbnailPath);
            if (TextUtils.isEmpty(thumbnailPath)) {
                Toast.makeText(mContext, "没有获取到缩略图,我们将生成缩略图", Toast.LENGTH_SHORT).show();
                //生成缩略图
                makeThumbnail(sourPath);
            } else {
                //通过缩略图路径加载缩略图
                getThumbnail(sourPath, thumbnailPath);
            }
        }
    }

    /**
     * 生成缩略图
     *
     * @param sourPath
     */
    private void makeThumbnail(String sourPath) {
        Bitmap sourBitmap = BitmapFactory.decodeFile(sourPath);
        Bitmap thumBitmap = BitmapUtil.extractMiniThumb(sourBitmap, 200, 200);
        try {
            File thumFile = BitmapUtil.saveFile(thumBitmap, mSdFileAbsolutePath, "ceshi");
            long thumFileSize = FileUtil.getFileSize(thumFile);
            String thumFileFormetSize = FileUtil.FormetFileSize(thumFileSize);
            Log.e(TAG, "makeThumbnail: thumFileFormetSize:" + thumFileFormetSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        thumBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        Glide.with(mContext).load(bytes).into(iv_gallery);
    }

    /**
     * 通过缩略图路径加载缩略图
     *
     * @param thumbnailPath
     */
    private void getThumbnail(String sourPath, String thumbnailPath) {
        File thumFile = new File(thumbnailPath);
        try {
            long thumFileSize = FileUtil.getFileSize(thumFile);
            //如果thumFileSize等于0代表无效的路径，我们就生成缩略图
            if (thumFileSize == 0) {
                makeThumbnail(sourPath);
                return;
            }
            String thumFileFormetSize = FileUtil.FormetFileSize(thumFileSize);
            Log.e(TAG, "getThumbnail: thumFileFormetSize:" + thumFileFormetSize);
        } catch (Exception e) {
            e.printStackTrace();

        }
        Glide.with(mContext).load(thumbnailPath).into(iv_gallery);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode == PERMISSION_CODE) {
            if (perms.contains(Manifest.permission.READ_EXTERNAL_STORAGE)
                    && perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(mContext, "获取权限成功", Toast.LENGTH_SHORT).show();
                initSomeThing();
            }
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (requestCode == PERMISSION_CODE) {
            if (perms.contains(Manifest.permission.READ_EXTERNAL_STORAGE)
                    && perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(mContext, "没有读取SD卡权限，请手动授予", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
