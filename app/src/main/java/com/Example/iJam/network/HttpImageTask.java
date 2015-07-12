package com.Example.iJam.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Khodary on 7/11/15.
 */
public class HttpImageTask extends AsyncTask<Bitmap, Void, String> {
    protected Context ctx;
    protected String api_url;
    public HttpImageTask(String api_url, Context ctx){
        this.api_url = api_url;
        this.ctx = ctx;
    }

    @Override
    protected String doInBackground(Bitmap... params) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        params[0].compress(Bitmap.CompressFormat.JPEG, 50, bos);

        final String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(Calendar.getInstance().getTime());
        byte[] data = bos.toByteArray();

        String boundary = "====UPhonebookxsaiIASckasoapsck====";
        String contentDisposition = "Content-Disposition: form-data; name=\"image\"; filename=\"" + timeStamp + ".jpg\"";

        String contentType = "Content-Type: image/jpeg";
        String lineEnd = "\r\n";
        String hyphens = "--";

        try {
            URL url = new URL(api_url);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            connection.setRequestProperty("Accept-Language", "en-US,en;q=0.8,ar;q=0.6");

            connection.setRequestProperty("Cache-Control", "max-age=0");
            connection.setRequestProperty("Connection", "keep-alive");

            connection.setRequestProperty("User-Agent", "Android Multipart HTTP UniversalPhonebook Client 1.0");

            connection.setRequestProperty("enctype", "multipart/form-data");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(hyphens + boundary + lineEnd);
            outputStream.writeBytes(contentDisposition + lineEnd);
            outputStream.writeBytes(contentType + lineEnd);
            outputStream.writeBytes(lineEnd);
            outputStream.write(data);
            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(hyphens + boundary + hyphens + lineEnd);
            outputStream.flush();
            outputStream.close();

            StringBuilder responseBuilder = new StringBuilder();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = buffer.readLine()) != null) {
                if (line.charAt(0) != '<')
                    responseBuilder.append(line + "\n");
                else
                    Log.i("phpWarnings", line);
            }
            buffer.close();

            return responseBuilder.toString().trim();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
