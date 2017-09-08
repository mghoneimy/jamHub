package com.Example.iJam.models;

import com.Example.iJam.interfaces.JamHUBInterface;
import com.Example.iJam.network.ServerManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Khodary on 7/5/15.
 */
public class Track implements JamHUBInterface, Serializable {
    private String title;                //track name
    private String uploader;            //track auploader name
    //private String band;               //true it the uploader is band
    private int id;                //track id
    private int duration;               //in seconds
    private String imageUrl;              //image source location
    private String trackUrl;
    private String upload_date;           //track upload date
    private String tags;     //track tags
    private int ancestor;             //ID of ancestor track, null if solo
    private ArrayList<Track> children;          //ID of children tracks, empty if last jam
    //private ArrayList<String> contributors;         //names of contributing users
    //private ArrayList<String> instruments;          //names of instrucments involved
    private String instrument;
    private int likes;                  //number of likes
    private double rating;
    private int raters;

    public Track(String title, String uploader, int id, int duration, String imageUrl, String trackUrl,
                 String upload_date, String tags, int ancestor,
                 String instrument, int likes, double rating) {
        this.title = title;
        this.id = id;
        this.duration = duration;
        this.imageUrl = imageUrl;
        this.trackUrl = trackUrl;
        this.upload_date = upload_date;
        this.tags = tags;
        this.ancestor = ancestor;
        this.instrument = instrument;
        this.likes = likes;
        this.rating = rating;
        this.uploader = uploader;
    }

    public Track(String uploader, boolean band, String instrument, Track ancestor) {
        this.title = ancestor.title;
        this.uploader = uploader;
        //this.band = band;
        //trackID = ++id;
        this.duration = ancestor.duration;
        this.tags = ancestor.tags;
        this.ancestor = ancestor.getID();
        children = null;
        //contributors = ancestor.contributors;
        //contributors.add(ancestor.uploader);
        this.instrument = instrument;
        likes = 0;
        rating = 0;
        raters = 0;

        ancestor.children.add(this);
    }

    public Track(String title, String instrument, String tags, String trackUrl,
                 String imageUrl, int duration, String uploader) {
        this.title = title;
        this.instrument = instrument;
        this.tags = tags;
        this.upload_date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        this.trackUrl = trackUrl;
        this.imageUrl = imageUrl;
        this.duration = duration;
        this.uploader = uploader;
    }

    public Track(String title, String instrument, String tags, int ancestor, String trackUrl,
                 String imageUrl, int duration, String uploader) {
        this.title = title;
        this.instrument = instrument;
        this.tags = tags;
        this.ancestor = ancestor;
        this.upload_date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        this.trackUrl = trackUrl;
        this.imageUrl = imageUrl;
        this.duration = duration;
        this.uploader = uploader;
    }

    public Track() {
        title = null;
        uploader = null;
        //band = 0;
        id = 0;
        duration = 0;
        imageUrl = null;
        upload_date = null;
        tags = null;
        ancestor = 0;
        children = null;
        //contributors;
        //instruments;
        instrument = null;
        likes = 0;
        rating = 0.0;
        raters = 0;
    }

    public static JSONArray toJsonArray(ArrayList<Track> tracks) throws JSONException {
        JSONArray trackArray = new JSONArray();
        for (Track track : tracks) {
            trackArray.put(track.toJSONObject());
        }
        return trackArray;
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject trackOb = new JSONObject();
        trackOb.put("name", title);
        trackOb.put("user_name", uploader);
        trackOb.put("band_name", JSONObject.NULL);
        trackOb.put("img_url", imageUrl);
        trackOb.put("track_url", trackUrl);
        trackOb.put("upload_date", upload_date);
        trackOb.put("duration", duration);
        trackOb.put("tags", tags);
        trackOb.put("instrument", instrument);
        trackOb.put("ancestor_id", ancestor);

        return trackOb;
    }

    public static ArrayList<Track> parseJson(JSONArray jArray) throws JSONException {
        ArrayList<Track> myTracks = new ArrayList<>();

        for (int i = 0; i < jArray.length(); i++) {
            JSONObject ob = jArray.getJSONObject(i);

            int duration = ob.getInt("duration");
            int likes = ob.getInt("likes");
            int id = ob.getInt("track_id");
            double rating = ob.getDouble("rating");
            String title = ob.getString("track_name");
            String uploadDate = ob.getString("upload_date");
            String instrument = ob.getString("instrument");
            String imgUrl = ServerManager.getServerURL() + ob.getString("img_url");
            String tags = ob.getString("tags");
            String band = ob.getString("band_name");
            String trackUrl = ServerManager.getServerURL() + ob.getString("track_url");
            String user_name = ob.getString("user_name");
            int ancestor = ob.getInt("ancestor_id");
            //ArrayList<String> tags = new ArrayList<>();

                /*JSONArray t = ob.getJSONArray("Tags");
                for(int j = 0; j < t.length(); j++ ){
                    JSONObject ob2 = t.getJSONObject(j);

                    String tag = ob2.getString("Tag"+Integer.toString(j));
                    tags.add(tag);
                }*/
            Track track;

            if (user_name == null)
                track = new Track(title, band, id, duration, imgUrl, trackUrl,
                        uploadDate, tags, ancestor, instrument, likes, rating);
            else
                track = new Track(title, user_name, id, duration, imgUrl, trackUrl,
                        uploadDate, tags, ancestor, instrument, likes, rating);

            myTracks.add(track);
        }
        return myTracks;
    }

    @Override
    public void setImgUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public void setUser_name(String title) {
        this.title = title;
    }

    @Override
    public void setID(int id) {
        this.id = id;
    }

    @Override
    public void setUpload_date(String upload_date) {
        this.upload_date = upload_date;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setAncestor(int ancestor) {
        this.ancestor = ancestor;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public void setChildren(ArrayList<Track> children) {
        this.children = children;
    }

    public void setTrackUrl(String trackUrl) {
        this.trackUrl = trackUrl;
    }

    public ArrayList<Track> getChildren() {
        return children;
    }

    public String getTrackUrl() {
        return trackUrl;
    }

    @Override
    public String getImgUrl() {
        return imageUrl;
    }

    @Override
    public String getUser_name() {
        return title;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public String getUpload_date() {
        return upload_date;
    }

    public double getRating() {
        return rating;
    }

    public int getLikes() {
        return likes;
    }

    public String getUploader() {
        return uploader;
    }

    public int getDuration() {
        return duration;
    }

    public String getTags() {
        return tags;
    }

    public int getAncestor() {
        return ancestor;
    }

    public String getInstrument() {
        return instrument;
    }

    public ArrayList<String> getContributors() {
        ArrayList<String> contributors = new ArrayList<String>();
        Track node = this;
        do {
            contributors.add(node.getUploader());
        } while (node.getAncestor() != 0);

        return contributors;
    }

    public ArrayList<String> getInstruments() {
        ArrayList<String> instruments = new ArrayList<String>();
        Track node = this;
        do {
            instruments.add(node.getInstrument());
        } while (node.getAncestor() != 0);

        return instruments;
    }
}