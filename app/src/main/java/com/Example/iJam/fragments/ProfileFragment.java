package com.Example.iJam.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.Example.iJam.R;
import com.Example.iJam.activities.AboutUsActivity;
import com.Example.iJam.activities.EdittProfileActivity;
import com.Example.iJam.activities.MainActivity;
import com.Example.iJam.activities.MyTracksActivity;
import com.Example.iJam.activities.SignInActivity;
import com.Example.iJam.network.HttpGetTask;
import com.Example.iJam.network.NetworkManager;
import com.Example.iJam.network.ServerManager;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//import android.app.Fragment;

public class ProfileFragment extends Fragment {
    ListView listV;
    NetworkImageView profileImage;
    TextView userName, userTracks, userJams;

    //private FloatingActionButton FAB;
    //LinearLayout linearList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_profile, container, false);

        final String[] profItems = new String[]{"Profile", "Settings", "My Tracks", "About us", "Sign Out"};
        final ArrayList<String> list = new ArrayList<String>();
        final String uName = MainActivity.user.getUser_name();
        final String lName = MainActivity.user.getLast_name();
        final String fName = MainActivity.user.getFirst_name();
        final String imgUrl = MainActivity.user.getImgUrl();

        listV = (ListView) v.findViewById(R.id.listView2);
        userName = (TextView) v.findViewById(R.id.tv_profilename);
        userTracks = (TextView) v.findViewById(R.id.tv_profiletracks);
        userJams = (TextView) v.findViewById(R.id.tv_profilejams);
        profileImage = (NetworkImageView) v.findViewById(R.id.profile_img_userimage);

        profileImage.setImageUrl(imgUrl, NetworkManager.getInstance(getActivity()).getImageLoader());
        userName.setText("Welcome, " + fName + "!");

        new HttpGetTask(ServerManager.getServerURL()+"/tracks/get_tracks.php?uname="+uName, getActivity()){
            @Override
            protected void onPostExecute(String s) {
                try {
                    JSONObject response = new JSONObject(s);
                    if(response.getString("status").equals("success")) {
                        userTracks.setText(response.getString("tracks") + " tracks");
                        userJams.setText(response.getString("jams") + " jams");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();


        for (int i = 0; i < profItems.length; ++i) {
            list.add(profItems[i]);
        }
        listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    Intent edit =new Intent(getActivity(), EdittProfileActivity.class);
                    startActivity(edit);
                } else if (position == 1) {


                } else if (position == 2) {
                    Intent mytracks=new Intent(getActivity(), MyTracksActivity.class);
                    startActivity(mytracks);

                } else if (position == 3) {
                    Intent aboutus = new Intent(getActivity(), AboutUsActivity.class);
                    startActivity(aboutus);
                } else if (position == 4) {
                    Intent signout = new Intent(getActivity(), SignInActivity.class);
                    SharedPreferences settings = getActivity().getSharedPreferences(SignInActivity.PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();

                    editor.putBoolean("signIn", false);
                    editor.commit();

                    startActivity(signout);
                    getActivity().finish();
                }
            }
        });

        listV.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_expandable_list_item_1, profItems);
        listV.setAdapter(adapter);

        return v;
    }

    public static ProfileFragment newInstance(String text) {

        ProfileFragment f = new ProfileFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }


}