package com.Example.iJam.models;

import com.Example.iJam.interfaces.JamHUBInterface;

import java.util.ArrayList;

/**
 * Created by Mostafa on 6/29/2015.
 */
public class Band implements JamHUBInterface {
    private int band_id;
    private String imageUrl;
    private String name;
    private String author;
    private String upload_date;
    private ArrayList<User> members;
    private ArrayList<Track> tracks;

    public Band(String imageUrl, String name, String author) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.author = author;
    }

    public Band(String imageUrl, String name, String author, ArrayList<User> members, ArrayList<Track> tracks) {

        this.imageUrl = imageUrl;
        this.name = name;
        this.author = author;
        this.members = members;
        this.tracks = tracks;
    }

    public Band(){
        name = null;
        band_id = 0;
        members = null;
        tracks = null;
    }

    @Override
    public int getID() {
        return band_id;
    }

    @Override
    public String getUpload_date() {
        return upload_date;
    }

    @Override
    public String getUser_name() {
        return name;
    }

    @Override
    public String getImgUrl() {
        return imageUrl;
    }

    public ArrayList<Track> getTracks() {
        return tracks;
    }

    public ArrayList<User> getUsers() {
        return members;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setMembers(ArrayList<User> members) {
        this.members = members;
    }

    public void addMember(User member){
        members.add(member);
    }

    public void addTrack(Track track){
        tracks.add(track);
    }

    public void setTracks(ArrayList<Track> tracks) {
        this.tracks = tracks;
    }

    public void setUser_name(String name) {
        this.name = name;
    }

    @Override
    public void setID(int id) {
        this.band_id = id;
    }

    @Override
    public void setUpload_date(String upload_date) {
        this.upload_date = upload_date;
    }

    @Override
    public void setImgUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}