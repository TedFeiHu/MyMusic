package com.ted.mymusic.com.ted.mymusic.bean;

import android.graphics.Bitmap;

/**
 * 音乐bean
 * Created by TED on 2016/9/14.
 */
public class Music {
    public String title; // 歌名
    public String artist; // 歌手
    public String path; //路径
    public int durationInt; //时长
    public Bitmap albumArt; // 图片
    public String duration;

    public Music(String title, String artist, String path, String duration, int durationInt, Bitmap albumArt) {
        this.title = title;
        this.artist = artist;
        this.path = path;
        this.duration = duration;
        this.durationInt = durationInt;
        this.albumArt = albumArt;
    }
}
