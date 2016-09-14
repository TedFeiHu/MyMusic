package com.ted.mymusic;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.ted.mymusic.com.ted.mymusic.adapter.MusicListViewAdapter;
import com.ted.mymusic.com.ted.mymusic.utils.Contants;
import com.ted.mymusic.com.ted.mymusic.utils.L;
import com.ted.mymusic.com.ted.mymusic.utils.MediaUtils;

import java.lang.reflect.Method;

/**
 * HOME TEST (GIT)
 */
public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private Toolbar toolbar;
    private TextView center;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //申请WRITE_EXTERNAL_STORAGE权限
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Contants.Permission.WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
            } else {
                initData();
            }
        }
        initListener();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Contants.Permission.WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
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

        listView = (ListView) findViewById(R.id.music_list_view);
        center = (TextView) findViewById(R.id.music_content_id);
    }

    private void initData() {

        MediaUtils.initSongList(this);
        listView.setAdapter(new MusicListViewAdapter(this));

        if (MediaUtils.songList.size() == 0){
            center.setVisibility(View.VISIBLE);
        }
    }

    private void initListener() {
        toolbar.setOnMenuItemClickListener(new MyMenuClickListener());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_base, menu);
        return true;
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
}
