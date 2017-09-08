package com.Example.iJam.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.Example.iJam.models.Band;
import com.Example.iJam.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Khodary on 7/7/15.
 */
public class BandAdapter extends ArrayAdapter<Band>{
    private final Activity context;
    private final Class<?> context2;
    ArrayList<Band> items;

    public static HashMap<String, Bitmap> myImages = new HashMap<String, Bitmap>();


    public BandAdapter(Activity context, Class<?> context2, ArrayList<Band> items) {
        super(context, R.layout.track_row_layout, items);
        this.items = items;
        this.context = context;
        this.context2 = context2;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.band_row_layout, null, true);

        TextView txtName = (TextView) rowView.findViewById(R.id.txtBand);
        TextView txtTracks = (TextView) rowView.findViewById(R.id.txtTracks);
        TextView txtAuthor = (TextView) rowView.findViewById(R.id.txtAuthor);
        final ImageView imageView = (ImageView) rowView.findViewById(R.id.ivBand);
        final Band item = items.get(position);

        txtName.setText(item.getUser_name());
        txtAuthor.setText(item.getAuthor());
        txtTracks.setText(item.getTracks().size());
        //imageView.setImageBitmap();

        /*
        rowView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, context2);
                context.startActivity(i);
            }
        });*/
        return rowView;
    }
}
