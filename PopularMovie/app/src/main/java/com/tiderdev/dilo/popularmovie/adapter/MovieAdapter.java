package com.tiderdev.dilo.popularmovie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tiderdev.dilo.popularmovie.R;
import com.tiderdev.dilo.popularmovie.model.MovieItem;

public class MovieAdapter extends ArrayAdapter<MovieItem> {

    final String IMAGE_URL = "http://image.tmdb.org/t/p/w185/";
    Context ctx;

    public MovieAdapter(Context context) {
        super(context, R.layout.movie_grid_item);
        ctx = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.movie_grid_item, parent, false);


        MovieItem movieItem = getItem(position);
        String imageUrl = IMAGE_URL + movieItem.getUrlImage();
        double voteValue = movieItem.getVote();

        ImageView imageView = (ImageView) v.findViewById(R.id.ivMoview);
        TextView textView = (TextView) v.findViewById(R.id.tvVote);

        Picasso.with(getContext()).load(imageUrl).into(imageView);
        textView.setText(String.valueOf(voteValue));

        return v;
    }
}

