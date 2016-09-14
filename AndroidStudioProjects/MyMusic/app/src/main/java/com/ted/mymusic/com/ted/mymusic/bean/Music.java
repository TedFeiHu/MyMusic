package com.ted.mymusic.com.ted.mymusic.bean;

/**
 * 音乐bean
 * Created by TED on 2016/9/14.
 */
public class Music {
    public String title; // 歌名
    public String artist; // 歌手
    public String path; //路径
    public int duration; //时长

    public Music(String title, String artist, String path, int duration) {
        this.title = title;
        this.artist = artist;
        this.path = path;
        this.duration = duration;
    }
}
