package com.ted.mymusic.com.ted.mymusic.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ted.mymusic.R;
import com.ted.mymusic.com.ted.mymusic.bean.Music;
import com.ted.mymusic.com.ted.mymusic.utils.MediaUtils;

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
        if (MediaUtils.songList.size()!=0){
            return MediaUtils.songList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (MediaUtils.songList.size()!=0){
            return MediaUtils.songList.get(position);
        }
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
            convertView = View.inflate(context, R.layout.item_music_lv, null);
            holder = new Holder();
            holder.title = (TextView) convertView.findViewById(R.id.music_list_item_title);
            holder.artist = (TextView) convertView.findViewById(R.id.music_list_item_artist);
            holder.duration = (TextView) convertView.findViewById(R.id.music_list_item_duration);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }
        Music music = MediaUtils.songList.get(position);

        holder.title.setText(music.title);
        holder.artist.setText(music.artist);

        holder.duration.setText(music.duration+"");
        return convertView;
    }

    class Holder {
        TextView title, artist, duration;
    }
}
