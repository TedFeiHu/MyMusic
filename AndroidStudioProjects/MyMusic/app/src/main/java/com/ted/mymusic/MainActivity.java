package com.ted.mymusic;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ted.mymusic.com.ted.mymusic.adapter.MusicListViewAdapter;
import com.ted.mymusic.com.ted.mymusic.bean.Music;
import com.ted.mymusic.com.ted.mymusic.utils.Constants;
import com.ted.mymusic.com.ted.mymusic.utils.L;
import com.ted.mymusic.com.ted.mymusic.utils.MediaUtils;

import java.lang.reflect.Method;

/**
 * HOME TEST (GIT)
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    private ListView mListView;
    private Toolbar toolbar;
    private TextView center;
    private ImageView mBtmArt;
    private View mInclude;
    private TextView mBtmTitle;
    private TextView mBtmArtist;

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


        /*测试代码，测试是否拿到专辑图 测试结果，成功，图片为原图，待压缩
        ImageView atrHead = (ImageView) findViewById(R.id.bottom_art);
        atrHead.setImageBitmap(MediaUtils.songList.get(0).albumArt);*/
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

    }

    private void initData() {

        MediaUtils.initSongList(this);
        mListView.setAdapter(new MusicListViewAdapter(this));

        if (MediaUtils.songList.size() == 0) {
            center.setVisibility(View.VISIBLE);
        }
    }

    private void initListener() {
        toolbar.setOnMenuItemClickListener(new MyMenuClickListener());
        mBtmArt.setOnClickListener(this);
        mListView.setOnItemClickListener(this);
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
        switch (v.getId()){
            case R.id.bottom_art:
                Intent intent = new Intent(this, SecondActivity.class);
                intent.putExtra("current_music",MediaUtils.CUR_MUsic);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        View viewWithTag = mListView.findViewWithTag(MediaUtils.CUR_MUsic);
        viewWithTag.setVisibility(View.GONE);

        MediaUtils.CUR_MUsic = position;
        Music music = MediaUtils.songList.get(position);
        mInclude.setVisibility(View.VISIBLE);
        mBtmArt.setImageBitmap(music.albumArt);
        mBtmTitle.setText(music.title);
        mBtmArtist.setText(music.artist);

        viewWithTag = mListView.findViewWithTag(MediaUtils.CUR_MUsic);
        viewWithTag.setVisibility(View.VISIBLE);
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
