package com.ted.mymusic.com.ted.mymusic.utils;

/**
 * 常量类
 * Created by TED on 2016/9/14.
 */
public class Constants {
    public static final class Permission{
        public static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 0;
    }

    public  static final class Music{
        public static final int MUSIC_STOP = 1001;
        public static final int MUSIC_PLAY = 1002;
        public static final int MUSIC_PAUSE = 1003;
    }

    public static final class BroadCastAction{
        public static final String UPDATE_UI = "com.wuxianedu.action.update";
    }

    public static final class HandleMsg{
        public static final int SEEKBAR_WHAT = 0;
    }
}
