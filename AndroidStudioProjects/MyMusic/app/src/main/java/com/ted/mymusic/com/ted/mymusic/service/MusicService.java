package com.ted.mymusic.com.ted.mymusic.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Path;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ted.mymusic.com.ted.mymusic.utils.Constants;
import com.ted.mymusic.com.ted.mymusic.utils.L;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Hu131 on 2016/10/6.
 */

public class MusicService extends Service implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private MediaPlayer player;
    private Messenger mMessenger;
    private Timer mTimer;
    private TimerTask mTask;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String option = intent.getStringExtra("option");

        if ("播放".equals(option)) {
            String path = intent.getStringExtra("path");
            play(path);
        } else if ("暂停".equals(option)) {
            pause();
        } else if ("继续".equals(option)) {
            continuePlay();
        } else if ("停止".equals(option)) {
            stopPlay();
        }else if ("进度".equals(option)){
            int progress = intent.getIntExtra("progress",-1);
            seekPlayer(progress);
        }

        if ("handler".equals(option)) {
            L.e("进入option==handler");
            if (mMessenger == null) {
                mMessenger = (Messenger) intent.getExtras().get("messenger");
            }
            setProgress();
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

    public void setProgress() {

        if (mTimer == null){
            mTimer = new Timer();
        }
        mTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    int currentPosition = player.getCurrentPosition();
                    Message msg = Message.obtain();
                    msg.what = Constants.HandleMsg.SEEKBAR_WHAT;
                    msg.arg1 = currentPosition;
                    msg.arg2 = player.getDuration();
                    mMessenger.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        mTimer.schedule(mTask,0,1000);
    }

    public void seekPlayer(int progress){
        if (player!=null&&player.isPlaying()){
            player.seekTo(progress);
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
        if (mMessenger != null) {
            mTask.cancel();
            setProgress();
        }

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }
}
