package com.jsvana.music;

import android.app.Activity;
import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;

import com.jsvana.music.songs.PlayedSong;
import com.jsvana.music.songs.SongManager;

public class MainActivity extends Activity {
    private static final String TAG = "Music";

    private static final int PREFERENCES_ACT = 0;

    private SongManager songManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar bar = getActionBar();
        bar.setTitle("Recommendations");

        GridView gridview = (GridView) findViewById(R.id.grid);
        ImageAdapter adapter = new ImageAdapter(this);
        adapter.addItem(new Recommendation("III", "Billy Talent", R.drawable.billytalent));
        gridview.setAdapter(adapter);

        IntentFilter iF = new IntentFilter();
        iF.addAction("com.android.music.metachanged");
        iF.addAction("com.android.music.playstatechanged");

        songManager = new SongManager();

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                String action = intent.getAction();
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    if ("com.android.music.playstatechanged".equals(action)) {
                        if (songManager.hasCurrentSong()) {
                            if (bundle.getBoolean("playing", false)) {
                                songManager.resume();
                            } else {
                                songManager.pause();
                            }
                        } else {
                            addSong(bundle);
                        }
                    } else if ("com.android.music.metachanged".equals(action)) {
                        addSong(bundle);
                    }
                }
            }
        };

        registerReceiver(receiver, iF);
    }

    public void addSong(Bundle bundle) {
        long duration = bundle.getLong("duration", 0);
        String artist = bundle.getString("artist", "");
        String album = bundle.getString("album", "");
        String track = bundle.getString("track", "");
        long albumID = bundle.getLong("albumId", 0);
        if (songManager.addSong(duration, artist, album, track, albumID)) {
            // TODO: Send data to server
            if (songManager.hasPreviousSong()) {
                PlayedSong song = songManager.getPreviousSong();
                Log.v(TAG, "Song was " + (song.getSkipped() ? "" : "not ") + "skipped");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_preferences:
                Intent i = new Intent(this, PreferencesActivity.class);
                startActivityForResult(i, PREFERENCES_ACT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
