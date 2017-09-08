package com.Example.iJam.network;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Mostafa on 7/6/2015.
 */
public class HttpPostTask extends AsyncTask<JSONObject, Void, String> {

    protected Context ctx;
    protected String api_url;
    public HttpPostTask(String api_url, Context ctx){
        this.api_url = api_url;
        this.ctx = ctx;
    }
    @Override
    protected String doInBackground(JSONObject... params) {
        try {
            URL url = new URL(api_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("POST");

            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(params[0].toString());
            writer.flush();
            writer.close();

            //SERVER RESPONSE
            StringBuilder builder = new StringBuilder();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String line;
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
