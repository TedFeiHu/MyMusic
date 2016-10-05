package com.ted.mymusic.com.ted.mymusic.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Path;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;

/**
 * Created by Hu131 on 2016/10/6.
 */

public class MusicService extends Service implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private MediaPlayer player;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String option = intent.getStringExtra("option");
        if ("播放".equals(option)){
            String path = intent.getStringExtra("path");
            play(path);
        }else if ("暂停".equals(option)){
            pause();
        }else if ("继续".equals(option)){
            continuePlay();
        }else if ("停止".equals(option)){
            stopPlay();
        }


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() { //多次启动service只会启动以此
        player = new MediaPlayer();
        //设置监听器
        player.setOnErrorListener(this);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        super.onCreate();
    }

    /*封装音乐播放器常用的方法----begin*/
    public void play(String path) {

        try {
            player.reset(); //idle
            player.setDataSource(path);// 设置歌曲的路劲
            player.prepare(); //开始准备，本地音乐使用同步的就可以了
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        if (player != null && player.isPlaying()) {
            player.pause();
        }
    }

    public void continuePlay() {
        if (player != null && !player.isPlaying()) {
            player.start();
        }
    }

    public void stopPlay() {
        if (player != null) {
            player.stop();
        }
    }
    /*封装音乐播放器常用的方法----end*/

    //相关的回调方法
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }
}
