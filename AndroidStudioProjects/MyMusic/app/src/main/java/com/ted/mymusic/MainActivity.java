package com.ted.mymusic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.ted.mymusic.com.ted.mymusic.adapter.MusicListViewAdapter;
import com.ted.mymusic.com.ted.mymusic.utils.L;

import java.lang.reflect.Method;

/**
 * HOME TEST (GIT)
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }

    void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("无限音乐");
        toolbar.setSubtitle("wuxianedu Pro Music");
        //   toolbar.inflateMenu(R.menu.toolbar_base);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new MyMenuClickListener());


        ListView listView = (ListView) findViewById(R.id.music_list_view);
        listView.setAdapter(new MusicListViewAdapter(this));
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
