package com.ted.mymusic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ted.mymusic.com.ted.mymusic.bean.Music;
import com.ted.mymusic.com.ted.mymusic.service.MusicService;
import com.ted.mymusic.com.ted.mymusic.utils.Constants;
import com.ted.mymusic.com.ted.mymusic.utils.L;
import com.ted.mymusic.com.ted.mymusic.utils.MediaUtils;

/**
 * 歌词界面
 * Created by Hu131 on 2016/10/5.
 */
public class SecondActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private Music music;
    private ImageView background;
    private TextView title;
    private ImageButton mPlay;
    private ImageButton mPre;
    private ImageButton mNext;
    private TextView mPlayedTime;
    private TextView mTotalTime;
    private SeekBar mSeekBar;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.HandleMsg.SEEKBAR_WHAT:

                    mSeekBar.setProgress(msg.arg1);
                    mSeekBar.setMax(msg.arg2);
                    mPlayedTime.setText((msg.arg1 / 60000 < 10 ? "" + 0 + msg.arg1 / 60000 : msg.arg1 / 60000)
                            + ":" +
                            ((msg.arg1 / 1000 % 60) < 10 ? "" + 0 + msg.arg1 / 1000 % 60 : msg.arg1 / 1000 % 60));
                    L.e("收到消息"+msg.arg1+":"+msg.arg2+":"+(MediaUtils.songList.get(MediaUtils.CUR_Music).durationInt));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        initView();
        initBackground();  //初始化背景，沉浸式状态栏
        initListener();
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
        music = MediaUtils.songList.get(currentMusic);
       /* Bitmap bitmap = MediaUtils.fastblur(music.albumArt, 30);
        Drawable drawable = new BitmapDrawable(bitmap);
        background.setBackgroundDrawable(drawable);

        title.setText(music.title);
        if (MediaUtils.CUR_STATUS == Constants.Music.MUSIC_PLAY) {
            mPlay.setBackgroundResource(R.mipmap.second_song_list_pause_normal);
        }
        if (MediaUtils.CUR_STATUS == Constants.Music.MUSIC_PAUSE) {
            mPlay.setBackgroundResource(R.mipmap.second_song_list_play_normal);
        }
        mTotalTime.setText(music.duration);*/
        refresh(music);

        intent = new Intent(SecondActivity.this, MusicService.class);
        intent.putExtra("option", "handler");
        intent.putExtra("messenger", new Messenger(handler));
        startService(intent);
    }


    private void initView() {
        background = (ImageView) findViewById(R.id.second_iv_background);
        title = (TextView) findViewById(R.id.second_title);
        mPlay = (ImageButton) findViewById(R.id.second_song_play);
        mPre = (ImageButton) findViewById(R.id.second_song_pre);
        mNext = (ImageButton) findViewById(R.id.second_song_next);

        mPlayedTime = (TextView) findViewById(R.id.second_played_time);
        mTotalTime = (TextView) findViewById(R.id.second_total_time);
        mSeekBar = (SeekBar) findViewById(R.id.second_seekBar);
    }

    private void initListener() {
        mPlay.setOnClickListener(this);
        mPre.setOnClickListener(this);
        mNext.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(SecondActivity.this, MusicService.class);
        intent.putExtra("messenger", new Messenger(handler));
        switch (v.getId()) {
            case R.id.second_song_play:
                if (MediaUtils.CUR_STATUS == Constants.Music.MUSIC_PLAY) {
                    intent.putExtra("option", "暂停");
                    MediaUtils.CUR_STATUS = Constants.Music.MUSIC_PAUSE;
                    mPlay.setBackgroundResource(R.mipmap.second_song_list_play_normal);
                } else if (MediaUtils.CUR_STATUS == Constants.Music.MUSIC_PAUSE) {
                    intent.putExtra("option", "继续");
                    MediaUtils.CUR_STATUS = Constants.Music.MUSIC_PLAY;
                    mPlay.setBackgroundResource(R.mipmap.second_song_list_pause_normal);
                }
                startService(intent);
                break;
            case R.id.second_song_pre:
                if (MediaUtils.CUR_Music > 0) {
                    MediaUtils.CUR_Music--;
                    intent.putExtra("option", "播放");
                    intent.putExtra("path", MediaUtils.songList.get(MediaUtils.CUR_Music).path);
                    startService(intent);
                    mPlay.setBackgroundResource(R.mipmap.second_song_list_pause_normal);
                    refresh(MediaUtils.songList.get(MediaUtils.CUR_Music));
                } else {
                    Toast.makeText(this, "已经是第一首了", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.second_song_next:
                if (MediaUtils.CUR_Music < MediaUtils.songList.size() - 1) {
                    MediaUtils.CUR_Music++;
                    intent.putExtra("option", "播放");
                    intent.putExtra("path", MediaUtils.songList.get(MediaUtils.CUR_Music).path);
                    startService(intent);
                    mPlay.setBackgroundResource(R.mipmap.second_song_list_pause_normal);
                    refresh(MediaUtils.songList.get(MediaUtils.CUR_Music));
                } else {
                    Toast.makeText(this, "已经是最后一首了", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void refresh(Music music) {
        Bitmap bitmap = MediaUtils.fastblur(music.albumArt, 30);
        Drawable drawable = new BitmapDrawable(bitmap);
        background.setBackgroundDrawable(drawable);

        if (MediaUtils.CUR_STATUS == Constants.Music.MUSIC_PLAY) {
            mPlay.setBackgroundResource(R.mipmap.second_song_list_pause_normal);
        }
        if (MediaUtils.CUR_STATUS == Constants.Music.MUSIC_PAUSE) {
            mPlay.setBackgroundResource(R.mipmap.second_song_list_play_normal);
        }

        title.setText(music.title);
        mTotalTime.setText(music.duration);

        //mSeekBar.setMax(music.durationInt);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Constants.BroadCastAction.UPDATE_UI);
            LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
            manager.sendBroadcast(intent);
            L.e("--------------");
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * seekbar
     *
     * @param seekBar
     * @param progress
     * @param fromUser
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
//        mSeekBar.setProgress(seekBar.getProgress());
        // 希望音乐播放器跳转到指定的地方
        Intent intent = new Intent(SecondActivity.this, MusicService.class);
        intent.putExtra("option","进度");
        intent.putExtra("progress",seekBar.getProgress());
        startService(intent);
    }

}
