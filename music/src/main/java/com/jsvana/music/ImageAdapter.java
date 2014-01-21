package com.jsvana.music;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jsvana on 19/1/14.
 */
public class ImageAdapter extends BaseAdapter {
    private Context context;
    private List<Recommendation> data;

    public ImageAdapter(Context c) {
        context = c;
        data = new ArrayList<Recommendation>();
    }

    public void addItem(Recommendation r) {
        data.add(r);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return data.get(i).hashCode();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View row = convertView;
        Panel holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.maingrid_row, parent, false);

            holder = new Panel();
            holder.album = (TextView) row.findViewById(R.id.album);
            holder.artist = (TextView) row.findViewById(R.id.artist);
            holder.albumArt = (ImageView) row.findViewById(R.id.album_art);
            row.setTag(holder);
        } else {
            holder = (Panel) row.getTag();
        }

        Recommendation item = data.get(i);
        holder.album.setText(item.getAlbum());
        holder.artist.setText(item.getArtist());
        holder.albumArt.setImageResource(item.getArtID());
        return row;
    }

    static class Panel {
        TextView album;
        TextView artist;
        ImageView albumArt;
    }
}
