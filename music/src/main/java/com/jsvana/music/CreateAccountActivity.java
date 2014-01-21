package com.jsvana.music;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.jsvana.music.http.Callback;
import com.jsvana.music.http.SuggestQuery;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jsvana on 19/1/14.
 */
public class CreateAccountActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        getActionBar().hide();

        Bundle b = getIntent().getExtras();
        if (b != null) {
            ((EditText)findViewById(R.id.email)).setText(b.getString("email", ""));
            ((EditText)findViewById(R.id.password)).setText(b.getString("password", ""));
        }
    }

    public void onCreateAccount(View view) {
        final String email = ((EditText)findViewById(R.id.email)).getText().toString();
        final String password = ((EditText)findViewById(R.id.password)).getText().toString();

        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("password", password);
        SuggestQuery sq = new SuggestQuery("/users/new", params, new Callback() {
            @Override
            public void callback(int status, String response) {
                boolean success = true;
                if (status != 200) {
                    success = false;
                } else {
                    try {
                        JSONObject json = new JSONObject(response);
                        if (!json.getBoolean("result")) {
                            success = false;
                            closeActivity(email, password);
                        }
                    } catch (JSONException e) {
                        success = false;
                    }
                }

                if (success) {
                    closeActivity(email, password);
                } else {
                    // TODO: Add error message
                }
            }
        });

        findViewById(R.id.loading).setVisibility(View.VISIBLE);
        sq.execute();
    }

    public void closeActivity(String email, String password) {
        findViewById(R.id.loading).setVisibility(View.INVISIBLE);

        Intent i = new Intent(this, LoginActivity.class);
        Bundle b = new Bundle();
        b.putString("email", email);
        b.putString("password", password);
        i.putExtras(b);
        setResult(RESULT_OK, i);
        finish();
    }
}
