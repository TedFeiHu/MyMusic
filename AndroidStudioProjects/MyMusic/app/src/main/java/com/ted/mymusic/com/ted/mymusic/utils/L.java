package com.ted.mymusic.com.ted.mymusic.utils;

import android.util.Log;

/**
 * log工具类
 * Created by TED on 2016/9/12.
 */
public class L {
    private  static  boolean  FLAG = true; //是否开启日志,false 不开启
    private static String  TAG = "ted----log";
    public static void e(String text){
        Log.e(TAG, "e: "+text);
    }
}
