package com.Example.iJam.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by ahmed on 6/30/15.
 */
public class NetworkManager {
	private static NetworkManager instance;
	private RequestQueue requestQueue;
	private ImageLoader imageLoader;
	private static Context context;

	private NetworkManager(Context context) {
		this.context = context;
		requestQueue = getRequestQueue();

		imageLoader = new ImageLoader(requestQueue,
				new ImageLoader.ImageCache() {
					private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);

					@Override
					public Bitmap getBitmap(String url) {
						return cache.get(url);
					}

					@Override
					public void putBitmap(String url, Bitmap bitmap) {
						cache.put(url, bitmap);
					}
				});
	}

	public static synchronized NetworkManager getInstance(Context context) {
		if (instance == null) {
			instance = new NetworkManager(context);
		}
		return instance;
	}

	public RequestQueue getRequestQueue() {
		if (requestQueue == null) {
			requestQueue = Volley.newRequestQueue(context.getApplicationContext());
		}
		return requestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req) {
		getRequestQueue().add(req);
	}

	public ImageLoader getImageLoader() {
		return imageLoader;
	}

	public static String uploadBitmap(Bitmap bitmap) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);


		final String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(Calendar.getInstance().getTime());

		byte[] data = bos.toByteArray();

		String boundary = "====UPhonebookxsaiIASckasoapsck====";
		String contentDisposition = "Content-Disposition: form-data; name=\"image\"; filename=\"" + timeStamp + ".jpg\"";

		String contentType = "Content-Type: image/jpeg";
		String lineEnd = "\r\n";
		String hyphens = "--";

		URL url = new URL(ServerManager.getServerURL() + "/tracks/upload_image.php");

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

		/*
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
		*/
		StringBuilder responseBuilder = new StringBuilder();
		BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line;
		while ((line = buffer.readLine())!= null)
			responseBuilder.append(line + "\n");

		buffer.close();
		return responseBuilder.toString().trim();
	}
}
