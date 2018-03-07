package com.tiderdev.dilo.popularmovie;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tiderdev.dilo.popularmovie.adapter.MovieAdapter;
import com.tiderdev.dilo.popularmovie.adapter.MoviesCursorAdapter;
import com.tiderdev.dilo.popularmovie.data.MoviesContract;
import com.tiderdev.dilo.popularmovie.model.MovieItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    final String BASE_MOVIE_URL = "https://api.themoviedb.org/3/movie/popular?";
    final String API_KEY = "796b78a940cdb3ba4ac81d7d423b34a6";

    private final static int MOVIES_LOADER = 100;

    MoviesCursorAdapter movieAdapter;
    private GridView gvMovies;

    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(this);

        gvMovies = (GridView) findViewById(R.id.gvMovies);
        View emptyView = findViewById(R.id.emptyView);
        gvMovies.setEmptyView(emptyView);

        movieAdapter = new MoviesCursorAdapter(this, null, 0);
        gvMovies.setAdapter(movieAdapter);

        fetchDataFromServer();
    }

    private void fetchDataFromServer() {
        Uri uri = Uri.parse(BASE_MOVIE_URL).buildUpon()
                .appendQueryParameter("api_key", API_KEY)
                .build();

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                uri.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        parseJson(response);

//                        List<MovieItem> result = parseJson(response);
                        Log.d("RESS", "Response : " + response);

//                        if (result != null) {
//                            movieAdapter.clear();
//
//                            for (MovieItem item : result) {
//                                movieAdapter.add(item);
//                            }
//                            movieAdapter.notifyDataSetChanged();
//                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        queue.add(stringRequest);
    }

    private void parseJson(String response) {
        final String TMDB_RESULT = "results";
        final String TMDB_URL_IMAGE = "poster_path";
        final String TMDB_VOTE = "vote_average";
        final String TMDB_ID = "id";
        final String TMDB_OVERVIEW = "overview";
        final String TMDB_TITLE = "original_title";
        List<MovieItem> resultMovies = new ArrayList<>();

        try {

            JSONObject objResponse = new JSONObject(response);
            JSONArray resultArray = objResponse.getJSONArray(TMDB_RESULT);

            int arraySize = resultArray.length();
            Vector<ContentValues> cvVector = new Vector<>(arraySize);


            for (int i = 0; i < arraySize; i++) {
                JSONObject movieObject = resultArray.getJSONObject(i);
                String urlImage = movieObject.getString(TMDB_URL_IMAGE);
                double voteMovie = movieObject.getDouble(TMDB_VOTE);
                int id = movieObject.getInt(TMDB_ID);
                String overview = movieObject.getString(TMDB_OVERVIEW);
                String title = movieObject.getString(TMDB_TITLE);

                ContentValues value = new ContentValues();
                value.put(MoviesContract.MoviesTable._ID, id);
                value.put(MoviesContract.MoviesTable.COLUMN_ORIGINAL_TITLE, title);
                value.put(MoviesContract.MoviesTable.COLUMN_POSTER_PATH, urlImage);
                value.put(MoviesContract.MoviesTable.COLUMN_VOTE_AVARAGE, voteMovie);
                value.put(MoviesContract.MoviesTable.COLUMN_OVERVIEW, overview);

                cvVector.add(value);
//                MovieItem movieItem = new MovieItem(urlImage, voteMovie);
//                resultMovies.add(movieItem);
            }

            Log.d("vector", String.valueOf(cvVector.size()));

            if(cvVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cvVector.size()];
                cvVector.toArray(cvArray);

                getContentResolver().bulkInsert(MoviesContract.MoviesTable.CONTENT_URI, cvArray);
            }

            getContentResolver().query(
                    MoviesContract.MoviesTable.CONTENT_URI,
                    MoviesContract.MoviesTable.MOVIES_PROJECTIONS,
                    null,
                    null,
                    null);

        } catch (JSONException e) {
            e.printStackTrace();
        }

//        return resultMovies;
    }


    @Override
    protected void onStart() {
        getSupportLoaderManager().initLoader(MOVIES_LOADER, null, this);
        super.onStart();


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this,
                MoviesContract.MoviesTable.CONTENT_URI,
                MoviesContract.MoviesTable.MOVIES_PROJECTIONS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        movieAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
