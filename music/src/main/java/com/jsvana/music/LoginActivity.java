package com.jsvana.music;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
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
public class LoginActivity extends Activity {
    private static final String TAG = "jsvana.Music";

    private static final int CREATE_ACCOUNT_ACT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getActionBar().hide();

        SharedPreferences sp = getSharedPreferences(TAG, MODE_PRIVATE);
        EditText email = (EditText)findViewById(R.id.email);
        email.setText(sp.getString("email", ""));
        CheckBox savePassword = (CheckBox)findViewById(R.id.savePassword);
        savePassword.setChecked(sp.getBoolean("savePassword", false));
        if (sp.getBoolean("savePassword", false)) {
            EditText password = (EditText)findViewById(R.id.password);
            password.setText(sp.getString("password", ""));
        }
    }

    public void onLogin(View view) {
        final String email = ((EditText)findViewById(R.id.email)).getText().toString();
        final String password = ((EditText)findViewById(R.id.password)).getText().toString();

        if ("".equals(email) || "".equals(password)) {
            return;
        }

        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("password", password);
        SuggestQuery sq = new SuggestQuery("/users/login", params, new Callback() {
            @Override
            public void callback(int status, String response) {
                boolean success = true;
                if (status != 200) {
                    success = false;
                } else {
                    try {
                        JSONObject json = new JSONObject(response);
                        int token = json.getInt("token");
                        SharedPreferences.Editor e = getSharedPreferences(TAG, MODE_PRIVATE).edit();
                        e.putInt("token", token);
                        e.commit();
                    } catch (JSONException e) {
                        success = false;
                    }
                }

                if (success) {
                    openMain(email, password);
                } else {
                    // TODO: Show error message
                }
            }
        });

        findViewById(R.id.loggingIn).setVisibility(View.VISIBLE);
        sq.execute();
        openMain(email, password);
    }

    public void openMain(String email, String password) {
        SharedPreferences.Editor e = getSharedPreferences(TAG, MODE_PRIVATE).edit();
        CheckBox savePassword = (CheckBox)findViewById(R.id.savePassword);
        e.putString("email", email);
        if (savePassword.isChecked()) {
            e.putString("password", password);
            e.putBoolean("savePassword", true);
        }
        e.commit();
        findViewById(R.id.loggingIn).setVisibility(View.INVISIBLE);
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void onCreateAccount(View view) {
        Intent i = new Intent(this, CreateAccountActivity.class);
        Bundle b = new Bundle();
        String email = ((EditText)findViewById(R.id.email)).getText().toString();
        String password = ((EditText)findViewById(R.id.password)).getText().toString();
        b.putString("email", email);
        b.putString("password", password);
        i.putExtras(b);
        startActivityForResult(i, CREATE_ACCOUNT_ACT);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_ACCOUNT_ACT && resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                EditText email = (EditText)findViewById(R.id.email);
                email.setText(data.getExtras().getString("email", ""));
                EditText password = (EditText)findViewById(R.id.password);
                password.setText(data.getExtras().getString("password", ""));
            }
        }
    }
}
