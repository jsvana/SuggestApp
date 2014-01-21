package com.jsvana.music;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by jsvana on 19/1/14.
 */
public class PreferencesActivity extends Activity {
    private static final String TAG = "jsvana.Music";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        getActionBar().setTitle("Preferences");
        getActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences prefs = getSharedPreferences(TAG, MODE_PRIVATE);
        TextView user = (TextView)findViewById(R.id.user);
        user.setText(prefs.getString("email", "Unknown"));
        CheckBox wifiSync = (CheckBox)findViewById(R.id.wifiSync);
        wifiSync.setChecked(prefs.getBoolean("wifiSync", false));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Save changes
                SharedPreferences.Editor e = getSharedPreferences(TAG, MODE_PRIVATE).edit();
                e.putBoolean("wifiSync", ((CheckBox)findViewById(R.id.wifiSync)).isChecked());
                e.commit();

                finish();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
