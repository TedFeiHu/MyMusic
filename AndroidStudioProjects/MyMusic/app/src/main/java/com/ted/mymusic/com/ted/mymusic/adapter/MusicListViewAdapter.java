package com.ted.mymusic.com.ted.mymusic.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ted.mymusic.R;

/**
 * 音乐播放列表
 * Created by Hu131 on 2016/9/12.
 */
public class MusicListViewAdapter extends BaseAdapter {

    Context context;

    public MusicListViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 20;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.music_list_item, null);
            holder = new Holder();
            holder.name = (TextView) convertView.findViewById(R.id.music_list_item_name);
            holder.singer = (TextView) convertView.findViewById(R.id.music_list_item_singer);
            holder.time = (TextView) convertView.findViewById(R.id.music_list_item_time);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }


        return convertView;
    }

    class Holder {
        TextView name, singer, time;
    }
}
