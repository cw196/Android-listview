package com.example.wang.JSON;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.example.wang.movies.Movie;

public class JSONParser {
    private static final String	TAG	= "JSONParser";

    public static Movie parseMovieJSON(JSONObject json_movie) {

        Movie movie = new Movie();
        movie.thumb_url=json_movie.optString("urlPoster");
        movie.title = json_movie.optString("title");
        movie.mpaa_rating = json_movie.optString("rated");
        movie.rating= (float) json_movie.optDouble("rating");
        movie.synopsis=json_movie.optString("plot");
        movie.movie_url=json_movie.optString("urlIMDB");

        return movie;
    }

    public static ArrayList<Movie> parseMovieListJSON(JSONArray json_movie_list) {
        ArrayList<Movie> movies = new ArrayList<Movie>();

        try {
            Log.d(TAG, "ok");


            for (int i = 0; i < json_movie_list.length(); i++) {

                JSONObject current_date_infro = json_movie_list.getJSONObject(i);
                JSONArray movie_list=current_date_infro.getJSONArray("movies");
                for(int j =0; j<movie_list.length();j++){
                    JSONObject movie_property = movie_list.getJSONObject(j);
                    movies.add(parseMovieJSON(movie_property));
                }
            }
        } catch (JSONException e) {
            Log.d(TAG, "JSONException");
            e.printStackTrace();
            return movies;
        }
        return movies;
    }

}

