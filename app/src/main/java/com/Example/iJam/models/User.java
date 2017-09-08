package com.Example.iJam.models;

import com.Example.iJam.interfaces.JamHUBInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Mostafa on 6/29/2015.
 */
public class User implements JamHUBInterface, Serializable {
    private int user_id;
    private String user_name;
    private String password;
    private String first_name;
    private String last_name;
    private String imgUrl;
    private String email;
    private String upload_date;
    private ArrayList<Track> tracks;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User(/*int user_id,*/ String user_name, String password, String fname,
                String lname, String imgUrl, String email) {
        //this.user_id = user_id;
        this.user_name = user_name;
        this.password = password;
        this.first_name = fname;
        this.last_name = lname;
        this.imgUrl = imgUrl;
        this.email = email;
        tracks = null;
    }

    /*public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }*/

    @Override
    public String getImgUrl() {
        return imgUrl;
    }

    @Override
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String getUser_name() {
        return user_name;
    }

    @Override
    public int getID() {
        return user_id;
    }

    @Override
    public String getUpload_date() {
        return upload_date;
    }

    @Override
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    @Override
    public void setID(int id) {
        this.user_id = id;
    }

    @Override
    public void setUpload_date(String upload_date) {
        this.upload_date = upload_date;
    }

    public String getPassword() {
        return password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setFirst_name(String name) {
        first_name = name;
    }

    public void setLast_name(String name) {
        last_name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /*public void addTrack(String trackName, int duration, String imgurl, String tags, String instrument){
        Track temp = new Track(trackName, user_name, false, duration, imgurl, tags, instrument);
        tracks.add(temp);
    }*/

    public void jamOn(Track original, String instrument) {
        Track temp = new Track(user_name, false, instrument, original);
        tracks.add(temp);
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject userOb = new JSONObject();

        userOb.put("user_name", user_name);
        userOb.put("password", password);
        userOb.put("first_name", first_name);
        userOb.put("last_name", last_name);
        userOb.put("email", email);
        userOb.put("img_url", imgUrl);

        return userOb;
    }

    public static User parseJson(JSONObject jOb) throws JSONException {
        String password = jOb.getString("password");
        String imgUrl = jOb.getString("img_url");
        String email = jOb.getString("email");
        String fName = jOb.getString("first_name");
        String lName = jOb.getString("last_name");
        String user_name = jOb.getString("user_name");

        User user = new User(user_name, password, fName, lName, imgUrl, email);

        return user;
    }
}
