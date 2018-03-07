package com.tiderdev.dilo.popularmovie.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tiderdev.dilo.popularmovie.R;
import com.tiderdev.dilo.popularmovie.data.MoviesContract;

/**
 * Created by dwikadarmawan on 7/23/16.
 */
public class MoviesCursorAdapter extends CursorAdapter {

    final String IMAGE_URL = "http://image.tmdb.org/t/p/w185/";

    public MoviesCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {


        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_grid_item, parent, false);

        ViewHolder viewHolder= new ViewHolder(view);
        view.setTag(viewHolder);


        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        Picasso.with(context).load(IMAGE_URL + cursor.getString(MoviesContract.MoviesTable.COL_POSTER))
                .into(viewHolder.ivMovie);

        viewHolder.tvVote.setText(String.valueOf(cursor.getDouble(MoviesContract.MoviesTable.COL_VOTE)));
    }

    public class ViewHolder {

        private final ImageView ivMovie;
        private final TextView tvVote;

        public ViewHolder(View view) {
            ivMovie = (ImageView) view.findViewById(R.id.ivMoview);
            tvVote = (TextView) view.findViewById(R.id.tvVote);

        }
    }
}
