package com.laghouati.projet_laghouati;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.laghouati.projet_laghouati.Piece;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PieceAdapter extends ArrayAdapter<Piece> {

    ArrayList<Piece> pieces;

    Context context;

    int resource;

    public PieceAdapter(Context context, int resource, ArrayList<Piece> pieces){
        super(context, resource, pieces);
        this.context = context;
        this.resource = resource;
        this.pieces = pieces;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(resource, null, false);

        ImageView imageView = view.findViewById(R.id.piecePicture);
        TextView textName = view.findViewById(R.id.pieceName);

        Piece piece = pieces.get(position);

        Picasso.get().load("https://myhouse.lesmoulinsdudev.com/"+piece.getPicture()).into(imageView);
        textName.setText(piece.getName());

        return view;
    }

    }
