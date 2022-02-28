package com.laghouati.projet_laghouati;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends ArrayAdapter<String> {

    ArrayList<String> images;

    Context context;

    int resource;

    public ImageAdapter(Context context, int resource, ArrayList<String> images){
        super(context, resource,R.id.textImage, images);
        this.context = context;
        this.resource = resource;
        this.images = images;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(resource, null, false);

        ImageView imageView = view.findViewById(R.id.imagePiece);
        TextView textView = view.findViewById(R.id.textImage);

        String image = images.get(position);
        Picasso.get().load("https://myhouse.lesmoulinsdudev.com/"+image).into(imageView);

        if(image.contains("bedroom"))
            textView.setText("Chambre");
        else if(image.contains("kitchen"))
            textView.setText("Cuisine");
        else if(image.contains("garage"))
            textView.setText("Garage");
        else
            textView.setText(image);

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(resource, null, false);

        ImageView imageView = view.findViewById(R.id.imagePiece);
        TextView textView = view.findViewById(R.id.textImage);


        String image = images.get(position);
        Picasso.get().load("https://myhouse.lesmoulinsdudev.com/"+image).into(imageView);

        if(image.contains("bedroom"))
            textView.setText("Chambre");
        else if(image.contains("kitchen"))
            textView.setText("Cuisine");
        else if(image.contains("garage"))
            textView.setText("Garage");
        else
            textView.setText(image);

        return view;
    }


}
