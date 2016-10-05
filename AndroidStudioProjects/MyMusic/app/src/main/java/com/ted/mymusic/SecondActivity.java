package com.ted.mymusic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ted.mymusic.com.ted.mymusic.bean.Music;
import com.ted.mymusic.com.ted.mymusic.utils.MediaUtils;

/**
 * 歌词界面
 * Created by Hu131 on 2016/10/5.
 */
public class SecondActivity extends AppCompatActivity{

    private Music music;
    private ImageView background;
    private TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        initView();
        initBackground();  //初始化背景，沉浸式状态栏

    }

    /**
     * 初始化背景，沉浸式状态栏
     */
    private void initBackground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        Intent intent = getIntent();
        int currentMusic = intent.getIntExtra("current_music", -1);
        if (currentMusic != -1){
            music = MediaUtils.songList.get(currentMusic);
            Bitmap bitmap = MediaUtils.fastblur(music.albumArt, 30);
            Drawable drawable =new BitmapDrawable(bitmap);
            background.setBackgroundDrawable(drawable);
        }

        title.setText(music.title);
    }


    private void initView() {
        background = (ImageView) findViewById(R.id.second_iv_background);
        title = (TextView) findViewById(R.id.second_title);

    }
}
