package com.Example.iJam.interfaces;

import com.Example.iJam.models.Track;

import java.util.ArrayList;

/**
 * Created by Khodary on 7/5/15.
 */
public interface TrackInterface {
    public void setImageUrl(String imageUrl);
    public void setTitle(String title);
    public void setRating(double rating);
    public void setLikes(int likes);
    public void setUploader(String uploader);
    public void setDuration(int duration);
    public void setTags(ArrayList<String> tags);
    public void setAncestor(Track ancestor);
    public void setID(int id);
    public void setInstrument(String instrument);
    public void setChildren(ArrayList<Track> children);
    public void setUpload_date(String date);

    public String getImageUrl();
    public String getTitle();
    public double getRating();
    public int getLikes();
    public String getUploader();
    public int getDuration();
    public ArrayList<String> getTags();
    public Track getAncestor();
    public int getID();
    public String getInstrument();
    public String getUpload_date();
}
