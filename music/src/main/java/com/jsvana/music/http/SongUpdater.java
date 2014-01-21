package com.jsvana.music.http;

import android.os.AsyncTask;

import com.jsvana.music.songs.PlayedSong;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jsvana on 20/1/14.
 */
public class SongUpdater extends AsyncTask<String, Void, String> {
    private static final String LOGIN_URL = "replace me";

    private PlayedSong song;
    private Callback after;

    public SongUpdater(PlayedSong song, Callback after) {
        this.song = song;
        this.after = after;
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(LOGIN_URL);

        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        params.add(new BasicNameValuePair("date", sdf.format(date)));
        sdf = new SimpleDateFormat("HH:mm");
        params.add(new BasicNameValuePair("time", sdf.format(date)));
        params.add(new BasicNameValuePair("albumID", Long.valueOf(song.getAlbumID()).toString()));
        params.add(new BasicNameValuePair("skipped", (song.getSkipped() ? "true" : "false")));
        if (song.getSkipped()) {
            params.add(new BasicNameValuePair("skipLen",
                Long.valueOf(song.getDuration() - song.getElapsed()).toString()));
        }

        try {
            post.setEntity(new UrlEncodedFormEntity(params));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }

        try {
            HttpResponse response = httpClient.execute(post);

            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();

            String ret = "";

            if (statusCode == 200) { // Ok
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                String line;
                while ((line = rd.readLine()) != null) {
                    ret += line;
                }
            }

            return ret;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } finally {
            return "";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        after.callback(0, "");//"".equals(result), null);
    }
}
