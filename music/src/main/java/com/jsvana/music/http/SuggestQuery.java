package com.jsvana.music.http;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jsvana on 20/1/14.
 */
public class SuggestQuery extends AsyncTask<String, Void, String> {
    private static final String BASE_URL = "http://jsvana.io:5001";

    private String endpoint;
    private Map<String, String> params;
    private int status;
    private Callback after;

    public SuggestQuery(String endpoint, Map<String, String> params, Callback after) {
        this.endpoint = endpoint;
        this.params = params;
        this.after = after;
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(BASE_URL + endpoint);

        List<NameValuePair> data = new ArrayList<NameValuePair>(2);
        for (Map.Entry<String, String> param : params.entrySet()) {
            data.add(new BasicNameValuePair(param.getKey(), param.getValue()));
        }

        try {
            post.setEntity(new UrlEncodedFormEntity(data));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return e.getMessage();
        }

        try {
            HttpResponse response = httpClient.execute(post);

            status = response.getStatusLine().getStatusCode();

            String ret = EntityUtils.toString(response.getEntity());
            response.getEntity().consumeContent();

            return ret;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        after.callback(status, result);
    }
}
