package com.Example.iJam.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.Example.iJam.R;
import com.Example.iJam.adapters.TrackAdapter;
import com.Example.iJam.models.Track;
import com.Example.iJam.network.HttpGetTask;
import com.Example.iJam.network.ServerManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MyTracksActivity extends AppCompatActivity {

    private ListView lvTracks;
    public static ArrayList<Track> myTracks = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tracks);

        lvTracks = (ListView) findViewById(R.id.activity_mytracks_list);

        String url=null;
        try {
            String uname = MainActivity.user.getUser_name();
            url = ServerManager.getServerURL()+"/users/my_tracks.php?name="+ URLEncoder.encode(uname, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        new HttpGetTask(url, getApplicationContext()){
            @Override
            protected void onPostExecute(String s) {
                if(s==null)
                    return;
                try {
                    JSONObject response = new JSONObject(s);
                    if(response.getString("status").equals("success")) {
                        JSONArray jArray = new JSONArray(response.getString("results"));
                        try {
                            myTracks = Track.parseJson(jArray);
                            TrackAdapter tracksAdap = new TrackAdapter(MyTracksActivity.this, MainTrackDetailActivity.class, myTracks);
                            lvTracks.setAdapter(tracksAdap);

                        }catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(ctx, "Failed to retreive data!", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_tracks, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}



