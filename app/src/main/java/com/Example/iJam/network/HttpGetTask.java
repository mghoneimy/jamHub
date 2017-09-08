package com.Example.iJam.network;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Mostafa on 7/8/2015.
 */
public class HttpGetTask extends AsyncTask<Void, Void, String> {
    protected Context ctx;
    protected String api_url;
    public HttpGetTask(String api_url, Context ctx){
        this.api_url = api_url;
        this.ctx = ctx;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            URL url = new URL(api_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setRequestProperty("Accept", "application/json");

            StringBuilder builder = new StringBuilder();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    if(line.charAt(0)!='<')
                        builder.append(line + "\n");
                }
                reader.close();
                return builder.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
