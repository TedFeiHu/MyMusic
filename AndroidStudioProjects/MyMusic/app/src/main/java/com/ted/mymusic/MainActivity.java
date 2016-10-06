package com.ted.mymusic;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Messenger;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ted.mymusic.com.ted.mymusic.adapter.MusicListViewAdapter;
import com.ted.mymusic.com.ted.mymusic.bean.Music;
import com.ted.mymusic.com.ted.mymusic.service.MusicService;
import com.ted.mymusic.com.ted.mymusic.utils.Constants;
import com.ted.mymusic.com.ted.mymusic.utils.L;
import com.ted.mymusic.com.ted.mymusic.utils.MediaUtils;

import java.lang.reflect.Method;

/**
 * HOME TEST (GIT)
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ListView mListView;
    private Toolbar toolbar;
    private TextView center;
    private ImageView mBtmArt;
    private View mInclude;
    private TextView mBtmTitle;
    private TextView mBtmArtist;
    private ImageButton mBtmPlay;
    private MusicListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        // 权限判断---
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //申请WRITE_EXTERNAL_STORAGE权限
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Constants.Permission.WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
            } else {
                initData();
            }
        }
        //----
        initListener();

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        BroadcastReceiver receiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                L.e("接收到广播");
                updateUI();
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.BroadCastAction.UPDATE_UI);
        manager.registerReceiver(receiver, filter);
    }

    /**
     * 更新ui
     */
    public void updateUI(){
        adapter.notifyDataSetChanged();
        Music music = MediaUtils.songList.get(MediaUtils.CUR_Music);
        mBtmArt.setImageBitmap(music.albumArt);
        mBtmTitle.setText(music.title);
        mBtmArtist.setText(music.artist);
        if (MediaUtils.CUR_STATUS == Constants.Music.MUSIC_PLAY){
            mBtmPlay.setImageResource(R.mipmap.minibar_btn_pause_normal);
        }
        if (MediaUtils.CUR_STATUS == Constants.Music.MUSIC_PAUSE){
            mBtmPlay.setImageResource(R.drawable.bottom_play_layer);
        }
    }

    //点击权限窗口调用
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.Permission.WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                L.e("--------11111-------------");
                initData();
            } else {
                // Permission Denied
                L.e("------------222---------");
            }
        }
    }

    void initView() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("无限音乐");
        toolbar.setTitleTextColor(0xffffffff);
        toolbar.setSubtitle("wuxianedu Pro Music");
        toolbar.setSubtitleTextColor(0xffffffff);
        //   toolbar.inflateMenu(R.menu.toolbar_base);
        setSupportActionBar(toolbar);

        mListView = (ListView) findViewById(R.id.music_list_view);
        center = (TextView) findViewById(R.id.music_content_id);

        mInclude = findViewById(R.id.include_layout);
        mBtmArt = (ImageView) findViewById(R.id.bottom_art);
        mBtmTitle = (TextView) findViewById(R.id.bottom_title);
        mBtmArtist = (TextView) findViewById(R.id.bottom_artist);
        mBtmPlay = (ImageButton) findViewById(R.id.bottom_play);

        if (MediaUtils.CUR_Music !=-1){
            Music music = MediaUtils.songList.get(MediaUtils.CUR_Music);
            mInclude.setVisibility(View.VISIBLE);
            mBtmArt.setImageBitmap(music.albumArt);
            mBtmTitle.setText(music.title);
            mBtmArtist.setText(music.artist);
            if (MediaUtils.CUR_STATUS == Constants.Music.MUSIC_PLAY){
                mBtmPlay.setImageResource(R.mipmap.minibar_btn_pause_normal);
            }else if (MediaUtils.CUR_STATUS == Constants.Music.MUSIC_PAUSE){
                mBtmPlay.setImageResource(R.drawable.bottom_play_layer);
            }
        }
    }

    private void initData() {

        MediaUtils.initSongList(this);

        adapter = new MusicListViewAdapter(this);
        mListView.setAdapter(adapter);

        if (MediaUtils.songList.size() == 0) {
            center.setVisibility(View.VISIBLE);
        }
    }

    private void initListener() {
        toolbar.setOnMenuItemClickListener(new MyMenuClickListener());
        mBtmArt.setOnClickListener(this);
        mListView.setOnItemClickListener(this);
        mBtmPlay.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_base, menu);
        return true;
    }

    // 溢出menu显示图标
    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    L.e(getClass().getSimpleName() + "onMenuOpened...unable to set icons for overflow menu" + e);
                    //   Out.print(getClass().getSimpleName() + "onMenuOpened...unable to set icons for overflow menu" + e);
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.bottom_art:
                intent = new Intent(this, SecondActivity.class);
                intent.putExtra("current_music", MediaUtils.CUR_Music);
                startActivity(intent);
                break;

            case R.id.bottom_play:
                intent = new Intent(MainActivity.this, MusicService.class);
                if (MediaUtils.CUR_STATUS == Constants.Music.MUSIC_PLAY){
                    intent.putExtra("option", "暂停");
                    MediaUtils.CUR_STATUS = Constants.Music.MUSIC_PAUSE;
                    mBtmPlay.setImageResource(R.drawable.bottom_play_layer);
                }else if (MediaUtils.CUR_STATUS == Constants.Music.MUSIC_PAUSE){
                    intent.putExtra("option", "继续");
                    MediaUtils.CUR_STATUS = Constants.Music.MUSIC_PLAY;
                    mBtmPlay.setImageResource(R.mipmap.minibar_btn_pause_normal);
                }
                startService(intent);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        View viewWithTag;
        if (MediaUtils.CUR_Music != -1) {
            viewWithTag = mListView.findViewWithTag(MediaUtils.CUR_Music);
            if (viewWithTag != null) {
                viewWithTag.setVisibility(View.GONE);
            }
        }


        MediaUtils.CUR_Music = position;
        Music music = MediaUtils.songList.get(position);
        mInclude.setVisibility(View.VISIBLE);
        mBtmArt.setImageBitmap(music.albumArt);
        mBtmTitle.setText(music.title);
        mBtmArtist.setText(music.artist);

        viewWithTag = mListView.findViewWithTag(MediaUtils.CUR_Music);
        viewWithTag.setVisibility(View.VISIBLE);

        mBtmPlay.setImageResource(R.mipmap.minibar_btn_pause_normal);
        MediaUtils.CUR_STATUS = Constants.Music.MUSIC_PLAY;

        Intent intent = new Intent(MainActivity.this, MusicService.class);
        intent.putExtra("option", "播放");
        intent.putExtra("path", music.path);
        startService(intent);
    }

    class MyMenuClickListener implements Toolbar.OnMenuItemClickListener {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.local_music_sorted_by_time_action:
                    L.e("按时间排序");
                    break;
                case R.id.local_music_sorted_by_name_action:
                    L.e("按名称排序");
                    break;
                case R.id.local_music_upgrade_song_quality__action:
                    L.e("升级品质");
                    break;
                case R.id.local_music_scan_action:
                    L.e("扫描歌曲");
                    break;
                case R.id.local_cloud_music_action:
                    L.e("本地恢复歌曲");
                    break;
            }
            return true;
        }
    }
}
