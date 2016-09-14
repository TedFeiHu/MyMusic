package com.ted.mymusic.com.ted.mymusic.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.ted.mymusic.com.ted.mymusic.bean.Music;

import java.util.ArrayList;
import java.util.List;

/**
 * 媒体工具类
 * Created by TED on 2016/9/14.
 */
public class MediaUtils {
    public static List<Music> songList = new ArrayList<>();

    public static void initSongList(Context context){
        songList.clear();
        L.e("------------------------");
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI; // uri
        String[] projection = {MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DURATION};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        assert cursor != null;
        while(cursor.moveToNext()){
            String title = cursor.getString(cursor.getColumnIndex(projection[0]));
            String artist = cursor.getString(cursor.getColumnIndex(projection[1]));
            String path = cursor.getString(cursor.getColumnIndex(projection[2]));
            int duration = cursor.getInt(cursor.getColumnIndex(projection[3]));

            Music music = new Music(title, artist, path, duration);
            songList.add(music);
        }
        cursor.close();
    }
}
