package com.Example.iJam.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.Example.iJam.R;
import com.Example.iJam.models.Track;
import com.Example.iJam.network.NetworkManager;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Khodary on 7/5/15.
 */
public class TrackAdapter extends ArrayAdapter<Track> {
    private final Activity context;
    private final Class<?> context2;
    ArrayList<Track> items;

    public static HashMap<String, Bitmap> myImages = new HashMap<String, Bitmap>();


    public TrackAdapter(Activity context, Class<?> context2, ArrayList<Track> items) {
        super(context, R.layout.track_row_layout, items);
        this.items = items;
        this.context = context;
        this.context2 = context2;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.track_row_layout, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.txtTitle);
        TextView txtLikes = (TextView) rowView.findViewById(R.id.txtLikes);
        TextView txtRating = (TextView) rowView.findViewById(R.id.txtRating);
        TextView txtUploader = (TextView)rowView.findViewById(R.id.txtUploader);
        NetworkImageView iv = (NetworkImageView) rowView.findViewById(R.id.img_trackimage);

        final Track item = items.get(position);

        final String title = item.getUser_name();
        final String likes = Integer.toString(item.getLikes());
        final String rating = Double.toString(item.getRating());
        final String uploader = (item.getUploader());
        final String imgUrl = item.getImgUrl();

        txtTitle.setText("Title: " + title);
        txtLikes.setText("Likes Count: " + likes);
        txtRating.setText("Rating: " + rating + "/5");
        txtUploader.setText("Uploader: "+ uploader);
        iv.setImageUrl(imgUrl, NetworkManager.getInstance(context).getImageLoader());
        /*
        NetworkManager.getInstance(context).getImageLoader().get("URL",
                new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                        Bitmap image = response.getBitmap();
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }//, 100, 100, ImageView.ScaleType.CENTER);
        */

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, context2);
                i.putExtra("track", item);
                context.startActivity(i);
            }
        });
        return rowView;
    }
}